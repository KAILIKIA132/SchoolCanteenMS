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
import com.zk.pushsdk.po.DeviceCommand;

/**
 * Device command database access layer,operations: Add, Delete, Modify, Query.
 * @author seiya
 *
 */
public class DeviceCommandDao extends BaseDao implements IBaseDao<DeviceCommand>{
	public static final String TABLE_NAME = "device_command";
	public static final String TABLE_NAME1 = "BIODATA";
    private static Logger logger = Logger.getLogger(DeviceCommandDao.class);
    
    /**
 	 * Add new device command to database
 	 * @param entity
 	 * Device command object
 	 * @throws DaoException
 	 */
   
	public void add(DeviceCommand entity) throws DaoException {
		String sql = (new StringBuffer())
		.append("insert into device_command (device_sn, ")
		.append("cmd_content, cmd_commit_times, cmd_trans_times, ")
		.append("cmd_over_time, cmd_return) values ")
		.append("(?,?,?,?,?,?)").toString();
		try {
			PreparedStatement pst = getConnection().prepareStatement(sql);
			pst.setString(1, entity.getDeviceSn());
			pst.setString(2, entity.getCmdContent());
			pst.setString(3, entity.getCmdCommitTime());
			pst.setString(4, entity.getCmdTransTime());
			pst.setString(5, entity.getCmdOverTime());
			pst.setString(6, entity.getCmdReturn());
			
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
	 * Delete device command by ID 
	 * @param id
	 * Device command ID
	 * @throws DaoException
	 */
	public void delete(int id) throws DaoException {
		try {
			String sql = (new StringBuilder())
			.append("delete from device_command where dev_cmd_id=").append(id).toString();
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
	 * Delete device command by condition
	 * @param cond
	 * Delete comdition
	 * @throws DaoException
	 */
	public void delete(String cond) throws DaoException {
		try {
			String sql = (new StringBuilder())
			.append("delete from device_command where 1=1 ").append(cond).toString();
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
	 * Get device command by ID
	 * @param id
	 * Device command ID
	 * @return
	 * Device command object <code>DeviceCommand</code>
	 * @throws DaoException
	 */
	public DeviceCommand fatch(int id) throws DaoException {
		try {
			String sql = (new StringBuilder())
			.append("select dev_cmd_id, device_sn, cmd_content, cmd_commit_times, ")
			.append("cmd_trans_times, cmd_over_time, cmd_return, cmd_return_info ")
			.append(" from device_command where dev_cmd_id=")
			.append(id).toString();
			Statement st = getConnection().createStatement();
			ResultSet rs = st.executeQuery(sql);
			DeviceCommand cmd = null;
			if (rs.next()) {
				cmd = new DeviceCommand();
				cmd.setDevCmdId(rs.getInt("dev_cmd_id"));
				cmd.setDeviceSn(rs.getString("device_sn"));
				cmd.setCmdContent(rs.getString("cmd_content"));
				cmd.setCmdCommitTime(rs.getString("cmd_commit_times"));
				cmd.setCmdTransTime(rs.getString("cmd_trans_times"));
				cmd.setCmdOverTime(rs.getString("cmd_over_time"));
				cmd.setCmdReturn(rs.getString("cmd_return"));
				cmd.setCmdReturnInfo(rs.getString("cmd_return_info"));
			}
			
			rs.close();
			st.close();
			return cmd;
		} catch (Exception e) {
			logger.error(e);
			throw new DaoException(e);
		}
	}

	/**
	 * Get device command list by condition
	 * @param cond
	 * Query condition
	 * @return
	 * Device command <code>DeviceCommand</code> List
	 * @throws DaoException
	 */
	public List<DeviceCommand> fatchList(String cond) throws DaoException {
		List<DeviceCommand> list = new ArrayList<DeviceCommand>();
		try {
			String sql = (new StringBuilder())
			.append("select dev_cmd_id, device_sn, cmd_content, cmd_commit_times, ")
			.append("cmd_trans_times, cmd_over_time, cmd_return ")
			.append(" from device_command where 1=1 ")
			.append(cond).append(" order by dev_cmd_id ASC limit 0, 100 ").toString();
			Statement st = getConnection().createStatement();
			ResultSet rs = st.executeQuery(sql);
			while (rs.next()) {
				DeviceCommand cmd = new DeviceCommand();
				cmd.setDevCmdId(rs.getInt("dev_cmd_id"));
				cmd.setDeviceSn(rs.getString("device_sn"));
				cmd.setCmdContent(rs.getString("cmd_content"));
				cmd.setCmdCommitTime(rs.getString("cmd_commit_times"));
				cmd.setCmdTransTime(rs.getString("cmd_trans_times"));
				cmd.setCmdOverTime(rs.getString("cmd_over_time"));
				cmd.setCmdReturn(rs.getString("cmd_return"));
				
				list.add(cmd);
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
	 * Get device command by condition use for interface display, in descending order
	 * 
	 * @param cond
	 * Query condition
	 * @param startRec
	 * Begin record position
	 * @param pageSize
	 * Query size
	 * @return
	 * Device command List
	 * @throws DaoException
	 */
	public List<DeviceCommand> fatchListForPage(String cond, int startRec, int pageSize) throws DaoException {
		List<DeviceCommand> list = new ArrayList<DeviceCommand>();
		try {
			String sql = (new StringBuilder())
			.append("select dev_cmd_id, device_sn, cmd_content, cmd_commit_times, ")
			.append("cmd_trans_times, cmd_over_time, cmd_return, cmd_return_info ")
			.append(" from device_command where 1=1 ")
			.append(cond).append(" order by dev_cmd_id DESC limit ").append(startRec).append(",").append(pageSize).append(" ").toString();
			Statement st = getConnection().createStatement();
			ResultSet rs = st.executeQuery(sql);
			while (rs.next()) {
				DeviceCommand cmd = new DeviceCommand();
				cmd.setDevCmdId(rs.getInt("dev_cmd_id"));
				cmd.setDeviceSn(rs.getString("device_sn"));
				cmd.setCmdContent(rs.getString("cmd_content"));
				cmd.setCmdCommitTime(rs.getString("cmd_commit_times"));
				cmd.setCmdTransTime(rs.getString("cmd_trans_times"));
				cmd.setCmdOverTime(rs.getString("cmd_over_time"));
				cmd.setCmdReturn(rs.getString("cmd_return"));
				cmd.setCmdReturnInfo(rs.getString("cmd_return_info"));
				
				list.add(cmd);
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
	 * Update device command
	 * @param entity
	 * Device command
	 * @throws DaoException
	 */
	public void update(DeviceCommand entity) throws DaoException {
		String sql = (new StringBuilder()).
		append("update device_command set device_sn=?, cmd_content=?, cmd_commit_times=?, " +
				"cmd_trans_times=?, cmd_over_time=?, cmd_return=?, cmd_return_info=? " +
				"where dev_cmd_id=? ").toString();
		try {
			PreparedStatement pst =  getConnection().prepareStatement(sql);
			pst.setString(1, entity.getDeviceSn());
			pst.setString(2, entity.getCmdContent());
			pst.setString(3, entity.getCmdCommitTime());
			pst.setString(4, entity.getCmdTransTime());
			pst.setString(5, entity.getCmdOverTime());
			pst.setString(6, entity.getCmdReturn());
			pst.setString(7, entity.getCmdReturnInfo());
			pst.setInt(8, entity.getDevCmdId());
			
			
			
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
	 * Get counts of device command by condition
	 * 
	 * @param cond
	 * Query condition
	 * @return
	 */
	public int fatchCount(String cond) {
		int count = 0;
		String sql = "select count(*) count_rec from device_command where 1=1 " + cond;
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
	
	public int truncate() throws DaoException {
		// TODO Auto-generated method stub
		return 0;
	}

}
