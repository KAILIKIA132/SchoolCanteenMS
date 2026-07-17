package com.zk.servlet;

import com.zk.dao.impl.ApiVerificationReport;
import com.zk.dao.impl.ApiVerificationReportDao;
import com.zk.manager.ManagerFactory;
import com.zk.pushsdk.po.AttLog;
import com.zk.pushsdk.po.DeviceCommand;
import com.zk.pushsdk.po.DeviceInfo;
import com.zk.pushsdk.po.UserInfo;
import com.zk.pushsdk.util.PushUtil;
import com.zk.util.ConfigUtil;
import com.zk.util.JsonWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

/**
 * JSON REST API dispatcher mounted at /api/*.
 * Authenticates via X-API-Key header (or ?apikey= query param) against config.xml /root/api/apikey.
 *
 *   GET  /api/health                  — no auth, returns server status
 *   GET  /api/users                   — list users (q[deviceSn], q[userPin], q[page], q[size])
 *   GET  /api/users/{id}              — single user by userId
 *   GET  /api/devices                 — list devices (q[page], q[size])
 *   GET  /api/devices/connected       — live connected devices from PushUtil.devMaps
 *   GET  /api/attlogs                 — list attendance log (q[deviceSn], q[userPin], q[page], q[size])
 *   GET  /api/verifications           — list ApiVerificationReport rows (q[status], q[userPin], q[deviceSn])
 *   GET  /api/commands                — list device_command rows (q[deviceSn], q[page], q[size])
 *   POST /api/users/sync-all?userId=1,2 — push selected users to every connected device
 */
