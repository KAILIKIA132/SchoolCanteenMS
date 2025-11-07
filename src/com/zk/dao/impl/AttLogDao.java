package com.zk.dao.impl;

import java.sql.BatchUpdateException;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import java.sql.PreparedStatement;
import com.zk.dao.BaseDao;
import com.zk.dao.IBaseDao;
import com.zk.exception.DaoException;
import com.zk.pushsdk.po.AttLog;

/**
 * Attendance record database access layer, operations: Add, Delete, Modify, Query.
 * @author seiya
 *
 */
public class AttLogDao extends BaseDao implements IBaseDao<AttLog> {
	
	public static final String TABLE_NAME = "att_log";
    private static Logger logger = Logger.getLogger(AttLogDao.class);

    /**
	 * Add new attendance record to the database
	 * @param entity
	 * Attendance record object
	 * @throws DaoException
	 */
	public void add(AttLog entity) throws DaoException {
		String sql = "insert into att_log(user_pin, verify_type, verify_time, " +
				"status, work_code, sensor_no, att_flag, device_sn, reserved1, reserved2, mask, temperature) " +
				"values(?,?,?,?,?,?,?,?,?,?,?,?)";
		try {
			PreparedStatement pst = getConnection().prepareStatement(sql);
			int index = 1;
			pst.setString(index++, entity.getUserPin());
			pst.setInt(index++, entity.getVerifyType());
			pst.setString(index++, entity.getVerifyTime());
			pst.setInt(index++, entity.getStatus());
			pst.setInt(index++, entity.getWorkCode());
			pst.setInt(index++, entity.getSensorNo());
			pst.setInt(index++, entity.getAttFlag());
			pst.setString(index++, entity.getDeviceSn());
			pst.setInt(index++, entity.getReserved1());
			pst.setInt(index++, entity.getReserved2());
			pst.setInt(index++, entity.getMaskFlag());
			pst.setString(index++, entity.getTemperatureReading());
			
			pst.addBatch();
			pst.executeBatch();
			pst.close();
			
		} catch (Exception e) {
			logger.error(e);
			throw new DaoException(e);
		}
		
		
	}

	/**
	 * Add or update the attendance record
	 * @param entity
	 * Attendance record object
	 * @return
	 */
	public int addOrUpdate(AttLog entity) {
		try {
			/**Add operation*/
			add(entity);
		} catch (DaoException e) {
			try {
				/**If the attendance exist, then do the update operation*/
				if (e.getCause() instanceof BatchUpdateException) {
					BatchUpdateException be = (BatchUpdateException)e.getCause();
					/**Repeat record wrong code: 1062*/
					if (1062 == be.getErrorCode()) {
						update(entity);
					}
				}
			} catch (DaoException e2) {
				logger.error(e2);
				throw new DaoException(e2);
			}
		}
		return 0;
	}
	
