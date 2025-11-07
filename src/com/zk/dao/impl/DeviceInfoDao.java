package com.zk.dao.impl;

import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import java.sql.PreparedStatement;
import com.zk.dao.BaseDao;
import com.zk.dao.IBaseDao;
import com.zk.exception.DaoException;
import com.zk.pushsdk.po.DeviceInfo;

/**
 * Device info database access layer, operations: Add, Delete, Modify, Query.
 * @author seiya
 *
 */
public class DeviceInfoDao extends BaseDao implements IBaseDao<DeviceInfo>{
	public static final String TABLE_NAME = "device_info";
    private static Logger logger = Logger.getLogger(DeviceInfoDao.class);
    
    /**
 	 * Add new device info to database
 	 * @param entity
 	 * Device info object
 	 * @throws DaoException
 	 */
	public void add(DeviceInfo entity) throws DaoException {
		String sql = "insert into device_info (device_sn, device_name, " +
		"alias_name, dept_id, state, last_activity, trans_times, " +
		"trans_interval, log_stamp, op_log_stamp, photo_stamp," +
		"fw_version, user_count, fp_count, trans_count, fp_alg_ver, " +
		"push_version, device_type, ipaddress, dev_language, push_comm_key,face_count, face_alg_ver, palm, mask,temperature,time_zone, bioData_Stamp, idCard_Stamp, errorLog_Stamp) " +
		"values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
		try {
			PreparedStatement pst = getConnection().prepareStatement(sql);
			int index = 1;
			pst.setString(index++, entity.getDeviceSn());
			pst.setString(index++, entity.getDeviceName());
			pst.setString(index++, entity.getAliasName());
			pst.setInt(index++, entity.getDeptId());
			pst.setString(index++, entity.getState());
			pst.setString(index++, entity.getLastActivity());
			pst.setString(index++, entity.getTransTimes());
			pst.setInt(index++, entity.getTransInterval());
			pst.setString(index++, entity.getLogStamp());
			pst.setString(index++, entity.getOpLogStamp());
			pst.setString(index++, entity.getPhotoStamp());
			pst.setString(index++, entity.getFirmwareVersion());
			pst.setInt(index++, entity.getUserCount());
			pst.setInt(index++, entity.getFpCount());
			pst.setInt(index++, entity.getTransCount());
			pst.setString(index++, entity.getFpAlgVersion());
			pst.setString(index++, entity.getPushVersion());
			pst.setString(index++, entity.getDeviceType());
			pst.setString(index++, entity.getIpAddress());
			pst.setString(index++, entity.getDevLanguage());
			pst.setString(index++, entity.getPushCommKey());
			pst.setInt(index++, entity.getFaceCount());
			pst.setString(index++, entity.getFaceAlgVer());
			pst.setInt(index++, entity.getPalm());
			pst.setInt(index++, entity.getMask());
			pst.setString(index++, entity.getTemperature());
			pst.setString(index++, entity.getTimeZone());
			pst.setString(index++, entity.getBioDataStamp());
			pst.setString(index++, entity.getIdCardStamp());
			pst.setString(index++, entity.getErrorLogStamp());

			pst.addBatch();
			pst.executeBatch();
			pst.close();
		} catch (Exception e) {
			rollback();
			logger.error(e);
			throw new DaoException(e);
		}
		
	}
	
	/**
	 * Delete device info by ID
	 * @param id
	 * Device info's ID
	 * @throws DaoException
	 */
	public void delete(int id) throws DaoException {
		try {
			String sql = (new StringBuilder())
			.append("delete from device_info where device_id=")
			.append(id).toString();
			Statement st = getConnection().createStatement();
			st.executeUpdate(sql);
			st.close();
		} catch (Exception e) {
			rollback();
			logger.error(e);
			throw new DaoException(e);
		}
	}
	
