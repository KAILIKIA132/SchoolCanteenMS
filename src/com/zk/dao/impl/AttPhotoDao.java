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
import com.zk.pushsdk.po.AttPhoto;

/**
 * Attendance photo database access layer, operations: Add, Delete, Modify, Query.
 * @author seiya
 *
 */
public class AttPhotoDao extends BaseDao implements IBaseDao<AttPhoto>{
	
	private Logger logger = Logger.getLogger(AttPhotoDao.class);

	/**
	 * Add new attendance photo to the database
	 * @param entity
	 * Attendance photo object
	 * @throws DaoException
	 */
	public void add(AttPhoto entity) throws DaoException {
		String sql = "insert into att_photo(device_sn, file_name, size, "
				+ "cmd, file_path) "
				+ "values(?,?,?,?,?)";
		try {
			PreparedStatement pst = getConnection()
					.prepareStatement(sql);
			int index = 1;
			pst.setString(index++, entity.getDeviceSn());
			pst.setString(index++, entity.getFileName());
			pst.setInt(index++, entity.getSize());
			pst.setString(index++, entity.getCmd());
			pst.setString(index++, entity.getFilePath());

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
	 * Delete attendance photo by ID
	 * @param id
	 * Attendance record ID
	 * @throws DaoException
	 */
	public void delete(int id) throws DaoException {
		try {
			String sql = (new StringBuilder())
			.append("delete from att_photo where id=")
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
	 * Delete attendance photo by condition
	 * @param cond
	 * Delete condition
	 * @throws DaoException
	 */
	public void delete(String cond) throws DaoException {
		try {
			String sql = (new StringBuilder())
			.append("delete from att_photo where 1=1 ")
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
	 * Get attendance photo by ID
	 * @param id
	 * Attendance photo's ID
	 * @return
	 * Attendance photo objetc<code>AttPhoto</code>
	 * @throws DaoException
	 */
	public AttPhoto fatch(int id) throws DaoException {
		String sql = (new StringBuilder()).
		append("select id, device_sn, file_name, size, cmd, file_path " +
				" from att_photo where id=").append(id).toString();
		AttPhoto photo = null;
		try {
			Statement st = getConnection().createStatement();
			ResultSet rs;
			rs = st.executeQuery(sql);
			if (rs.next()) {
				photo = new AttPhoto();
				photo.setId(rs.getInt("id"));
				photo.setDeviceSn(rs.getString("device_sn"));
				photo.setFileName(rs.getString("file_name"));
				photo.setSize(rs.getInt("size"));
				photo.setCmd(rs.getString("cmd"));
				photo.setFilePath(rs.getString("file_path"));
			}
			rs.close();
			st.close();
			
		} catch (Exception e) {
			logger.error(e);
            throw new DaoException(e);
		}
		return photo;
	}

	/**
	 * Get attendance photo by condition
	 * 
	 * @param cond
	 * Query condition
	 * @return
	 * List of attendance photo
	 * @throws DaoException
	 */
	public List<AttPhoto> fatchList(String cond) throws DaoException {
		List<AttPhoto> list = new ArrayList<AttPhoto>();
		String sql = (new StringBuilder()).
		append("select id, device_sn, file_name, size, cmd, file_path " +
				" from att_photo where 1=1 ").append(cond).toString();
		try {
			Statement st = getConnection().createStatement();
			ResultSet rs;
			rs = st.executeQuery(sql);
			while (rs.next()) {
				AttPhoto photo = new AttPhoto();
				photo.setId(rs.getInt("id"));
				photo.setDeviceSn(rs.getString("device_sn"));
				photo.setFileName(rs.getString("file_name"));
				photo.setSize(rs.getInt("size"));
				photo.setCmd(rs.getString("cmd"));
				photo.setFilePath(rs.getString("file_path"));
            	
            	list.add(photo);
			}
			rs.close();
			st.close();
		} catch (Exception e) {
			logger.error(e);
            throw new DaoException(e);
		}
		return list;
	}

	public int truncate() throws DaoException {
		return 0;
	}

	public void update(AttPhoto entity) throws DaoException {
		
	}

}
