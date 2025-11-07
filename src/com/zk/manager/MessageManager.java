package com.zk.manager;

import java.util.ArrayList;
import java.util.List;

import com.zk.dao.impl.MessageDao;
import com.zk.exception.DaoException;
import com.zk.pushsdk.po.Message;

/**
 * SMS management class, can be used for database operation
 * @author seiya
 *
 */
public class MessageManager {

	/**
	 * Get the SMS list according to condition
	 * @param type
	 * SMS type
	 * @param startRec
	 * start record position
	 * @param pageSize
	 * maximum number of query data
	 * @return
	 */
	public List<Message> getSmsList(int type, int startRec, int pageSize) {
		MessageDao dao = new MessageDao();
		try {
			StringBuilder sb = new StringBuilder();
			if (type >= 0) {
				sb.append(" and sms_type=").append(type);
			}
			List<Message> list = dao.fatchList(sb.toString(), startRec, pageSize);
			
			return list;
		} catch (DaoException e) {
		} finally {
			dao.close();
		}
		return new ArrayList<Message>();
	}
	
	/**
	 * Get the total number of SMS according to condition
	 * @param type
	 * SMS type
	 * @return
	 */
	public int getMessageCount(int type) {
		MessageDao dao = new MessageDao();
		try {
			StringBuilder sb = new StringBuilder();
			if (type >= 0) {
				sb.append(" and sms_type=").append(type);
			}
			return dao.fatchCount(sb.toString());
		} catch (DaoException e) {
			
		} finally {
			dao.close();
		}
		return 0;
	}
	
	/**
	 * Add the SMS to database
	 * @param message
	 * SMS object
	 * @return
	 */
	public int addSms(Message message) {
		if (null == message) {
			return -1;
		}
		MessageDao dao = new MessageDao();
		try {
			dao.add(message);
			
			dao.commit();
		} catch (DaoException e) {
			dao.rollback();
		} finally {
			dao.close();
		}
		return 0;
	}
	
	/**
	 * Delete the SMS according to ID
	 * @param ids
	 * SMS ID
	 * @return
	 */
	public int deleteSms(String[] ids) {
		if (null == ids || ids.length <= 0) {
			return -1;
		}
		MessageDao dao = new MessageDao();
		try {
			StringBuilder sb = new StringBuilder();
			/**combine the condition**/
			sb.append(" and id in(");
			for (String id : ids) {
				sb.append(id);
				sb.append(",");
			}
			sb.deleteCharAt(sb.length() - 1);
			sb.append(")");
			
			dao.delete(sb.toString());
			
			dao.commit();
		} catch (DaoException e) {
			dao.rollback();
		} finally {
			dao.close();
		}
		
		return 0;
	}
	
}