	/**
	 * Delete device info by condition
	 * @param cond
	 * Delete condition
	 * @throws DaoException
	 */
	public void delete(String cond) throws DaoException {
		try {
			String sql = (new StringBuilder())
			.append("delete from device_info where 1=1 ")
			.append(cond).toString();
			Statement st = getConnection().createStatement();
			st.executeUpdate(sql);
			st.close();
		} catch (Exception e) {
			rollback();
			logger.error(e);
			throw new DaoException(e);
		}	
	}
	
	/**
	 * Get device info by ID
	 * @param id
	 * Device info's ID
	 * @return
	 * Device info object <code>DeviceInfo</code>
	 * @throws DaoException
	 */
	public DeviceInfo fatch(int id) throws DaoException {
		try {
			String sql = (new StringBuilder()).
			append("select device_id, device_sn, device_name, alias_name, " +
					"dept_id, state, last_activity, trans_times, trans_interval, " +
					"log_stamp, op_log_stamp, photo_stamp, fw_version, user_count, " +
					"fp_count, trans_count, fp_alg_ver, push_version, device_type, " +
					"ipaddress, dev_language, push_comm_key, face_count, face_alg_ver, reg_face_count, dev_funs,palm,mask,temperature, bioData_Stamp, idCard_Stamp, errorLog_Stamp" +
					" from device_info where " +
					"device_id=").append(id).toString();
            Statement st = getConnection().createStatement();
            ResultSet rs = st.executeQuery(sql);
            DeviceInfo info = null;
            if (rs.next()) {
            	info = new DeviceInfo();
            	info.setDeviceId(rs.getInt("device_id"));
            	info.setDeviceSn(rs.getString("device_sn"));
            	info.setDeviceName(rs.getString("device_name"));
            	info.setAliasName(rs.getString("alias_name"));
            	info.setDeptId(rs.getInt("dept_id"));
            	info.setState(rs.getString("state"));
            	info.setLastActivity(rs.getString("last_activity"));
            	info.setTransTimes(rs.getString("trans_times"));
            	info.setTransInterval(rs.getInt("trans_interval"));
            	info.setLogStamp(rs.getString("log_stamp"));
            	info.setOpLogStamp(rs.getString("op_log_stamp"));
            	info.setPhotoStamp(rs.getString("photo_stamp"));
            	info.setFirmwareVersion(rs.getString("fw_version"));
            	info.setUserCount(rs.getInt("user_count"));
            	info.setFpCount(rs.getInt("fp_count"));
            	info.setTransCount(rs.getInt("trans_count"));
            	info.setFpAlgVersion(rs.getString("fp_alg_ver"));
            	info.setPushVersion(rs.getString("push_version"));
            	info.setDeviceType(rs.getString("device_type"));
            	info.setIpAddress(rs.getString("ipaddress"));
            	info.setDevLanguage(rs.getString("dev_language"));
            	info.setPushCommKey(rs.getString("push_comm_key"));
            	info.setFaceCount(rs.getInt("face_count"));
            	info.setFaceAlgVer(rs.getString("face_alg_ver"));
            	info.setRegFaceCount(rs.getInt("reg_face_count"));
            	info.setDevFuns(rs.getString("dev_funs"));
            	
            	info.setPalm(rs.getInt("palm"));
            	info.setMask(rs.getInt("mask"));
            	info.setTemperature(rs.getString("temperature"));
            	info.setBioDataStamp(rs.getString("bioData_Stamp"));
            	info.setIdCardStamp(rs.getString("idCard_Stamp"));
            	info.setErrorLogStamp(rs.getString("errorLog_Stamp"));
            }
            
            rs.close();
            st.close();
            return info;
		} catch (Exception e) {
			 logger.error(e);
	         throw new DaoException(e);
		}
	}
	
