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
import com.zk.pushsdk.po.Message;

/**
 * SMS databse access layer, operations: Add, Delete, Modify, Query.
 * @author seiya
 *
 */
public class MessageDao extends BaseDao implements IBaseDao<Message>{
	
	public static final String TABLE_NAME = "message";
    private static Logger logger = Logger.getLogger(MessageDao.class);

    /**
 	 * Add new SMS to database
 	 * @param entity
 	 * SMS object
 	 * @throws DaoException
 	 */
	public void add(Message entity) throws DaoException {
		String sql = "insert into message (device_sn, sms_type, " +
				"start_time, valid_minutes, sms_content) " +
				"values (?,?,?,?,?)";
		try {
			PreparedStatement pst = getConnection().prepareStatement(sql);
			int index = 1;
			pst.setString(index++, entity.getDeviceSn());
			pst.setInt(index++, entity.getSmsType());
			pst.setString(index++, entity.getStartTime());
			pst.setInt(index++, entity.getValidMinutes());
			pst.setString(index++, entity.getSmsContent());
			
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
	 * Delete SMS by ID
	 * @param id
	 * SMS's ID
	 * @throws DaoException
	 */
	public void delete(int id) throws DaoException {
		try {
			String sql = (new StringBuilder())
			.append("delete from message where id=")
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
	 * Delete SMS by condition
	 * @param cond
	 * Delete condition
	 * @throws DaoException
	 */
	public void delete(String cond) throws DaoException {
		try {
			String sql = (new StringBuilder())
			.append("delete from message where 1=1 ")
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
	 * Get SMS by ID
	 * @param id
	 * SMS's ID
	 * @return
	 * SMS object <code>Message</code>
	 * @throws DaoException
	 */
	public Message fatch(int id) throws DaoException {
		try {
			String sql = (new StringBuilder()).
			append("select id, device_sn, sms_type, start_time, ")
			.append(" valid_minutes, sms_content from message where ")
			.append("id=").append(id).toString();
            Statement st = getConnection().createStatement();
            ResultSet rs = st.executeQuery(sql);
            Message msg = null;
            if (rs.next()) {
            	msg = new Message();
            	msg.setId(rs.getInt("id"));
            	msg.setDeviceSn(rs.getString("device_sn"));
            	msg.setSmsType(rs.getInt("sms_type"));
            	msg.setStartTime(rs.getString("start_time"));
            	msg.setValidMinutes(rs.getInt("valid_minutes"));
            	msg.setSmsContent(rs.getString("sms_content"));
            }
            
            rs.close();
            st.close();
            return msg;
		} catch (Exception e) {
			 logger.error(e);
	         throw new DaoException(e);
		}
	}

	/**
	 * Get SMS list by condition
	 * @param cond
	 * Query condition
	 * @return
	 * SMS <code>Message</code> list
	 * @throws DaoException
	 */
	public List<Message> fatchList(String cond) throws DaoException {
		List<Message> list = new ArrayList<Message>();
		try {
			String sql = (new StringBuilder()).
			append("select id, device_sn, sms_type, start_time, ")
			.append(" valid_minutes, sms_content from message where ")
			.append("1=1 ").append(cond).toString();
            Statement st = getConnection().createStatement();
            ResultSet rs = st.executeQuery(sql);
            while(rs.next()) {
            	Message msg = new Message();
            	msg.setId(rs.getInt("id"));
            	msg.setDeviceSn(rs.getString("device_sn"));
            	msg.setSmsType(rs.getInt("sms_type"));
            	msg.setStartTime(rs.getString("start_time"));
            	msg.setValidMinutes(rs.getInt("valid_minutes"));
            	msg.setSmsContent(rs.getString("sms_content"));
            	
            	list.add(msg);
            }
            
            rs.close();
            st.close();
		} catch (Exception e) {
			 logger.error(e);
	         throw new DaoException(e);
		}
		return list;
	}
	
	/**
	 * Get SMS list by condition, use for interface display
	 * 
	 * @param cond
	 * Query condition
	 * @param startRec
	 * Begin record position
	 * @param pageSize
	 * Query size
	 * @return
	 * SMS list
	 * @throws DaoException
	 */
	public List<Message> fatchList(String cond, int startRec, int pageSize) throws DaoException {
		List<Message> list = new ArrayList<Message>();
		try {
			String sql = (new StringBuilder()).
			append("select id, device_sn, sms_type, start_time, ")
			.append(" valid_minutes, sms_content from message where ")
			.append("1=1 ").append(cond).append(" limit ").append(startRec).append(", ").append(pageSize).toString();
            Statement st = getConnection().createStatement();
            ResultSet rs = st.executeQuery(sql);
            while(rs.next()) {
            	Message msg = new Message();
            	msg.setId(rs.getInt("id"));
            	msg.setDeviceSn(rs.getString("device_sn"));
            	msg.setSmsType(rs.getInt("sms_type"));
            	msg.setStartTime(rs.getString("start_time"));
            	msg.setValidMinutes(rs.getInt("valid_minutes"));
            	msg.setSmsContent(rs.getString("sms_content"));
            	
            	list.add(msg);
            }
            
            rs.close();
            st.close();
		} catch (Exception e) {
			 logger.error(e);
	         throw new DaoException(e);
		}
		return list;
	}

	/**
	 * Get SMS counts by condition
	 * 
	 * @param cond
	 * Query condition
	 * @return
	 */
	public int fatchCount(String cond) {
		int count = 0;
		String sql = "select count(*) count_rec from message where 1=1 " + cond;
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
	 * Update SMS
	 * @param entity
	 * SMS
	 * @throws DaoException
	 */
	public void update(Message entity) throws DaoException {
		String sql = (new StringBuffer())
		.append("update message set device_sn=?, sms_type=?, ")
		.append("start_time=?, valid_minutes=?, sms_content=? ")
		.append(" where id=?").toString();
		try {
			PreparedStatement pst = getConnection().prepareStatement(sql);
			int index = 1;
			pst.setString(index++, entity.getDeviceSn());
			pst.setInt(index++, entity.getSmsType());
			pst.setString(index++, entity.getStartTime());
			pst.setInt(index++, entity.getValidMinutes());
			pst.setString(index++, entity.getSmsContent());
			pst.setInt(index++, entity.getId());
			
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
