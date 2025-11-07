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
import com.zk.pushsdk.po.MeetInfo;

public class MeetInfoDao extends BaseDao implements IBaseDao<MeetInfo> {

	public static final String TABLE_NAME = "meet_info";
	private static Logger logger = Logger.getLogger(MeetInfoDao.class);
	public MeetInfoDao() {

	}
	
	/**
	 * Get meet basic info list by condition
	 * @param cond
	 * Query condition
	 * @return
	 * Meet basic info <code>MeetInfo</code> list
	 * @throws DaoException
	 */
	public List<MeetInfo> fatchList(String cond) {
		List<MeetInfo> list = new ArrayList<MeetInfo>();
		String sql = (new StringBuilder()).
		append("select meet_info_id, met_name, met_star_sign_tm, met_lat_sign_tm, ear_ret_tm, " +
				"lat_ret_tm, code, met_str_tm, met_end_tm, device_sn " +
				"from meet_info where 1=1 ").append(cond).toString();
		try {
			Statement st = getConnection().createStatement();
			ResultSet rs;
			rs = st.executeQuery(sql);
			while (rs.next()) {
				MeetInfo info = new MeetInfo();
				info.setMeetInfoId(rs.getInt("meet_info_id"));
				info.setMetName(rs.getString("met_name"));
				info.setMetStarSignTm(rs.getString("met_star_sign_tm"));
				info.setMetLatSignTm(rs.getString("met_lat_sign_tm"));
				info.setEarRetTm(rs.getString("ear_ret_tm"));
				info.setLatRetTm(rs.getString("lat_ret_tm"));
				info.setCode(rs.getString("code"));
				info.setMetStrTm(rs.getString("met_str_tm"));
				info.setMetEndTm(rs.getString("met_end_tm"));
				info.setDeviceSn(rs.getString("device_sn"));
            	
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
	 * Get meet basic info by condition, use for interface display
	 * 
	 * @param cond
	 * Query condition
	 * @param startRec
	 * Begin record position
	 * @param pageSize
	 * Query size
	 * @return
	 * Meet basic info list
	 * @throws DaoException
	 */
	public List<MeetInfo> fatchListForPage(String cond, int startRec, int pageSize) {
		List<MeetInfo> list = new ArrayList<MeetInfo>();
		String sql = (new StringBuilder()).
		append("select meet_info_id, met_name, met_star_sign_tm, met_lat_sign_tm, ear_ret_tm, " +
				"lat_ret_tm, code, met_str_tm, met_end_tm, device_sn " +
				"from meet_info a where 1=1 ").append(cond).append(" limit ").append(startRec).append(", ").append(pageSize).toString();
		try {
			Statement st = getConnection().createStatement();
			ResultSet rs;
			rs = st.executeQuery(sql);
			while (rs.next()) {
				MeetInfo info = new MeetInfo();
				info.setMeetInfoId(rs.getInt("meet_info_id"));
				info.setMetName(rs.getString("met_name"));
				info.setMetStarSignTm(rs.getString("met_star_sign_tm"));
				info.setMetLatSignTm(rs.getString("met_lat_sign_tm"));
				info.setEarRetTm(rs.getString("ear_ret_tm"));
				info.setLatRetTm(rs.getString("lat_ret_tm"));
				info.setCode(rs.getString("code"));
				info.setMetStrTm(rs.getString("met_str_tm"));
				info.setMetEndTm(rs.getString("met_end_tm"));
				info.setDeviceSn(rs.getString("device_sn"));
				
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
	 * Get counts of meet basic info by condition
	 * 
	 * @param cond
	 * Query condition
	 * @return
	 */
	public int fatchCount(String cond) {
		int count = 0;
		String sql = "select count(*) count_rec from meet_info where 1=1 " + cond;
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
	 * Add new meet basic info to database
	 * @param entity
	 * meet basic info object
	 * @throws DaoException
	 */
	public void add(MeetInfo entity) throws DaoException {
		StringBuilder sb = new StringBuilder();
		sb.append("insert into meet_info ");
		sb.append("(met_name");
		sb.append(", met_star_sign_tm");
		sb.append(", met_lat_sign_tm");
		sb.append(", ear_ret_tm");
		sb.append(", lat_ret_tm");
		sb.append(", code");
		sb.append(", met_str_tm");
		sb.append(", met_end_tm");
		sb.append(", device_sn)");
		sb.append(" values (?,?,?,?,?,?,?,?,?)");
		/*sb.append(" values (");
		sb.append(entity.getMetName()).append(",");
		sb.append(entity.getMetStarSignTm()).append(",");
		sb.append(entity.getMetLatSignTm()).append(",");
		sb.append(entity.getEarRetTm()).append(",");
		sb.append(entity.getLatRetTm()).append(",");
		sb.append(entity.getCode()).append(",");
		sb.append(entity.getMetStrTm()).append(",");
		sb.append(entity.getMetEndTm()).append(",");
		sb.append(entity.getDeviceSn()).append(")");*/
		
		try {
			PreparedStatement pst = getConnection().prepareStatement(sb.toString());
			int index = 1;
			pst.setString(index++, entity.getMetName());
			pst.setString(index++, entity.getMetStarSignTm());
			pst.setString(index++, entity.getMetLatSignTm());
			pst.setString(index++, entity.getEarRetTm());
			pst.setString(index++, entity.getLatRetTm());
			pst.setString(index++, entity.getCode());
			pst.setString(index++, entity.getMetStrTm());
			pst.setString(index++, entity.getMetEndTm());
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
	 * Add or update new meet basic info
	 * @param entity
	 * Meet basic info object
	 * @return
	 */
	public int addOrUpdate(MeetInfo entity) {
		try {
			add(entity);
		} catch (DaoException e) {
			try {
				if (e.getCause() instanceof BatchUpdateException) {
					BatchUpdateException be = (BatchUpdateException)e.getCause();
					if (1062 == be.getErrorCode()) {
						updateMeetInfoByCode(entity);
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
	 * Update meet basic info by meet code and device serial number
	 * @param entity
	 * Meet basic info
	 * @return
	 * @throws DaoException
	 */
	public int updateMeetInfoByCode(MeetInfo entity) throws DaoException {
		String sql = "update meet_info set met_name=?, met_star_sign_tm=?, met_lat_sign_tm=?, "
				+ "ear_ret_tm=?, lat_ret_tm=?, code=?, met_str_tm=?, "
				+ "met_end_tm=?, device_sn=? where code=? and device_sn=? ";
		try {
			PreparedStatement pst = getConnection().prepareStatement(sql);
			int index = 1;
			pst.setString(index++, entity.getMetName());
			pst.setString(index++, entity.getMetStarSignTm());
			pst.setString(index++, entity.getMetLatSignTm());
			pst.setString(index++, entity.getEarRetTm());
			pst.setString(index++, entity.getLatRetTm());
			pst.setString(index++, entity.getCode());
			pst.setString(index++, entity.getMetStrTm());
			pst.setString(index++, entity.getMetEndTm());
			pst.setString(index++, entity.getDeviceSn());
			
			pst.setString(index++, entity.getCode());
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
	 * Update meet basic info by ID
	 * @param entity
	 * Meet basic info
	 * @throws DaoException
	 */	
	public void update(MeetInfo entity) throws DaoException {
		String sql = "update meet_info set met_name=?, met_star_sign_tm=?, met_lat_sign_tm=?, "
				+ "ear_ret_tm=?, lat_ret_tm=?, code=?, met_str_tm=?, "
				+ "met_end_tm=?, device_sn=? where meet_info_id=?";
		try {
			PreparedStatement pst =  getConnection()
					.prepareStatement(sql);
			int index = 1;
			pst.setString(index++, entity.getMetName());
			pst.setString(index++, entity.getMetStarSignTm());
			pst.setString(index++, entity.getMetLatSignTm());
			pst.setString(index++, entity.getEarRetTm());
			pst.setString(index++, entity.getLatRetTm());
			pst.setString(index++, entity.getCode());
			pst.setString(index++, entity.getMetStrTm());
			pst.setString(index++, entity.getMetEndTm());
			pst.setString(index++, entity.getDeviceSn());
			
			pst.setInt(index++, entity.getMeetInfoId());

			pst.addBatch();
			pst.executeBatch();
			pst.close();
		} catch (SQLException e) {
			logger.error(e);
			throw new DaoException(e);
		}
	}
	
	/**
	 * Delete meet basic info by ID
	 * @param id
	 * Meet basic info's ID
	 * @throws DaoException
	 */
	public void delete(int id) throws DaoException {
		try {
			String sql = (new StringBuilder())
			.append("delete from meet_info where meet_info_id=")
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
	 * Delete meet basic info by condition
	 * @param cond
	 * Delete condition
	 * @throws DaoException
	 */
	public void delete(String cond) throws DaoException {
		try {
			String sql = (new StringBuilder())
			.append("delete from meet_info where 1=1 ")
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
	 * Get Meet basic info by ID
	 * @param id
	 * Meet basic info's ID
	 * @return
	 * Meet basic info object <code>MeetInfo</code>
	 * @throws DaoException
	 */
	public MeetInfo fatch(int id) throws DaoException {
		try {
			String sql = (new StringBuilder()).
			append("select meet_info_id, met_name, met_star_sign_tm, met_lat_sign_tm, ear_ret_tm, " +
					"lat_ret_tm, code, met_str_tm, met_end_tm, device_sn " +
					"from meet_info where meet_info_id=").append(id).toString();
            Statement st = getConnection().createStatement();
            ResultSet rs = st.executeQuery(sql);
            MeetInfo info = null;
            if (rs.next()) {
            	info = new MeetInfo();
            	info.setMeetInfoId(rs.getInt("meet_info_id"));
				info.setMetName(rs.getString("met_name"));
				info.setMetStarSignTm(rs.getString("met_star_sign_tm"));
				info.setMetLatSignTm(rs.getString("met_lat_sign_tm"));
				info.setEarRetTm(rs.getString("ear_ret_tm"));
				info.setLatRetTm(rs.getString("lat_ret_tm"));
				info.setCode(rs.getString("code"));
				info.setMetStrTm(rs.getString("met_str_tm"));
				info.setMetEndTm(rs.getString("met_end_tm"));
				info.setDeviceSn(rs.getString("device_sn"));
            }
            
            rs.close();
            st.close();
            return info;
		} catch (Exception e) {
			 logger.error(e);
	         throw new DaoException(e);
		}
	}

	@Override
	public int truncate() throws DaoException {
		// TODO Auto-generated method stub
		return 0;
	}


}