	/**
	 * Get device info by serial number 
	 * @param deviceSn
	 * Device serial number
	 * @return
	 * Device info <code>DeviceInfo</code>
	 * @throws DaoException
	 */
	public DeviceInfo fatchDeviceInfoBySn(String deviceSn) throws DaoException {
		System.out.println("SN=="+deviceSn );
		try {
			String sql = (new StringBuilder()).
			append("select device_id, device_sn, device_name, alias_name, " +
					"dept_id, state, last_activity, trans_times, trans_interval, " +
					"log_stamp, op_log_stamp, photo_stamp, fw_version, user_count, " +
					"fp_count, trans_count, fp_alg_ver, push_version, device_type, " +
					"ipaddress, dev_language, push_comm_key, face_count, face_alg_ver, reg_face_count, dev_funs, palm, mask, temperature, time_zone, bioData_Stamp, idCard_Stamp, errorLog_Stamp " +
					" from device_info where " +
					"device_sn='").append(deviceSn).append("'").toString();
            Statement st = getConnection().createStatement();
            ResultSet rs = st.executeQuery(sql);
            DeviceInfo info = null;
            if (rs.next()) {
            	info = new DeviceInfo();
            	info.setDeviceId(rs.getInt("device_id"));
            	info.setDeviceSn(rs.getString("device_sn"));
            	
            	info.setDeviceName(rs.getString("device_name"));
            	info.setAliasName(rs.getString("alias_name"));
            	info.setDeptId(rs.getInt("dept_id"));
            	info.setState(rs.getString("state"));
            	info.setLastActivity(rs.getString("last_activity"));
            	info.setTransTimes(rs.getString("trans_times"));
            	info.setTransInterval(rs.getInt("trans_interval"));
            	info.setLogStamp(rs.getString("log_stamp"));
            	info.setOpLogStamp(rs.getString("op_log_stamp"));
            	info.setPhotoStamp(rs.getString("photo_stamp"));
            	info.setFirmwareVersion(rs.getString("fw_version"));
            	info.setUserCount(rs.getInt("user_count"));
            	info.setFpCount(rs.getInt("fp_count"));
            	info.setTransCount(rs.getInt("trans_count"));
            	info.setFpAlgVersion(rs.getString("fp_alg_ver"));
            	info.setPushVersion(rs.getString("push_version"));
            	info.setDeviceType(rs.getString("device_type"));
            	info.setIpAddress(rs.getString("ipaddress"));
            	info.setDevLanguage(rs.getString("dev_language"));
            	info.setPushCommKey(rs.getString("push_comm_key"));
            	info.setFaceCount(rs.getInt("face_count"));
            	info.setFaceAlgVer(rs.getString("face_alg_ver"));
            	info.setRegFaceCount(rs.getInt("reg_face_count"));
            	info.setDevFuns(rs.getString("dev_funs"));
            	info.setPalm(Integer.parseInt(rs.getString("palm")));
            	info.setMask(Integer.parseInt(rs.getString("mask")));
            	info.setTemperature(rs.getString("temperature"));
            	info.setTimeZone(rs.getString("time_zone"));
            	info.setBioDataStamp(rs.getString("bioData_Stamp"));
            	info.setIdCardStamp(rs.getString("idCard_Stamp"));
            	info.setErrorLogStamp(rs.getString("errorLog_Stamp"));
            }
            
            rs.close();
            st.close();
            return info;
		} catch (Exception e) {
			 logger.error(e);
	         throw new DaoException(e);
		}
	}
	
