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
import com.zk.pushsdk.po.Adv;

public class AdvDao extends BaseDao implements IBaseDao<Adv> {

	public static final String TABLE_NAME = "adv_info";
	private static Logger logger = Logger.getLogger(AdvDao.class);
	public AdvDao() {

	}
	
	/**
	 * Get adv info list by condition
	 * @param cond
	 * Query condition
	 * @return
	 * Adv info <code>AdvInfo</code> list
	 * @throws DaoException
	 */
	public List<Adv> fatchList(String cond) {
		List<Adv> list = new ArrayList<Adv>();
		String sql = (new StringBuilder()).
		append("select adv_id, type, file_name, url, device_sn from adv_info where 1=1 ").append(cond).toString();
		try {
			Statement st = getConnection().createStatement();
			ResultSet rs;
			rs = st.executeQuery(sql);
			while (rs.next()) {
				Adv adv = new Adv();
				adv.setAdvId(rs.getInt("adv_id"));
				adv.setType(rs.getInt("type"));
				adv.setFileName(rs.getString("file_name"));
				adv.setUrl(rs.getString("url"));
				adv.setDeviceSn(rs.getString("device_sn"));
				
            	list.add(adv);
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
	 * Get adv info by condition, use for interface display
	 * 
	 * @param cond
	 * Query condition
	 * @param startRec
	 * Begin record position
	 * @param pageSize
	 * Query size
	 * @return
	 * Adv info list
	 * @throws DaoException
	 */
	public List<Adv> fatchListForPage(String cond, int startRec, int pageSize) {
		List<Adv> list = new ArrayList<Adv>();
		String sql = (new StringBuilder()).
		append("select adv_id, type, file_name, url, device_sn " +
				"from adv_info a where 1=1 ").append(cond).append(" limit ").append(startRec).append(", ").append(pageSize).toString();
		try {
			Statement st = getConnection().createStatement();
			ResultSet rs;
			rs = st.executeQuery(sql);
			while (rs.next()) {
				Adv adv = new Adv();
				adv.setAdvId(rs.getInt("adv_id"));
				adv.setType(rs.getInt("type"));
				adv.setFileName(rs.getString("file_name"));
				adv.setUrl(rs.getString("url"));
				adv.setDeviceSn(rs.getString("device_sn"));
				
            	list.add(adv);
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
	 * Get counts of avd info by condition
	 * 
	 * @param cond
	 * Query condition
	 * @return
	 */
	public int fatchCount(String cond) {
		int count = 0;
		String sql = "select count(*) count_rec from adv_info where 1=1 " + cond;
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
	 * Add new adv info to database
	 * @param entity
	 * adv info object
	 * @throws DaoException
	 */
	public void add(Adv entity) throws DaoException {
		StringBuilder sb = new StringBuilder();
		sb.append("insert into adv_info ");
		sb.append("(type");
		sb.append(", file_name");
		sb.append(", url");
		sb.append(", device_sn)");
		sb.append(" values (?,?,?,?)");
		
		try {
			PreparedStatement pst = getConnection().prepareStatement(sb.toString());
			int index = 1;
			pst.setInt(index++, entity.getType());
			pst.setString(index++, entity.getFileName());
			pst.setString(index++, entity.getUrl());
			pst.setString(index++, entity.getDeviceSn());

			pst.addBatch();
			pst.executeBatch();
			pst.close();
		} catch (Exception e) {
			logger.error(e);
			throw new DaoException(e);
		}

	}
	
	/**
	 * Add or update new adv info
	 * @param entity
	 * Adv info object
	 * @return
	 */
	public int addOrUpdate(Adv entity) {
		try {
			add(entity);
		} catch (DaoException e) {
			try {
				if (e.getCause() instanceof BatchUpdateException) {
					BatchUpdateException be = (BatchUpdateException)e.getCause();
					if (1062 == be.getErrorCode()) {
						//updateAdvById(entity);
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
	 * Update adv info by meet code and device serial number
	 * @param entity
	 * Meet basic info
	 * @return
	 * @throws DaoException
	 */
/*	public int updateAdvById(Adv entity) throws DaoException {
		String sql = "update adv_info set type=?, file_name=?, url=? where adv_id=? and device_sn=? ";
		try {
			PreparedStatement pst = getConnection().prepareStatement(sql);
			int index = 1;
			pst.setInt(index++, entity.getType());
			pst.setString(index++, entity.getFileName());
			pst.setString(index++, entity.getUrl());
			
			pst.setInt(index++, entity.getAdvId());
			pst.setString(index++, entity.getDeviceSn());

			pst.addBatch();
			pst.executeBatch();
			pst.close();
		} catch (SQLException e) {
			logger.error(e);
			throw new DaoException(e);
		}
		return 0;
	}*/
	
	/**
	 * Update adv info by ID
	 * @param entity
	 * Adv info
	 * @throws DaoException
	 */	
	public void update(Adv entity) throws DaoException {
		String sql = "update adv_info set type=?, file_name=?, url=?, device_sn=? where adv_id=?";
		try {
			PreparedStatement pst =  getConnection()
					.prepareStatement(sql);
			int index = 1;
			pst.setInt(index++, entity.getType());
			pst.setString(index++, entity.getFileName());
			pst.setString(index++, entity.getUrl());
			pst.setString(index++, entity.getDeviceSn());
			
			pst.setInt(index++, entity.getAdvId());

			pst.addBatch();
			pst.executeBatch();
			pst.close();
		} catch (SQLException e) {
			logger.error(e);
			throw new DaoException(e);
		}
	}
	
	/**
	 * Delete adv info by ID
	 * @param id
	 * Adv info's ID
	 * @throws DaoException
	 */
	public void delete(int id) throws DaoException {
		try {
			String sql = (new StringBuilder())
			.append("delete from adv_info where adv_id=")
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
	 * Delete adv info by condition
	 * @param cond
	 * Delete condition
	 * @throws DaoException
	 */
	public void delete(String cond) throws DaoException {
		try {
			String sql = (new StringBuilder())
			.append("delete from adv_info where 1=1 ")
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
	 * Get Adv info by ID
	 * @param id
	 * Adv info's ID
	 * @return
	 * adv info object <code>AdvInfo</code>
	 * @throws DaoException
	 */
	public Adv fatch(int id) throws DaoException {
		try {
			String sql = (new StringBuilder()).
			append("select adv_id, type, file_name, url, device_sn " +
					"from adv_info where adv_id=").append(id).toString();
            Statement st = getConnection().createStatement();
            ResultSet rs = st.executeQuery(sql);
            Adv adv = null;
            if (rs.next()) {
            	adv = new Adv();
            	
            	adv.setAdvId(rs.getInt("adv_id"));
				adv.setType(rs.getInt("type"));
				adv.setFileName(rs.getString("file_name"));
				adv.setUrl(rs.getString("url"));
				adv.setDeviceSn(rs.getString("device_sn"));
            }
            
            rs.close();
            st.close();
            return adv;
		} catch (Exception e) {
			 logger.error(e);
	         throw new DaoException(e);
		}
	}

	@Override
	public int truncate() throws DaoException {
		return 0;
	}

}
