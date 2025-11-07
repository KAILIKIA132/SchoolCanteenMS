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
import com.zk.pushsdk.po.PersonBioTemplate;

/**
 * BioTemplate database access layer, operations: Add, Delete, Modify, Query.
 * @author seiya
 *
 */
public class PersonBioTemplateDao extends BaseDao implements
		IBaseDao<PersonBioTemplate>{
	public static final String TABLE_NAME = "pers_bio_template";
    private static Logger logger = Logger.getLogger(PersonBioTemplateDao.class);
    
    /**
	 * Add new BioTemplate to database
	 * @param entity
	 * BioTemplate object
	 * @throws DaoException
	 */
	public void add(PersonBioTemplate entity) throws DaoException {
		String sql = "insert into pers_bio_template (user_id, user_pin, device_sn, " +
				"valid, is_duress, bio_type, version, data_format, " +
				"template_no, template_no_index, size, template_data) values " +
				"(?,?,?,?,?,?,?,?,?,?,?,?)";
		try {
			PreparedStatement pst = getConnection().prepareStatement(sql);
			int index = 1;
			pst.setInt(index++, entity.getUserId());
			pst.setString(index++, entity.getUserPin());
			pst.setString(index++, entity.getDeviceSn());
			pst.setInt(index++, entity.getValid());
			pst.setInt(index++, entity.getIsDuress());
			pst.setInt(index++, entity.getBioType());
			pst.setString(index++, entity.getVersion());
			pst.setInt(index++, entity.getDataFormat());
			pst.setInt(index++, entity.getTemplateNo());
			pst.setInt(index++, entity.getTemplateNoIndex());
			pst.setInt(index++, entity.getSize());
			pst.setString(index++, entity.getTemplateData());
			
			pst.addBatch();
			pst.executeBatch();
			pst.close();
		} catch (Exception e) {
			logger.error(e);
			throw new DaoException(e);
		}
	}

	/**
	 * Delete BioTemplate by ID
	 * @param id
	 * BioTemplate's ID
	 * @throws DaoException
	 */
	public void delete(int id) throws DaoException {
		try {
			String sql = (new StringBuilder())
			.append("delete from pers_bio_template where id=").append(id).toString();
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
	 * Delete BioTemplate by condition
	 * @param cond
	 * Delete condition
	 * @throws DaoException
	 */
	public void delete(String cond) throws DaoException {
		try {
			String sql = (new StringBuilder())
			.append("delete from pers_bio_template where 1=1 ")
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
	 * Get BioTemplate by ID
	 * @param id
	 * BioTemplate's ID
	 * @return
	 * BioTemplate object <code>PersonBioTemplate</code>
	 * @throws DaoException
	 */
	public PersonBioTemplate fatch(int id) throws DaoException {
		try {
			String sql = (new StringBuilder()).
			append("select id, user_id, user_pin, device_sn, valid, " +
					"is_duress, bio_type, version, data_format, " +
					"template_no, template_no_index, size, template_data from pers_bio_template " +
					"where id=").append(id).toString();
            Statement st = getConnection().createStatement();
            ResultSet rs = st.executeQuery(sql);
            PersonBioTemplate template = null;
            if (rs.next()) {
            	template = new PersonBioTemplate();
            	template.setId(rs.getInt("id"));
            	template.setUserId(rs.getInt("user_id"));
            	template.setUserPin(rs.getString("user_pin"));
            	template.setDeviceSn(rs.getString("device_sn"));
            	template.setValid(rs.getInt("valid"));
            	template.setIsDuress(rs.getInt("is_duress"));
            	template.setBioType(rs.getInt("bio_type"));
            	template.setVersion(rs.getString("version"));
            	template.setDataFormat(rs.getInt("data_format"));
            	template.setTemplateNo(rs.getInt("template_no"));
            	template.setTemplateNoIndex(rs.getInt("template_no_index"));
            	template.setSize(rs.getInt("size"));
            	template.setTemplateData(rs.getString("template_data"));
            }
            
            rs.close();
            st.close();
            return template;
		} catch (Exception e) {
			 logger.error(e);
	         throw new DaoException(e);
		}
	}

	/**
	 * Get BioTemplate list by condition
	 * @param cond
	 * Query condition
	 * @return
	 * BioTemplate <code>PersonBioTemplate</code> list
	 * @throws DaoException
	 */
	public List<PersonBioTemplate> fatchList(String cond) throws DaoException {
		List<PersonBioTemplate> list = new ArrayList<PersonBioTemplate>();
		try {
			String sql = (new StringBuilder()).
			append("select id, user_id, user_pin, device_sn, valid, " +
					"is_duress, bio_type, version, data_format, " +
					"template_no, template_no_index, size, template_data from pers_bio_template " +
					"where 1=1 ").append(cond).toString();
            Statement st = getConnection().createStatement();
            ResultSet rs = st.executeQuery(sql);
            while (rs.next()) {
            	PersonBioTemplate template = new PersonBioTemplate();
            	template.setId(rs.getInt("id"));
            	template.setUserId(rs.getInt("user_id"));
            	template.setUserPin(rs.getString("user_pin"));
            	template.setDeviceSn(rs.getString("device_sn"));
            	template.setValid(rs.getInt("valid"));
            	template.setIsDuress(rs.getInt("is_duress"));
            	template.setBioType(rs.getInt("bio_type"));
            	template.setVersion(rs.getString("version"));
            	template.setDataFormat(rs.getInt("data_format"));
            	template.setTemplateNo(rs.getInt("template_no"));
            	template.setTemplateNoIndex(rs.getInt("template_no_index"));
            	template.setSize(rs.getInt("size"));
            	template.setTemplateData(rs.getString("template_data"));
            	
            	list.add(template);
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
	 * Update BioTemplate by only factor
	 * @param entity
	 * BioTemplate
	 * @throws DaoException
	 */
	public void update2(PersonBioTemplate entity) throws DaoException {
		String sql = (new StringBuilder()).
		append("update pers_bio_template set user_id=?, user_pin=?, device_sn=?,").
		append("valid=?, is_duress=?, bio_type=?, version=?, ").
		append("data_format=?, template_no=?, template_no_index=?, ").
		append("size=?, template_data=? ").
		append(" where user_id=? and user_pin=? and template_no=? and bio_type=? and device_sn=?").toString();
		try {
			PreparedStatement pst = getConnection().prepareStatement(sql);
			int index = 1;
			pst.setInt(index++, entity.getUserId());
			pst.setString(index++, entity.getUserPin());
			pst.setString(index++, entity.getDeviceSn());
			pst.setInt(index++, entity.getValid());
			pst.setInt(index++, entity.getIsDuress());
			pst.setInt(index++, entity.getBioType());
			pst.setString(index++, entity.getVersion());
			pst.setInt(index++, entity.getDataFormat());
			pst.setInt(index++, entity.getTemplateNo());
			pst.setInt(index++, entity.getSize());
			pst.setInt(index++, entity.getTemplateNoIndex());
			pst.setString(index++, entity.getTemplateData());
			/**Update condition, make record no repeat*/
			pst.setInt(index++, entity.getUserId());
			pst.setString(index++, entity.getUserPin());
			pst.setInt(index++, entity.getTemplateNo());
			pst.setInt(index++, entity.getBioType());
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
	 * Update BioTemplate by ID
	 * @param entity
	 * BioTemplate
	 * @throws DaoException
	 */
	public void update(PersonBioTemplate entity) throws DaoException {
		String sql = (new StringBuilder()).
		append("update pers_bio_template set user_id=?, user_pin=? device_sn=?, ").
		append("valid=?, is_duress=?, bio_type=?, version=?, ").
		append("data_format=?, template_no=?, template_no_index=?, ").
		append("size=?, template_data=? ").
		append(" where id=?").toString();
		try {
			PreparedStatement pst = getConnection().prepareStatement(sql);
			int index = 1;
			pst.setInt(index++, entity.getUserId());
			pst.setString(index++, entity.getUserPin());
			pst.setString(index++, entity.getDeviceSn());
			pst.setInt(index++, entity.getValid());
			pst.setInt(index++, entity.getIsDuress());
			pst.setInt(index++, entity.getBioType());
			pst.setString(index++, entity.getVersion());
			pst.setInt(index++, entity.getDataFormat());
			pst.setInt(index++, entity.getTemplateNo());
			pst.setInt(index++, entity.getSize());
			pst.setInt(index++, entity.getTemplateNoIndex());
			pst.setString(index++, entity.getTemplateData());
			pst.setInt(index++, entity.getId());
			
			pst.addBatch();
			pst.executeBatch();
			pst.close();
		} catch (Exception e) {
			logger.error(e);
			throw new DaoException(e);
		}
	}

	/**
	 * Add or update BioTemplate
	 * @param entity
	 * BioTemplate object
	 * @return
	 */
	public int addOrUpdate(PersonBioTemplate entity) {
		try {
			/**Execute add operate*/
			add(entity);
		} catch (DaoException e) {
			try {
				/**If record exist, update it*/
				if (e.getCause() instanceof BatchUpdateException) {
					BatchUpdateException be = (BatchUpdateException)e.getCause();
					/**Repeat record wrong code: 1062*/
					if (1062 == be.getErrorCode()) {
						update2(entity);
					}
				}
			} catch (DaoException e2) {
				logger.error(e2);
				throw new DaoException(e2);
			}
		}
		return 0;
	}
	
	public int truncate() throws DaoException {
		// TODO Auto-generated method stub
		return 0;
	}

}