	/**
	 * Get device info list by condition
	 * @param cond
	 * Query condition
	 * @return
	 * Device info <code>DeviceInfo</code> list
	 * @throws DaoException
	 */
	public List<DeviceInfo> fatchList(String cond) throws DaoException {
		
		List<DeviceInfo> list = new ArrayList<DeviceInfo>();
		try {
			String sql = (new StringBuilder()).
			append("select device_id, device_sn, device_name, alias_name, " +
					"dept_id, state, last_activity, trans_times, trans_interval, " +
					"log_stamp, op_log_stamp, photo_stamp, fw_version, user_count, " +
					"fp_count, trans_count, fp_alg_ver, push_version, device_type, " +
					"ipaddress, dev_language, push_comm_key, face_count, face_alg_ver, reg_face_count, dev_funs,palm,mask,temperature, time_zone,"
					+ " bioData_Stamp, idCard_Stamp, errorLog_Stamp" +
					" from device_info where " +
					"1=1 ").append(cond).toString();
            Statement st = getConnection().createStatement();
            ResultSet rs = st.executeQuery(sql);
            
           while(rs.next()) {
            	DeviceInfo info = new DeviceInfo();
            	info.setDeviceId(rs.getInt("device_id"));
            	info.setDeviceSn(rs.getString("device_sn"));
            	info.setDeviceName(rs.getString("device_name"));
            	info.setAliasName(rs.getString("alias_name"));
            	info.setDeptId(rs.getInt("dept_id"));
            	info.setState(rs.getString("state"));
            	info.setLastActivity(rs.getString("last_activity"));
            	info.setTransTimes(rs.getString("trans_times"));
            	info.setTransInterval(rs.getInt("trans_interval"));
            	info.setLogStamp(rs.getString("log_stamp"));
            	info.setOpLogStamp(rs.getString("op_log_stamp"));
            	info.setPhotoStamp(rs.getString("photo_stamp"));
            	info.setFirmwareVersion(rs.getString("fw_version"));
            	info.setUserCount(rs.getInt("user_count"));
            	info.setFpCount(rs.getInt("fp_count"));
            	info.setTransCount(rs.getInt("trans_count"));
            	info.setFpAlgVersion(rs.getString("fp_alg_ver"));
            	info.setPushVersion(rs.getString("push_version"));
            	info.setDeviceType(rs.getString("device_type"));
            	info.setIpAddress(rs.getString("ipaddress"));
            	info.setDevLanguage(rs.getString("dev_language"));
            	info.setPushCommKey(rs.getString("push_comm_key"));
            	info.setFaceCount(rs.getInt("face_count"));
            	info.setFaceAlgVer(rs.getString("face_alg_ver"));
            	info.setRegFaceCount(rs.getInt("reg_face_count"));
            	info.setDevFuns(rs.getString("dev_funs"));
            	
            	info.setPalm(rs.getInt("palm"));
            	info.setMask(rs.getInt("mask"));
            	info.setTemperature(rs.getString("temperature"));
            	info.setTimeZone(rs.getString("time_zone"));
            	info.setBioDataStamp(rs.getString("bioData_Stamp"));
            	info.setIdCardStamp(rs.getString("idCard_Stamp"));
            	info.setErrorLogStamp(rs.getString("errorLog_Stamp"));
            	
            	list.add(info);
            }
           
           	rs.close();
           	st.close();
            return list;
		} catch (Exception e) {
			 logger.error(e);
	         throw new DaoException(e);
		}
	}
	
