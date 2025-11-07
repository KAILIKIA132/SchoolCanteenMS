package com.zk.pushsdk;

import com.zk.pushsdk.process.ConnectProcess;
import com.zk.pushsdk.process.DownloadProcess;
import com.zk.pushsdk.process.RealTimeProcess;
import com.zk.pushsdk.process.UploadProcess;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


/**
 * This class is the servlet entrance to map the device requests to the server.
 * The mapping can be configured by changing the parameters in web.xml.
 * @author seiya
 *
 */
public class PushCommServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final ConnectProcess connProc = new ConnectProcess();
	private static final UploadProcess upProc = new UploadProcess();
	private static final DownloadProcess downProc = new DownloadProcess();
	private static final RealTimeProcess rtProc = new RealTimeProcess();

	/**
	 * The list of device request commands 
	 * @author seiya
	 *
	 */
	public enum CmdEnum {
		/** 
		 * Command cdata, included functions:
		 * <li>Device exchange initialization information with Server.
		 * <li>Device upload attendance log data to Server.
		 * <li>Remote attendance.
		 * <li>Background identification.
		 * */
		CDATA, 
		/**
		 * Command getrequest, included functions:
		 * <li>Device inform Server there are updates.
		 * <li>Device gain commands from server.
		 */
		GETREQUEST, 
		/**
		 * Command devicecmd, Device use to response the commands from Server 
		 */
		DEVICECMD, 
		/**Undefined command*/
		NOTFOUND, 
		/**Command fdata, discarded*/
		FDATA, 
		/**Command registry, reserved*/
		REGISTRY, 
		/**Command login, reserved*/
		LOGIN, 
		/**Command rtdata, reserved*/
		RTDATA,
		/**Command querydata, reserved*/
		QUERYDATA
	}

	private static final Map<String, CmdEnum> urlMap = new HashMap<String, CmdEnum>();
	
	static {
		urlMap.put("/cdata", CmdEnum.CDATA);
		urlMap.put("/getrequest", CmdEnum.GETREQUEST);
		urlMap.put("/devicecmd", CmdEnum.DEVICECMD);
		urlMap.put("/fdata", CmdEnum.FDATA);
		urlMap.put("/registry", CmdEnum.REGISTRY);
		urlMap.put("/login", CmdEnum.LOGIN);
		urlMap.put("/rtdata", CmdEnum.RTDATA);
		urlMap.put("/querydata", CmdEnum.QUERYDATA);
	}

	public PushCommServlet() {
		super();
	}

	public void init() {
	}

	public void destroy() {
		super.destroy();
	}

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doRequest(request, response, "GET");
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doRequest(request, response, "POST");
	}
	
	/**
	 * Extract command info from the request header, then process commands with the right function
	 * @param request
	 * @param response
	 * @param method
	 * @throws ServletException
	 * @throws IOException
	 */
	private void doRequest(HttpServletRequest request, HttpServletResponse response, String method) {
		String path = request.getPathInfo();
		CmdEnum cmdName = urlMap.get(path.contains(".") ? path.substring(0,path.indexOf(".")) : path);
		cmdName = cmdName != null ? cmdName : CmdEnum.NOTFOUND;
		
		switch (cmdName) {
		case CDATA:
			if ("GET".equals(method)) {
				upProc.getCdata(request, response);
			} else if ("POST".equals(method)) {
				upProc.postCdata(request, response);
			}
			break;
		case GETREQUEST:
			if ("GET".equals(method)) {
				downProc.getrequest(request, response);	
			}
			break;
		case DEVICECMD:
			if ("POST".equals(method)) {
				downProc.devicecmd(request, response);
			}
			break;
		case FDATA:
			upProc.fdata(request, response);
			break;
		case LOGIN:
			connProc.login(request, response);
			break;
		case REGISTRY:
			connProc.registry(request, response);
			break;
		case RTDATA:
			rtProc.rtdata(request, response);
			break;
		case QUERYDATA:
			downProc.querydata(request, response);
			break;
		default:
			/**Process the request that Device want to download files from Server*/
			downProc.devDownloadFile(request, response);
			break;
		}
	}
}