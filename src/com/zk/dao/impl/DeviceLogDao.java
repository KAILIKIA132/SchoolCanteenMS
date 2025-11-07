package com.zk.dao.impl;

import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import java.sql.PreparedStatement;
import com.zk.dao.BaseDao;
import com.zk.dao.IBaseDao;
import com.zk.exception.DaoException;
import com.zk.pushsdk.po.DeviceLog;

/**
 * Device operation log access layer, operations: Add, Delete, Modify, Query.
 * @author seiya
 *
 */
public class DeviceLogDao extends BaseDao implements IBaseDao<DeviceLog> {

	public static final String TABLE_NAME = "device_log";
    private static Logger logger = Logger.getLogger(DeviceLogDao.class);
    
    /**
	 * Add new operation log to database
	 * @param entity
	 * Device operation log object
	 * @throws DaoException
	 */
	public void add(DeviceLog entity) throws DaoException {
		String sql = "insert into device_log (device_sn, operator, operator_type, " +
				"op_type, op_time, value1, value2, value3, reserved) values (?,?,?,?,?,?,?,?,?)";
		try {
			
			PreparedStatement pst = getConnection()
					.prepareStatement(sql);
			int index = 1;
			pst.setString(index++, entity.getDeviceSn());
			pst.setString(index++, entity.getOperator());
			pst.setInt(index++, entity.getOperatorType());
			pst.setInt(index++, entity.getOperateType());
			pst.setString(index++, entity.getOperateTime());
			//pst.setString(index++, entity.getTemperatureReading());
			pst.setString(index++, entity.getValue1());
			pst.setString(index++, entity.getValue2());
			pst.setString(index++, entity.getValue3());
			pst.setString(index++, entity.getReserved());

			pst.addBatch();
			pst.executeBatch();
			pst.close();
		} catch (Exception e) {
			logger.error(e);
			throw new DaoException(e);
		}
	}

	/**
	 * Delete Device operation log by ID
	 * @param id
	 * Device operation log ID
	 * @throws DaoException
	 */
	public void delete(int id) throws DaoException {
		try {
			String sql = (new StringBuilder())
			.append("delete from device_log where dev_log_id=").append(id).toString();
			Statement st = getConnection().createStatement();
			st.executeUpdate(sql);
			st.close();
		} catch (Exception e) {
			logger.error(e);
			throw new DaoException(e);
		}
	}

	/**
	 * Delete device operation log by condition
	 * @param cond
	 * Delete condition
	 * @throws DaoException
	 */
	public void delete(String cond) throws DaoException {
		try {
			String sql = (new StringBuilder())
			.append("delete from device_log where 1=1 ").append(cond).toString();
			Statement st = getConnection().createStatement();
			st.executeUpdate(sql);
			st.close();
		} catch (Exception e) {
			logger.error(e);
			throw new DaoException(e);
		}
	}

	/**
	 * Get device operation log by ID
	 * @param id
	 * Device operation log's ID
	 * @return
	 * Device operation log object <code>DeviceLog</code>
	 * @throws DaoException
	 */
	public DeviceLog fatch(int id) throws DaoException {
		try {
			String sql = (new StringBuilder()).
			append("select dev_log_id, device_sn, operator, operator_type, ")
			.append("op_type, op_time, value1, value2, value3, reserved from device_log where dev_log_id=").append(id).toString();
            Statement st = getConnection().createStatement();
            ResultSet rs = st.executeQuery(sql);
            
            DeviceLog log = null;
            if (rs.next()) {
            	log = new DeviceLog();
            	log.setDevLogId(rs.getInt("dev_log_id"));
            	log.setDeviceSn(rs.getString("device_sn"));
            	log.setOperator(rs.getString("operator"));
            	log.setOperatorType(rs.getInt("operator_type"));
            	log.setOperateType(rs.getInt("op_type"));
            	log.setOperateTime(rs.getString("op_time"));
            	//log.setTemperatureReading(rs.getString("temperature_reading"));
            	log.setValue1(rs.getString("value1"));
            	log.setValue2(rs.getString("value2"));
            	log.setValue3(rs.getString("value3"));
            	log.setReserved(rs.getString("reserved"));
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
	 * Get device operation log list by condition
	 * @param cond
	 * Query condition
	 * @return
	 * Device operation log <code>DeviceLog</code> list
	 * @throws DaoException
	 */
	public List<DeviceLog> fatchList(String cond) throws DaoException {
		List<DeviceLog> list = new ArrayList<DeviceLog>();
		try {
			String sql = (new StringBuilder()).
			append("select dev_log_id, device_sn, operator, operator_type, ")
			.append("op_type, op_time, value1, value2, value3,reserved from device_log where 1=1 ")
			.append(cond)
			.append(" limit 0, 100")
			.toString();
            Statement st = getConnection().createStatement();
            ResultSet rs = st.executeQuery(sql);
            while (rs.next()) {
            	DeviceLog log = new DeviceLog();
            	log.setDevLogId(rs.getInt("DEV_LOG_ID"));
            	log.setDeviceSn(rs.getString("device_sn"));
            	log.setOperator(rs.getString("operator"));
            	log.setOperatorType(rs.getInt("operator_type"));
            	log.setOperateType(rs.getInt("op_type"));
            	log.setOperateTime(rs.getString("op_time"));
            	//log.setTemperatureReading(rs.getString("temperature_reading"));
              	log.setValue1(rs.getString("value1"));
            	log.setValue2(rs.getString("value2"));
            	log.setValue3(rs.getString("value3"));
            	log.setReserved(rs.getString("reserved"));
            	
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
	 * Get device operation log by condition, use for interface display
	 * 
	 * @param cond
	 * Query condition
	 * @param startRec
	 * Begin record position
	 * @param pageSize
	 * Query size
	 * @return
	 * Device operation log list
	 * @throws DaoException
	 */
	public List<DeviceLog> fatchList(String cond, int startRec, int pageSize) throws DaoException {
		List<DeviceLog> list = new ArrayList<DeviceLog>();
		try {
			String sql = (new StringBuilder()).
			append("select dev_log_id, device_sn, operator, operator_type, ")
			.append("op_type, op_time, value1, value2, value3,reserved from device_log where 1=1 ")
			.append(cond).append(" order by dev_log_id desc ")
			.append(" limit ").append(startRec).append(", ").append(pageSize)
			.toString();
            Statement st = getConnection().createStatement();
            ResultSet rs = st.executeQuery(sql);
            while (rs.next()) {
            	DeviceLog log = new DeviceLog();
            	log.setDevLogId(rs.getInt("DEV_LOG_ID"));
            	log.setDeviceSn(rs.getString("device_sn"));
            	log.setOperator(rs.getString("operator"));
            	log.setOperatorType(rs.getInt("operator_type"));
            	log.setOperateType(rs.getInt("op_type"));
            	log.setOperateTime(rs.getString("op_time"));
            	//log.setTemperatureReading(rs.getString("temperature_reading"));
              	log.setValue1(rs.getString("value1"));
            	log.setValue2(rs.getString("value2"));
            	log.setValue3(rs.getString("value3"));
            	log.setReserved(rs.getString("reserved"));
            	
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
	 * Get counts of device operation log by condition
	 * 
	 * @param cond
	 * Query condition
	 * @return
	 */
	public int fatchCount(String cond) {
		int count = 0;
		String sql = "select count(*) count_rec from device_log where 1=1 " + cond;
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

	public void update(DeviceLog entity) throws DaoException {
		// don't need to impl

	}

	public int truncate() throws DaoException {
		return 0;
	}

}