	/**
	 * Get device info by condition use for interface display
	 * 
	 * @param cond
	 * Query condition
	 * @param startRec
	 * Begin record position
	 * @param pageSize
	 * Query size
	 * @return
	 * Device info list
	 * @throws DaoException
	 */
	public List<DeviceInfo> fatchListWithCount(String cond, int startRec, int pageSize) throws DaoException {
		List<DeviceInfo> list = new ArrayList<DeviceInfo>();
		try {
			/**call stored procedure*/
			CallableStatement st = getConnection().prepareCall("{call get_device_for_page(?,?,?)}");
			int index = 1;
			st.setString(index++, cond);
			st.setInt(index++, startRec);
			st.setInt(index++, pageSize);
			ResultSet rs = st.executeQuery();
           while(rs.next()) {
            	DeviceInfo info = new DeviceInfo();
            	info.setDeviceId(rs.getInt("device_id"));
            	info.setDeviceSn(rs.getString("device_sn"));
            	info.setDeviceName(rs.getString("device_name"));
            	info.setAliasName(rs.getString("alias_name"));
            	info.setDeptId(rs.getInt("dept_id"));
            	info.setState(rs.getString("state"));
            	info.setLastActivity(rs.getString("last_activity"));
            	info.setTransTimes(rs.getString("trans_times"));
            	info.setTransInterval(rs.getInt("trans_interval"));
            	info.setLogStamp(rs.getString("log_stamp"));
            	info.setOpLogStamp(rs.getString("op_log_stamp"));
            	info.setPhotoStamp(rs.getString("photo_stamp"));
            	info.setFirmwareVersion(rs.getString("fw_version"));
            	info.setUserCount(rs.getInt("user_count"));
            	info.setFpCount(rs.getInt("fp_count"));
            	info.setTransCount(rs.getInt("trans_count"));
            	info.setFpAlgVersion(rs.getString("fp_alg_ver"));
            	info.setPushVersion(rs.getString("push_version"));
            	info.setDeviceType(rs.getString("device_type"));
            	info.setIpAddress(rs.getString("ipaddress"));
            	info.setDevLanguage(rs.getString("dev_language"));
            	info.setPushCommKey(rs.getString("push_comm_key"));
            	info.setFaceCount(rs.getInt("face_count"));
            	info.setFaceAlgVer(rs.getString("face_alg_ver"));
            	info.setRegFaceCount(rs.getInt("reg_face_count"));
            	info.setDevFuns(rs.getString("dev_funs"));
            	
            	info.setActUserCount(rs.getInt("act_user_count"));
            	info.setActFpCount(rs.getInt("act_fp_count"));
            	info.setActFaceCount(rs.getInt("act_face_count"));
            	info.setActTransCount(rs.getInt("act_att_count"));
            //	System.out.println("rs.getInt"+rs.getInt("mask"));
            	info.setPalm(rs.getInt("palm"));
            	info.setMask(rs.getInt("mask"));
            	info.setTemperature(rs.getString("temperature"));
            	info.setTimeZone(rs.getString("time_zone"));
            	info.setBioDataStamp(rs.getString("bioData_Stamp"));
            	info.setIdCardStamp(rs.getString("idCard_Stamp"));
            	info.setErrorLogStamp(rs.getString("errorLog_Stamp"));
            	
            	list.add(info);
            }
           	rs.close();
           	st.close();
            return list;
		} catch (Exception e) {
			 logger.error(e);
	         throw new DaoException(e);
		}
	}
	
	/**
	 * Get counts of device info by condition
	 * 
	 * @param cond
	 * Query condition
	 * @return
	 */
	public int fatchCount(String cond) {
		int count = 0;
		String sql = "select count(*) count_rec from device_info where 1=1 " + cond;
		try {
			Statement st = getConnection().createStatement();
			ResultSet rs = st.executeQuery(sql);
			if (rs.next()) {
				count = rs.getInt("count_rec");
			}
			rs.close();
			st.close();
		} catch (Exception e) {
			logger.error(e);
			throw new DaoException(e);
		}
		return count;
	}
	
	/**
	 * Update device info
	 * @param entity
	 * Device info
	 * @throws DaoException
	 */
	public void update(DeviceInfo entity) throws DaoException {
		String sql = (new StringBuilder()).
		append("update device_info set device_sn=?, device_name=?, alias_name=?, " +
				"dept_id=?, state=?, last_activity=?, trans_times=?, " +
				"trans_interval=?, log_stamp=?, op_log_stamp=?, photo_stamp=?," +
				"fw_version=?, user_count=?, fp_count=?, trans_count=?, " +
				"fp_alg_ver=?, push_version=?, device_type=?, ipaddress=?, " +
				"dev_language=?, push_comm_key=?, face_count=?, face_alg_ver=?,  reg_face_count=?, dev_funs=?, mask=?, palm=?,temperature=?, time_zone=?, bioData_Stamp=?, idCard_Stamp=?, errorLog_Stamp=? where device_id=? ").toString();
		try {
			PreparedStatement pst = getConnection().prepareStatement(sql);
			int index = 1;
			pst.setString(index++, entity.getDeviceSn());
			pst.setString(index++, entity.getDeviceName());
			pst.setString(index++, entity.getAliasName());
			pst.setInt(index++, entity.getDeptId());
			pst.setString(index++, entity.getState());
			pst.setString(index++, entity.getLastActivity());
			pst.setString(index++, entity.getTransTimes());
			pst.setInt(index++, entity.getTransInterval());
			pst.setString(index++, entity.getLogStamp());
			pst.setString(index++, entity.getOpLogStamp());
			pst.setString(index++, entity.getPhotoStamp());
			pst.setString(index++, entity.getFirmwareVersion());
			pst.setInt(index++, entity.getUserCount());
			pst.setInt(index++, entity.getFpCount());
			pst.setInt(index++, entity.getTransCount());
			pst.setString(index++, entity.getFpAlgVersion());
			pst.setString(index++, entity.getPushVersion());
			pst.setString(index++, entity.getDeviceType());
			pst.setString(index++, entity.getIpAddress());
			pst.setString(index++, entity.getDevLanguage());
			pst.setString(index++, entity.getPushCommKey());
			pst.setInt(index++, entity.getFaceCount());
			pst.setString(index++, entity.getFaceAlgVer());
			pst.setInt(index++, entity.getRegFaceCount());
			pst.setString(index++, entity.getDevFuns());
			
			pst.setInt(index++, entity.getPalm());
			pst.setInt(index++, entity.getMask());
			pst.setString(index++, entity.getTemperature());
			pst.setString(index++, entity.getTimeZone());
			
			
			pst.setString(index++, entity.getBioDataStamp());
			pst.setString(index++, entity.getIdCardStamp());
			pst.setString(index++, entity.getErrorLogStamp());
			
			pst.setInt(index++, entity.getDeviceId());
			
			
			pst.addBatch();
			pst.executeBatch();
			pst.close();
		} catch (Exception e) {
			rollback();
			logger.error(e);
			throw new DaoException(e);
		}
	}
	
