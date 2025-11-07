package com.zk.dao.impl;

import java.sql.BatchUpdateException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.zk.dao.BaseDao;
import com.zk.dao.IBaseDao;
import com.zk.exception.DaoException;
import com.zk.pushsdk.po.UserInfo;

/**
 * User basic info database access layer, operations: Add, Delete, Modify, Query.
 * @author seiya
 *
 */
public class UserInfoDao extends BaseDao implements IBaseDao<UserInfo> {

	public static final String TABLE_NAME = "user_info";
	private static Logger logger = Logger.getLogger(UserInfoDao.class);
	public UserInfoDao() {

	}

	/**
	 * Get user basic info list by condition
	 * @param cond
	 * Query condition
	 * @return
	 * User basic info <code>UserInfo</code> list
	 * @throws DaoException
	 */
	public List<UserInfo> fatchList(String cond) {
		List<UserInfo> list = new ArrayList<UserInfo>();
		String sql = (new StringBuilder()).
		append("select user_id, user_pin, privilege, name, password, " +
				"face_group_id, acc_group_id, dept_id, is_group_tz, " +
				"verify_type, main_card, vice_card, expires, device_sn, tz, photo_id_name, photo_id_size, photo_id_content,meet_code, category " +
				"from user_info where 1=1 ").append(cond).toString();
		try {
			Statement st = getConnection().createStatement();
			ResultSet rs;
			rs = st.executeQuery(sql);
			while (rs.next()) {
				UserInfo info = new UserInfo();
            	info.setUserId(rs.getInt("user_id"));
            	info.setUserPin(rs.getString("user_pin"));
            	info.setPrivilege(rs.getInt("privilege"));
            	info.setName(rs.getString("name"));
            	info.setPassword(rs.getString("password"));
            	info.setFaceGroupId(rs.getInt("face_group_id"));
            	//info.setPlamGroupId(rs.getInt("plam_group_id"));
            	info.setAccGroupId(rs.getInt("acc_group_id"));
            	info.setDeptId(rs.getInt("dept_id"));
            	info.setIsGroupTz(rs.getInt("is_group_tz"));
            	info.setVerifyType(rs.getInt("verify_type"));
            	info.setMainCard(rs.getString("main_card"));
            	info.setViceCard(rs.getString("vice_card"));
            	info.setExpires(rs.getInt("expires"));
            	info.setDeviceSn(rs.getString("device_sn"));
            	info.setTz(rs.getString("tz"));
            	info.setPhotoIdName(rs.getString("photo_id_name"));
            	info.setPhotoIdSize(rs.getInt("photo_id_size"));
            	info.setPhotoIdContent(rs.getString("photo_id_content"));
            	info.setMeetCode(rs.getString("meet_code"));
            	info.setCategory(rs.getInt("category"));
            	
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
	 * Get user basic info by condition, use for interface display
	 * 
	 * @param cond
	 * Query condition
	 * @param startRec
	 * Begin record position
	 * @param pageSize
	 * Query size
	 * @return
	 * User basic info list
	 * @throws DaoException
	 */
	public List<UserInfo> fatchListForPage(String cond, int startRec, int pageSize) {
		List<UserInfo> list = new ArrayList<UserInfo>();
		String sql = (new StringBuilder()).
		append("select user_id, user_pin, privilege, name, password, " +
				"face_group_id, acc_group_id, dept_id, is_group_tz, " +
				"verify_type, main_card, vice_card, expires, device_sn, tz, photo_id_name, photo_id_size, photo_id_content, meet_code, category, " +
				"(select count(*) from pers_bio_template where BIO_TYPE=1 and DEVICE_SN=a.DEVICE_SN and USER_ID=a.USER_ID) user_fp_count, " +
				"(select count(*) from pers_bio_template where BIO_TYPE=9 OR BIO_TYPE=2 and DEVICE_SN=a.DEVICE_SN and USER_ID=a.USER_ID) user_face_count, " +
				"(select count(*) from pers_bio_template where BIO_TYPE=8 and DEVICE_SN=a.DEVICE_SN and USER_ID=a.USER_ID) user_palm_count " +
				"from user_info a where 1=1 ").append(cond).append(" limit ").append(startRec).append(", ").append(pageSize).toString();
		try {
			Statement st = getConnection().createStatement();
			ResultSet rs;
			System.out.println(sql);
			rs = st.executeQuery(sql);
			while (rs.next()) {
				UserInfo info = new UserInfo();
            	info.setUserId(rs.getInt("user_id"));
            	info.setUserPin(rs.getString("user_pin"));
            	info.setPrivilege(rs.getInt("privilege"));
            	info.setName(rs.getString("name"));
            	info.setPassword(rs.getString("password"));
            	info.setFaceGroupId(rs.getInt("face_group_id"));
            	//info.setPlamGroupId(rs.getInt("plam_group_id"));
            	info.setAccGroupId(rs.getInt("acc_group_id"));
            	info.setDeptId(rs.getInt("dept_id"));
            	info.setIsGroupTz(rs.getInt("is_group_tz"));
            	info.setVerifyType(rs.getInt("verify_type"));
            	info.setMainCard(rs.getString("main_card"));
            	info.setViceCard(rs.getString("vice_card"));
            	info.setExpires(rs.getInt("expires"));
            	info.setDeviceSn(rs.getString("device_sn"));
            	info.setTz(rs.getString("tz"));
            	info.setPhotoIdName(rs.getString("photo_id_name"));
            	info.setPhotoIdSize(rs.getInt("photo_id_size"));
            	info.setPhotoIdContent(rs.getString("photo_id_content"));
            	info.setUserFpCount(rs.getInt("user_fp_count"));
            	info.setUserFaceCount(rs.getInt("user_face_count"));
            	info.setUserPalmCount(rs.getInt("user_palm_count"));
            	info.setMeetCode(rs.getString("meet_code"));
            	info.setCategory(rs.getInt("category"));
            	
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
	 * Get counts of user basic info by condition
	 * 
	 * @param cond
	 * Query condition
	 * @return
	 */
	public int fatchCount(String cond) {
		int count = 0;
		String sql = "select count(*) count_rec from user_info where 1=1 " + cond;
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
	 * Add new user basic info to database
	 * @param entity
	 * user basic info object
	 * @throws DaoException
	 */
	public void add(UserInfo entity) throws DaoException {
		StringBuilder sb = new StringBuilder();
		sb.append("insert into user_info ");
		sb.append("(user_pin");
		sb.append(", privilege");
		sb.append(", name");
		sb.append(", password");
		sb.append(", face_group_id");
		//sb.append(", plam_group_id");
		sb.append(", acc_group_id");
		sb.append(", dept_id");
		sb.append(", is_group_tz");
		sb.append(", verify_type");
		sb.append(", main_card");
		sb.append(", vice_card");
		sb.append(", expires");
		sb.append(", device_sn");
		sb.append(", tz ");
		sb.append(", photo_id_content");
		sb.append(", photo_id_name");
		sb.append(", photo_id_size");
		sb.append(", meet_code");
		sb.append(", category)");
		sb.append(" values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
		
		try {
			PreparedStatement pst = getConnection().prepareStatement(sb.toString());
			int index = 1;
			pst.setString(index++, entity.getUserPin());
			pst.setInt(index++, entity.getPrivilege());
			pst.setString(index++, entity.getName());
			pst.setString(index++, entity.getPassword());
			pst.setInt(index++, entity.getFaceGroupId());
			//pst.setInt(index++, entity.getPlamGroupId());
			pst.setInt(index++, entity.getAccGroupId());
			pst.setInt(index++, entity.getDeptId());
			pst.setInt(index++, entity.getIsGroupTz());
			pst.setInt(index++, entity.getVerifyType());
			pst.setString(index++, entity.getMainCard());
			pst.setString(index++, entity.getViceCard());
			pst.setInt(index++, entity.getExpires());
			pst.setString(index++, entity.getDeviceSn());
			pst.setString(index++, entity.getTz());
			pst.setString(index++, entity.getPhotoIdContent());
			pst.setString(index++, entity.getPhotoIdName());
			pst.setInt(index++, entity.getPhotoIdSize());
			pst.setString(index++, entity.getMeetCode());
			pst.setInt(index++, entity.getCategory());

			pst.addBatch();
			pst.executeBatch();
			pst.close();
		} catch (Exception e) {
			logger.error(e);
			throw new DaoException(e);
		}

	}
	
	/**
	 * Add or update new user basic info
	 * @param entity
	 * User basic info object
	 * @return
	 */
	public int addOrUpdate(UserInfo entity) {
		try {
			add(entity);
		} catch (DaoException e) {
			try {
				if (e.getCause() instanceof BatchUpdateException) {
					BatchUpdateException be = (BatchUpdateException)e.getCause();
					if (1062 == be.getErrorCode()) {
						updateUserInfoByPin(entity);
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
	 * Update user basic info by user pin and device serial number
	 * @param entity
	 * User basic info
	 * @return
	 * @throws DaoException
	 */
	public int updateUserInfoByPin(UserInfo entity) throws DaoException {
		String sql = "update user_info set user_pin=?, privilege=?, name=?, "
				+ "password=?, face_group_id=?, acc_group_id=?, dept_id=?, "
				+ "is_group_tz=?, verify_type=?, main_card=?, vice_card=?, "
				+ "expires=?, device_sn=?, tz=?, meet_code=?, category=? where user_pin=? and device_sn=? ";
		try {
			PreparedStatement pst = getConnection().prepareStatement(sql);
			int index = 1;
			pst.setString(index++, entity.getUserPin());
			pst.setInt(index++, entity.getPrivilege());
			pst.setString(index++, entity.getName());
			pst.setString(index++, entity.getPassword());
			pst.setInt(index++, entity.getFaceGroupId());
			//pst.setInt(index++, entity.getPlamGroupId());
			pst.setInt(index++, entity.getAccGroupId());
			pst.setInt(index++, entity.getDeptId());
			pst.setInt(index++, entity.getIsGroupTz());
			pst.setInt(index++, entity.getVerifyType());
			pst.setString(index++, entity.getMainCard());
			pst.setString(index++, entity.getViceCard());
			pst.setInt(index++, entity.getExpires());
			pst.setString(index++, entity.getDeviceSn());
			pst.setString(index++, entity.getTz());
			pst.setString(index++, entity.getMeetCode());
			pst.setInt(index++, entity.getCategory());
			pst.setString(index++, entity.getUserPin());
			pst.setString(index++, entity.getDeviceSn());

			pst.addBatch();
			pst.executeBatch();
			pst.close();
		} catch (SQLException e) {
			logger.error(e);
			throw new DaoException(e);
		}
		return 0;
	}
	
	/**
	 * Update user basic info by ID
	 * @param entity
	 * User basic info
	 * @throws DaoException
	 */	
	public void update(UserInfo entity) throws DaoException {
		String sql = "update user_info set user_pin=?, privilege=?, name=?, "
				+ "password=?, face_group_id=?, acc_group_id=?, dept_id=?, "
				+ "is_group_tz=?, verify_type=?, main_card=?, vice_card=?, "
				+ "expires=?, device_sn=?, tz=?, photo_id_name=?, photo_id_size=?, photo_id_content=?, meet_code=?, category=? where user_id=?";
		try {
			PreparedStatement pst =  getConnection()
					.prepareStatement(sql);
			int index = 1;
			pst.setString(index++, entity.getUserPin());
			pst.setInt(index++, entity.getPrivilege());
			pst.setString(index++, entity.getName());
			pst.setString(index++, entity.getPassword());
			pst.setInt(index++, entity.getFaceGroupId());
			//pst.setInt(index++, entity.getPlamGroupId());
			pst.setInt(index++, entity.getAccGroupId());
			pst.setInt(index++, entity.getDeptId());
			pst.setInt(index++, entity.getIsGroupTz());
			pst.setInt(index++, entity.getVerifyType());
			pst.setString(index++, entity.getMainCard());
			pst.setString(index++, entity.getViceCard());
			pst.setInt(index++, entity.getExpires());
			pst.setString(index++, entity.getDeviceSn());
			pst.setString(index++, entity.getTz());
			pst.setString(index++, entity.getPhotoIdName());
			pst.setInt(index++, entity.getPhotoIdSize());
			pst.setString(index++, entity.getPhotoIdContent());
			pst.setString(index++, entity.getMeetCode());
			pst.setInt(index++, entity.getCategory());
			pst.setInt(index++, entity.getUserId());

			pst.addBatch();
			pst.executeBatch();
			pst.close();
		} catch (SQLException e) {
			logger.error(e);
			throw new DaoException(e);
		}
	}

	/**
	 * Delete user basic info by ID
	 * @param id
	 * User basic info's ID
	 * @throws DaoException
	 */
	public void delete(int id) throws DaoException {
		try {
			String sql = (new StringBuilder())
			.append("delete from user_info where user_id=")
			.append(id).toString();
			Statement st = getConnection().createStatement();
			st.executeUpdate(sql);
			st.close();
		} catch (Exception e) {
			logger.error(e);
			throw new DaoException(e);
		}
	}

	/**
	 * Delete user basic info by condition
	 * @param cond
	 * Delete condition
	 * @throws DaoException
	 */
	public void delete(String cond) throws DaoException {
		try {
			String sql = (new StringBuilder())
			.append("delete from user_info where 1=1 ")
			.append(cond).toString();
			Statement st = getConnection().createStatement();
			st.executeUpdate(sql);
			st.close();
		} catch (Exception e) {
			logger.error(e);
			throw new DaoException(e);
		}
	}

	/**
	 * Get user basic info by ID
	 * @param id
	 * User basic info's ID
	 * @return
	 * User basic info object <code>UserInfo</code>
	 * @throws DaoException
	 */
	public UserInfo fatch(int id) throws DaoException {
		try {
			String sql = (new StringBuilder()).
			append("select user_id, user_pin, privilege, name, password, " +
					"face_group_id, acc_group_id, dept_id, is_group_tz, " +
					"verify_type, main_card, vice_card, expires, device_sn, tz, photo_id_name, photo_id_size, photo_id_content, meet_code, category " +
					"from user_info where user_id=").append(id).toString();
            Statement st = getConnection().createStatement();
            ResultSet rs = st.executeQuery(sql);
            UserInfo info = null;
            if (rs.next()) {
            	info = new UserInfo();
            	info.setUserId(rs.getInt("user_id"));
            	info.setUserPin(rs.getString("user_pin"));
            	info.setPrivilege(rs.getInt("privilege"));
            	info.setName(rs.getString("name"));
            	info.setPassword(rs.getString("password"));
            	info.setFaceGroupId(rs.getInt("face_group_id"));
            	//info.setPlamGroupId(rs.getInt("plam_group_id"));
            	info.setAccGroupId(rs.getInt("acc_group_id"));
            	info.setDeptId(rs.getInt("dept_id"));
            	info.setIsGroupTz(rs.getInt("is_group_tz"));
            	info.setVerifyType(rs.getInt("verify_type"));
            	info.setMainCard(rs.getString("main_card"));
            	info.setViceCard(rs.getString("vice_card"));
            	info.setExpires(rs.getInt("expires"));
            	info.setDeviceSn(rs.getString("device_sn"));
            	info.setTz(rs.getString("tz"));
            	info.setPhotoIdName(rs.getString("photo_id_name"));
            	info.setPhotoIdSize(rs.getInt("photo_id_size"));
            	info.setPhotoIdContent(rs.getString("photo_id_content"));
            	info.setMeetCode(rs.getString("meet_code"));
            	info.setCategory(rs.getInt("category"));
            }
            
            rs.close();
            st.close();
            return info;
		} catch (Exception e) {
			 logger.error(e);
	         throw new DaoException(e);
		}
	}

	public int truncate() throws DaoException {
		// TODO Auto-generated method stub
		return 0;
	}
	
	/**
	 * Update user's photo
	 * @param entity
	 * User basic info
	 * @return
	 */
	public int updateUserPic(UserInfo entity) {
		StringBuilder sb = new StringBuilder();
		sb.append(" update user_info set ");
		sb.append(" photo_id_name=? ");
		sb.append(", photo_id_size=? ");
		sb.append(", photo_id_content=? ");
		sb.append(" where user_id=? ");
		
		try {
			PreparedStatement pst = getConnection().prepareStatement(sb.toString());
			
			int index = 1;
			pst.setString(index++, entity.getPhotoIdName());
			pst.setInt(index++, entity.getPhotoIdSize());
			pst.setString(index++, entity.getPhotoIdContent());
			pst.setInt(index++, entity.getUserId());
			
			pst.addBatch();
			pst.executeBatch();
			pst.close();
		} catch (Exception e) {
			rollback();
			logger.error(e);
			throw new DaoException(e);
		}
		return 0;
	}
	
	/**
	 * Update user's meet
	 * @param meetCode
	 * @param cond
	 * @return
	 */
	public int updateMeet(String meetCode,String cond) {
		StringBuilder sb = new StringBuilder();
		sb.append(" update user_info set ");
		sb.append(" meet_code=? ");
		sb.append(" where 1=1 ");
		sb.append(cond);
		
		try {
			PreparedStatement pst = getConnection().prepareStatement(sb.toString());
			
			int index = 1;
			pst.setString(index++, meetCode);
			
			pst.addBatch();
			pst.executeBatch();
			pst.close();
		} catch (Exception e) {
			rollback();
			logger.error(e);
			throw new DaoException(e);
		}
		return 0;
	}
	
	/**
	 * Get User basic info by user pin and serial number
	 * @param userPin
	 * User pin
	 * @param deviceSn
	 * Serial number
	 * @return
	 * User basic info object <code>UserInfo</code>
	 */
	public UserInfo fatch(String userPin, String deviceSn) {
		 UserInfo info = null;
		try {
			String sql = (new StringBuilder()).
			append("select user_id, user_pin, privilege, name, password, " +
					"face_group_id,acc_group_id, dept_id, is_group_tz, " +
					"verify_type, main_card, vice_card, expires, device_sn, tz, photo_id_name, photo_id_size, photo_id_content,meet_code, category  " +
					"from user_info where user_pin='").append(userPin)
					.append("' ").append(" and device_sn='").append(deviceSn).append("'").toString();
            Statement st = getConnection().createStatement();
            ResultSet rs = st.executeQuery(sql);
           
            if (rs.next()) {
            	info = new UserInfo();
            	info.setUserId(rs.getInt("user_id"));
            	info.setUserPin(rs.getString("user_pin"));
            	info.setPrivilege(rs.getInt("privilege"));
            	info.setName(rs.getString("name"));
            	info.setPassword(rs.getString("password"));
            	info.setFaceGroupId(rs.getInt("face_group_id"));
            	//info.setPlamGroupId(rs.getInt("plam_group_id"));
            	info.setAccGroupId(rs.getInt("acc_group_id"));
            	info.setDeptId(rs.getInt("dept_id"));
            	info.setIsGroupTz(rs.getInt("is_group_tz"));
            	info.setVerifyType(rs.getInt("verify_type"));
            	info.setMainCard(rs.getString("main_card"));
            	info.setViceCard(rs.getString("vice_card"));
            	info.setExpires(rs.getInt("expires"));
            	info.setDeviceSn(rs.getString("device_sn"));
            	info.setTz(rs.getString("tz"));
            	info.setPhotoIdName(rs.getString("photo_id_name"));
            	info.setPhotoIdSize(rs.getInt("photo_id_size"));
            	info.setPhotoIdContent(rs.getString("photo_id_content"));
            	info.setMeetCode(rs.getString("meet_code"));
            	info.setCategory(rs.getInt("category"));
            }
            
            rs.close();
            st.close();
            
		} catch (Exception e) {
			 logger.error(e);
	         throw new DaoException(e);
		}
		return info;
	}

}