	/**
	 * Delete the attendance record by ID
	 * @param id
	 * ID of the attendance record
	 * @throws DaoException
	 */
	public void delete(int id) throws DaoException {
		try {
			String sql = (new StringBuilder())
			.append("delete from att_log where att_log_id=")
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
	 * Delete attendance record by conditions
	 * @param cond
	 * Delete conditions
	 * @throws DaoException
	 */
	public void delete(String cond) throws DaoException {
		try {
			String sql = (new StringBuilder())
			.append("delete from att_log where 1=1 ")
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
	 * Get the attendance records by ID
	 * @param id
	 * ID of the attendance record
	 * @return
	 * Attendance record object <code>AttLog</code>
	 * @throws DaoException
	 */
	public AttLog fatch(int id) throws DaoException {
		try {
			String sql = (new StringBuilder()).
			append("select att_log_id, user_pin, verify_type, verify_time, " +
					"status, work_code, sensor_no, att_flag, device_sn, reserved1, reserved2 " +
					"from att_log where att_log_id=").append(id).toString();
            Statement st = getConnection().createStatement();
            ResultSet rs = st.executeQuery(sql);
            AttLog log = null;
            if (rs.next()) {
            	log = new AttLog();
            	log.setAttLogId(rs.getInt("att_log_id"));
            	log.setUserPin(rs.getString("user_pin"));
            	log.setVerifyType(rs.getInt("verify_type"));
            	log.setVerifyTime(rs.getString("verify_time"));
            	log.setStatus(rs.getInt("status"));
            	log.setWorkCode(rs.getInt("work_code"));
            	log.setSensorNo(rs.getInt("sensor_no"));
            	log.setAttFlag(rs.getInt("att_flag"));
            	log.setDeviceSn(rs.getString("device_sn"));
            	log.setReserved1(rs.getInt("reserved1"));
            	log.setReserved2(rs.getInt("reserved2"));
            	log.setMaskFlag(rs.getInt("mask"));
            	log.setTemperatureReading(rs.getString("temperature"));
            }
            
            rs.close();
            st.close();
            return log;
		} catch (Exception e) {
			 logger.error(e);
	         throw new DaoException(e);
		}
	}

	/**
	 * Get attendance records by conditions, use for interface display
	 * 
	 * @param cond
	 * Query condition
	 * @param startRec
	 * Begin record position
	 * @param pageSize
	 * Query size
	 * @return
	 * List of attendance records
	 * @throws DaoException
	 */
	public List<AttLog> fatchList(String cond, int startRec, int pageSize) throws DaoException {
		List<AttLog> list = new ArrayList<AttLog>();
		String sql = (new StringBuilder()).
		append("select att_log_id, user_pin, verify_type, verify_time, " +
				"status, work_code, sensor_no, att_flag, device_sn, reserved1, reserved2, " +
				"(select name from user_info where DEVICE_SN=a.DEVICE_SN and USER_PIN=a.USER_PIN) user_name " +
				"from att_log a where 1=1 ").append(cond).append(" limit ").append(startRec).append(", ").append(pageSize).toString();
		try {
			Statement st = getConnection().createStatement();
			ResultSet rs;
			rs = st.executeQuery(sql);
			while (rs.next()) {
				AttLog log = new AttLog();
            	log.setAttLogId(rs.getInt("att_log_id"));
            	log.setUserPin(rs.getString("user_pin"));
            	log.setVerifyType(rs.getInt("verify_type"));
            	log.setVerifyTime(rs.getString("verify_time"));
            	log.setStatus(rs.getInt("status"));
            	log.setWorkCode(rs.getInt("work_code"));
            	log.setSensorNo(rs.getInt("sensor_no"));
            	log.setAttFlag(rs.getInt("att_flag"));
            	log.setDeviceSn(rs.getString("device_sn"));
            	log.setReserved1(rs.getInt("reserved1"));
            	log.setReserved2(rs.getInt("reserved2"));
            	log.setUserName(rs.getString("user_name"));
            	
            	list.add(log);
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
	 * Get counts of attendance records under the condition
	 * 
	 * @param cond
	 * Query condition
	 * @return
	 */
	public int fatchCount(String cond) {
		int count = 0;
		String sql = "select count(*) count_rec from att_log where 1=1 " + cond;
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
	 * Update attendance record
	 * @param entity
	 * Attendance record
	 * @throws DaoException
	 */
	public void update(AttLog entity) throws DaoException {
		String sql = "update att_log set user_pin=?, verify_type=?, verify_time=?, "
				+ " status=?, work_code=?, sensor_no=?, att_flag=?, device_sn=?, reserved1=?, reserved2=?,"
				+ " mask=?, temperature=? "
				+ " where user_pin=? and verify_type=? and verify_time=? and device_sn=? ";
		try {
			PreparedStatement pst = (PreparedStatement) getConnection()
					.prepareStatement(sql);
			int index = 1;
			pst.setString(index++, entity.getUserPin());
			pst.setInt(index++, entity.getVerifyType());
			pst.setString(index++, entity.getVerifyTime());
			pst.setInt(index++, entity.getStatus());
			pst.setInt(index++, entity.getWorkCode());
			pst.setInt(index++, entity.getSensorNo());
			pst.setInt(index++, entity.getAttFlag());
			pst.setString(index++, entity.getDeviceSn());
			pst.setInt(index++, entity.getReserved1());
			pst.setInt(index++, entity.getReserved2());
			pst.setInt(index++, entity.getMaskFlag());
			pst.setString(index++, entity.getTemperatureReading());
			/**update condition, make the record unique*/
			pst.setString(index++, entity.getUserPin());
			pst.setInt(index++, entity.getVerifyType());
			pst.setString(index++, entity.getVerifyTime());
			pst.setString(index++, entity.getDeviceSn());
			
			pst.addBatch();
			pst.executeBatch();
			pst.close();

		} catch (Exception e) {
			logger.error(e);
			throw new DaoException(e);
		}
	}


	public int truncate() throws DaoException {
		return 0;
	}

	/**
	 * Get attendance record list by condition
	 * @param cond
	 * Query condition
	 * @return
	 * Attendance record <code>AttLog</code> List
	 * @throws DaoException
	 */
	public List<AttLog> fatchList(String cond) throws DaoException {
		List<AttLog> list = new ArrayList<AttLog>();
		String sql = (new StringBuilder()).
		append("select att_log_id, user_pin, verify_type, verify_time, " +
				"status, work_code, sensor_no, att_flag, device_sn, reserved1, reserved2,mask,temperature " +
				"from att_log where 1=1 ").append(cond).toString();
		try {
			Statement st = getConnection().createStatement();
			ResultSet rs;
			rs = st.executeQuery(sql);
			while (rs.next()) {
				AttLog log = new AttLog();
            	log.setAttLogId(rs.getInt("att_log_id"));
            	log.setUserPin(rs.getString("user_pin"));
            	log.setVerifyType(rs.getInt("verify_type"));
            	log.setVerifyTime(rs.getString("verify_time"));
            	log.setStatus(rs.getInt("status"));
            	log.setWorkCode(rs.getInt("work_code"));
            	log.setSensorNo(rs.getInt("sensor_no"));
            	log.setAttFlag(rs.getInt("att_flag"));
            	log.setDeviceSn(rs.getString("device_sn"));
            	log.setReserved1(rs.getInt("reserved1"));
            	log.setReserved2(rs.getInt("reserved2"));
            	
            	log.setMaskFlag(rs.getInt("mask"));
            	log.setTemperatureReading(rs.getString("temperature"));
            	
            	list.add(log);
			}
			
			rs.close();
			st.close();
			return list;
		} catch (Exception e) {
			logger.error(e);
            throw new DaoException(e);
		}
	}

}