	public void updateOptions(DeviceInfo entity) throws DaoException {
		String sql = (new StringBuilder()).
		append("update device_info set mask=?, temperature=?, palm=? where device_id=? ").toString();
		try {
			PreparedStatement pst = getConnection().prepareStatement(sql);
			
			int index = 1;
			pst.setInt(index++, entity.getMask());
			pst.setString(index++, entity.getTemperature());
			pst.setInt(index++, entity.getPalm());
			
			pst.setInt(index++, entity.getDeviceId());
			
			pst.executeUpdate();
			pst.close();
		} catch (Exception e) {
			rollback();
			logger.error(e);
			throw new DaoException(e);
		}
	}
	
	public void updateTimeZoneOption(DeviceInfo entity) throws DaoException {
		String sql = (new StringBuilder()).
		append("update device_info set time_zone=? where device_id=? ").toString();
		try {
			PreparedStatement pst = getConnection().prepareStatement(sql);
			
			int index = 1;
			pst.setString(index++, entity.getTimeZone());
			pst.setInt(index++, entity.getDeviceId());
			
			//System.out.println("After : " + pst.toString());
			pst.executeUpdate();
			pst.close();
		} catch (Exception e) {
			rollback();
			logger.error(e);
			throw new DaoException(e);
		}
	}
	
	
	/**
	 * Update device status and last refresh time
	 * 
	 * @param deviceSn
	 * Device serial number
	 * @param state
	 * Device status
	 * @param lastActive
	 * Last refresh time
	 * @throws DaoException
	 */
	public void updateState(String deviceSn, String state, String lastActive) throws DaoException {
		StringBuilder sql = new StringBuilder();
		sql.append("update device_info set device_sn=? ");
		if (null != state && !"".equals(state)) {
			sql.append(" , state=?");
		}
		if (null != lastActive && !"".equals(lastActive)) {
			sql.append(" , last_activity=? ");
		}
		sql.append(" where device_sn=? ");
		try {
			PreparedStatement pst = getConnection().prepareStatement(sql.toString());
			int index = 1;
			pst.setString(index++, deviceSn);
			if (null != state && !"".equals(state)) {
				pst.setString(index++, state);
			}
			if (null != lastActive && !"".equals(lastActive)) {
				pst.setString(index++, lastActive);
			}
			pst.setString(index++, deviceSn);
			pst.addBatch();
			pst.executeBatch();
			pst.close();
		} catch (Exception e) {
			rollback();
			logger.error(e);
			throw new DaoException(e);
		}
	}

	public int truncate() throws DaoException {
		// TODO Auto-generated method stub
		return 0;
	}
}