public class ApiServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        dispatch(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        dispatch(req, resp);
    }

    private void dispatch(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json;charset=UTF-8");
        resp.setHeader("Access-Control-Allow-Origin", "*");
        resp.setHeader("Access-Control-Allow-Headers", "X-API-Key, Content-Type");
        resp.setHeader("Access-Control-Allow-Methods", "GET, POST, OPTIONS");

        String path = req.getPathInfo();
        if (path == null) path = "/";

        // Health is unauthenticated
        if ("/health".equals(path)) {
            writeJson(resp, 200, mapOf(
                "status", "ok",
                "time", new Date().toString(),
                "connectedDevices", PushUtil.devMaps != null ? PushUtil.devMaps.size() : 0
            ));
            return;
        }

        if (!authorized(req)) {
            writeJson(resp, 401, mapOf("error", "Unauthorized — missing or invalid X-API-Key header"));
            return;
        }

        try {
            if ("/users".equals(path) || "/users/".equals(path)) {
                handleUsersList(req, resp);
            } else if (path.startsWith("/users/sync-all")) {
                handleSyncAll(req, resp);
            } else if (path.startsWith("/users/")) {
                handleUserById(req, resp, path.substring("/users/".length()));
            } else if ("/devices".equals(path) || "/devices/".equals(path)) {
                handleDevicesList(req, resp);
            } else if ("/devices/connected".equals(path)) {
                handleConnectedDevices(req, resp);
            } else if ("/attlogs".equals(path) || "/attlogs/".equals(path)) {
                handleAttLogs(req, resp);
            } else if ("/verifications".equals(path) || "/verifications/".equals(path)) {
                handleVerifications(req, resp);
            } else if ("/commands".equals(path) || "/commands/".equals(path)) {
                handleCommands(req, resp);
            } else if ("/".equals(path) || "".equals(path)) {
                handleIndex(resp);
            } else {
                writeJson(resp, 404, mapOf("error", "Unknown endpoint: " + path));
            }
        } catch (Exception e) {
            writeJson(resp, 500, mapOf("error", "Internal error: " + e.getMessage()));
        }
    }

    /* ---------- auth ---------- */

    private boolean authorized(HttpServletRequest req) {
        String expected = ConfigUtil.getInstance().getValue("api.apikey");
        if (expected == null || expected.isEmpty()) return false;
        String given = req.getHeader("X-API-Key");
        if (given == null || given.isEmpty()) given = req.getParameter("apikey");
        return expected.equals(given);
    }

    /* ---------- index ---------- */

    private void handleIndex(HttpServletResponse resp) throws IOException {
        Map<String, Object> body = new LinkedHashMap<String, Object>();
        body.put("name", "M-KOPA Canteen Device Manager API");
        body.put("version", "1.0");
        body.put("auth", "X-API-Key header required (except /api/health)");
        List<Map<String, Object>> endpoints = new ArrayList<Map<String, Object>>();
        endpoints.add(endpoint("GET", "/api/health", "Server health and connected device count (no auth)"));
        endpoints.add(endpoint("GET", "/api/users", "List users. Params: deviceSn, userPin, page, size"));
        endpoints.add(endpoint("GET", "/api/users/{id}", "Get one user by userId"));
        endpoints.add(endpoint("POST", "/api/users/sync-all?userId=1,2", "Push users to every connected device"));
        endpoints.add(endpoint("GET", "/api/devices", "List devices. Params: page, size"));
        endpoints.add(endpoint("GET", "/api/devices/connected", "Live connected devices snapshot"));
        endpoints.add(endpoint("GET", "/api/attlogs", "List attendance log. Params: deviceSn, userPin, page, size"));
        endpoints.add(endpoint("GET", "/api/verifications", "List meal-card verification reports. Params: status, userPin, deviceSn"));
        endpoints.add(endpoint("GET", "/api/commands", "List device_command rows. Params: deviceSn, page, size"));
        body.put("endpoints", endpoints);
        writeJson(resp, 200, body);
    }

    private Map<String, Object> endpoint(String method, String path, String desc) {
        Map<String, Object> m = new LinkedHashMap<String, Object>();
        m.put("method", method);
        m.put("path", path);
        m.put("description", desc);
        return m;
    }

    /* ---------- users ---------- */

    private void handleUsersList(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String deviceSn = req.getParameter("deviceSn");
        String userPin = req.getParameter("userPin");
        int page = parseInt(req.getParameter("page"), 1);
        int size = parseInt(req.getParameter("size"), 200);
        int start = (page - 1) * size;

        int total = ManagerFactory.getUserInfoManager().getUserInfoCount(deviceSn, userPin);
        List<UserInfo> users = ManagerFactory.getUserInfoManager().fatchAllUser(deviceSn, userPin, start, size);

        List<Map<String, Object>> rows = new ArrayList<Map<String, Object>>();
        if (users != null) for (UserInfo u : users) rows.add(userMap(u));

        Map<String, Object> body = new LinkedHashMap<String, Object>();
        body.put("page", page);
        body.put("size", size);
        body.put("total", total);
        body.put("count", rows.size());
        body.put("users", rows);
        writeJson(resp, 200, body);
    }

    private void handleUserById(HttpServletRequest req, HttpServletResponse resp, String idStr) throws IOException {
        int id;
        try { id = Integer.parseInt(idStr); }
        catch (NumberFormatException e) { writeJson(resp, 400, mapOf("error", "Invalid user id")); return; }
        UserInfo u = ManagerFactory.getUserInfoManager().getUserInfoById(id);
        if (u == null) { writeJson(resp, 404, mapOf("error", "User not found")); return; }
        writeJson(resp, 200, userMap(u));
    }

    private void handleSyncAll(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        final String userIdStr = req.getParameter("userId");
        if (userIdStr == null || userIdStr.isEmpty()) {
            writeJson(resp, 400, mapOf("error", "userId parameter required (comma-separated)"));
            return;
        }
        final String[] ids = userIdStr.split(",");
        final Set<String> sns = PushUtil.devMaps != null ? PushUtil.devMaps.keySet() : null;
        if (sns == null || sns.isEmpty()) {
            writeJson(resp, 409, mapOf("error", "No connected devices"));
            return;
        }
        new Thread(new Runnable() {
            public void run() {
                for (String sn : sns) {
                    try { ManagerFactory.getCommandManager().createUpdateUserInfosCommandByIds(ids, sn); }
                    catch (Exception ignored) {}
                }
            }
        }).start();
        writeJson(resp, 202, mapOf(
            "status", "queued",
            "userCount", ids.length,
            "deviceCount", sns.size()
        ));
    }

    private Map<String, Object> userMap(UserInfo u) {
        Map<String, Object> m = new LinkedHashMap<String, Object>();
        m.put("userId", u.getUserId());
        m.put("userPin", u.getUserPin());
        m.put("name", u.getName());
        m.put("privilege", u.getPrivilege());
        m.put("category", u.getCategory());
        m.put("mainCard", u.getMainCard());
        m.put("deviceSn", u.getDeviceSn());
        m.put("fpCount", u.getUserFpCount());
        m.put("faceCount", u.getUserFaceCount());
        m.put("palmCount", u.getUserPalmCount());
        m.put("hasPhoto", u.getPhotoIdName() != null && !u.getPhotoIdName().isEmpty());
        return m;
    }

    /* ---------- devices ---------- */

    private void handleDevicesList(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        int page = parseInt(req.getParameter("page"), 1);
        int size = parseInt(req.getParameter("size"), 200);
        int start = (page - 1) * size;
        List<DeviceInfo> devices = ManagerFactory.getDeviceManager().getDeviceInfoListForPage(null, start, size);

        List<Map<String, Object>> rows = new ArrayList<Map<String, Object>>();
        if (devices != null) for (DeviceInfo d : devices) rows.add(deviceMap(d));
        Map<String, Object> body = new LinkedHashMap<String, Object>();
        body.put("page", page);
        body.put("size", size);
        body.put("count", rows.size());
        body.put("devices", rows);
        writeJson(resp, 200, body);
    }

    private void handleConnectedDevices(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        List<Map<String, Object>> rows = new ArrayList<Map<String, Object>>();
        if (PushUtil.devMaps != null) {
            for (Map.Entry<String, DeviceInfo> e : PushUtil.devMaps.entrySet()) {
                Map<String, Object> m = deviceMap(e.getValue());
                m.put("sn", e.getKey());
                rows.add(m);
            }
        }
        writeJson(resp, 200, mapOf("count", rows.size(), "devices", rows));
    }

    private Map<String, Object> deviceMap(DeviceInfo d) {
        Map<String, Object> m = new LinkedHashMap<String, Object>();
        if (d == null) return m;
        m.put("deviceId", d.getDeviceId());
        m.put("deviceSn", d.getDeviceSn());
        m.put("deviceName", d.getDeviceName());
        m.put("aliasName", d.getAliasName());
        m.put("state", d.getState());
        m.put("lastActivity", d.getLastActivity());
        m.put("firmwareVersion", d.getFirmwareVersion());
        return m;
    }

    /* ---------- attlogs ---------- */

    private void handleAttLogs(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String deviceSn = req.getParameter("deviceSn");
        String userPin = req.getParameter("userPin");
        int page = parseInt(req.getParameter("page"), 1);
        int size = parseInt(req.getParameter("size"), 200);
        int start = (page - 1) * size;

        List<AttLog> logs = ManagerFactory.getAttLogManager().getAttLogList(deviceSn, userPin, start, size);
        List<Map<String, Object>> rows = new ArrayList<Map<String, Object>>();
        if (logs != null) {
            for (AttLog l : logs) {
                Map<String, Object> m = new LinkedHashMap<String, Object>();
                m.put("attLogId", l.getAttLogId());
                m.put("userPin", l.getUserPin());
                m.put("userName", l.getUserName());
                m.put("deviceSn", l.getDeviceSn());
                m.put("verifyTime", l.getVerifyTime());
                m.put("verifyType", l.getVerifyType());
                m.put("verifyTypeStr", l.getVerifyTypeStr());
                m.put("status", l.getStatus());
                m.put("statusStr", l.getStatusStr());
                m.put("maskFlag", l.getMaskFlag());
                m.put("temperature", l.getTemperatureReading());
                m.put("workCode", l.getWorkCode());
                rows.add(m);
            }
        }
        Map<String, Object> body = new LinkedHashMap<String, Object>();
        body.put("page", page);
        body.put("size", size);
        body.put("count", rows.size());
        body.put("attlogs", rows);
        writeJson(resp, 200, body);
    }

    /* ---------- verifications (meal-card API reports) ---------- */

    private void handleVerifications(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String status = req.getParameter("status");
        String userPin = req.getParameter("userPin");
        String deviceSn = req.getParameter("deviceSn");
        StringBuilder cond = new StringBuilder();
        if (status != null && !status.isEmpty()) cond.append(" AND status='").append(escapeSql(status)).append("'");
        if (userPin != null && !userPin.isEmpty()) cond.append(" AND user_pin='").append(escapeSql(userPin)).append("'");
        if (deviceSn != null && !deviceSn.isEmpty()) cond.append(" AND device_sn='").append(escapeSql(deviceSn)).append("'");
        cond.append(" LIMIT 500");

        ApiVerificationReportDao dao = new ApiVerificationReportDao();
        List<ApiVerificationReport> list = dao.query(cond.toString());
        List<Map<String, Object>> rows = new ArrayList<Map<String, Object>>();
        if (list != null) {
            for (ApiVerificationReport r : list) {
                Map<String, Object> m = new LinkedHashMap<String, Object>();
                m.put("reportId", r.getReportId());
                m.put("userPin", r.getUserPin());
                m.put("userName", r.getUserName());
                m.put("studentId", r.getStudentId());
                m.put("verificationTime", r.getVerificationTime());
                m.put("apiCallTime", r.getApiCallTime() != null ? r.getApiCallTime().toString() : null);
                m.put("mealType", r.getMealType());
                m.put("status", r.getStatus());
                m.put("responseCode", r.getResponseCode());
                m.put("responseMessage", r.getResponseMessage());
                m.put("errorMessage", r.getErrorMessage());
                m.put("deviceSn", r.getDeviceSn());
                rows.add(m);
            }
        }
        Map<String, Object> body = new LinkedHashMap<String, Object>();
        body.put("count", rows.size());
        body.put("verifications", rows);
        writeJson(resp, 200, body);
    }

    /* ---------- commands ---------- */

    private void handleCommands(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String deviceSn = req.getParameter("deviceSn");
        int page = parseInt(req.getParameter("page"), 1);
        int size = parseInt(req.getParameter("size"), 200);
        int start = (page - 1) * size;

        List<DeviceCommand> cmds = ManagerFactory.getCommandManager().getDeviceCommandList(deviceSn, null, start, size);
        List<Map<String, Object>> rows = new ArrayList<Map<String, Object>>();
        if (cmds != null) {
            for (DeviceCommand c : cmds) {
                Map<String, Object> m = new LinkedHashMap<String, Object>();
                m.put("cmdId", c.getDevCmdId());
                m.put("deviceSn", c.getDeviceSn());
                m.put("cmdContent", c.getCmdContent());
                m.put("cmdCommitTime", c.getCmdCommitTime());
                m.put("cmdTransTime", c.getCmdTransTime());
                m.put("cmdOverTime", c.getCmdOverTime());
                m.put("cmdReturn", c.getCmdReturn());
                m.put("cmdReturnInfo", c.getCmdReturnInfo());
                rows.add(m);
            }
        }
        Map<String, Object> body = new LinkedHashMap<String, Object>();
        body.put("page", page);
        body.put("size", size);
        body.put("count", rows.size());
        body.put("commands", rows);
        writeJson(resp, 200, body);
    }

    /* ---------- helpers ---------- */

    private void writeJson(HttpServletResponse resp, int status, Object body) throws IOException {
        resp.setStatus(status);
        PrintWriter w = resp.getWriter();
        w.write(JsonWriter.write(body));
        w.flush();
    }

    private int parseInt(String s, int def) {
        if (s == null || s.isEmpty()) return def;
        try { return Integer.parseInt(s); } catch (NumberFormatException e) { return def; }
    }

    private String escapeSql(String s) {
        return s.replace("'", "''");
    }

    private Map<String, Object> mapOf(Object... kv) {
        Map<String, Object> m = new LinkedHashMap<String, Object>();
        for (int i = 0; i + 1 < kv.length; i += 2) m.put(String.valueOf(kv[i]), kv[i+1]);
        return m;
    }
}
