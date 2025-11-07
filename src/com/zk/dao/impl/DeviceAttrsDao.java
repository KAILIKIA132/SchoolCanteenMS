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
import com.zk.pushsdk.po.DeviceAttrs;

public class DeviceAttrsDao extends BaseDao implements IBaseDao<DeviceAttrs>{

	private static Logger logger = Logger.getLogger(DeviceAttrsDao.class);
	
	@Override
	public void add(DeviceAttrs entity) throws DaoException {
		StringBuilder sb = new StringBuilder();
		sb.append("insert into device_attrs ");
		sb.append("(device_sn");
		sb.append(", attr_name");
		sb.append(", attr_value)");
		sb.append(" values (?,?,?)");
		
		try {
			PreparedStatement pst = getConnection().prepareStatement(sb.toString());
			int index = 1;
			pst.setString(index++, entity.getDeviceSn());
			pst.setString(index++, entity.getAttrName());
			pst.setString(index++, entity.getAttrValue());

			pst.addBatch();
			pst.executeBatch();
			pst.close();
		} catch (Exception e) {
			logger.error(e);
			throw new DaoException(e);
		}
		
	}

	@Override
	public void update(DeviceAttrs entity) throws DaoException {
		String sql = "update device_attrs set device_sn=?, attr_name=?, attr_value=? "
				+ " where device_sn=? and attr_name=?";
		try {
			PreparedStatement pst =  getConnection()
					.prepareStatement(sql);
			int index = 1;
			pst.setString(index++, entity.getDeviceSn());
			pst.setString(index++, entity.getAttrName());
			pst.setString(index++, entity.getAttrValue());
			pst.setString(index++, entity.getDeviceSn());
			pst.setString(index++, entity.getAttrName());

			pst.addBatch();
			pst.executeBatch();
			pst.close();
		} catch (SQLException e) {
			logger.error(e);
			throw new DaoException(e);
		}
		
	}

	public int addOrUpdate(DeviceAttrs entity) {
		try {
			add(entity);
		} catch (DaoException e) {
			try {
				if (e.getCause() instanceof BatchUpdateException) {
					BatchUpdateException be = (BatchUpdateException)e.getCause();
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
	
	@Override
	public void delete(int id) throws DaoException {
		try {
			String sql = (new StringBuilder())
			.append("delete from device_attrs where user_id=")
			.append(id).toString();
			Statement st = getConnection().createStatement();
			st.executeUpdate(sql);
			st.close();
		} catch (Exception e) {
			logger.error(e);
			throw new DaoException(e);
		}
		
	}

	@Override
	public void delete(String cond) throws DaoException {
		try {
			String sql = (new StringBuilder())
			.append("delete from device_attrs where 1=1 ")
			.append(cond).toString();
			Statement st = getConnection().createStatement();
			st.executeUpdate(sql);
			st.close();
		} catch (Exception e) {
			logger.error(e);
			throw new DaoException(e);
		}
		
	}

	@Override
	public DeviceAttrs fatch(int id) throws DaoException {
		DeviceAttrs attrs = null;
		try {
			String sql = (new StringBuilder()).
			append("select id, device_sn, attr_name, attr_value  " +
					" from device_attrs where id=").append(id).toString();
            Statement st = getConnection().createStatement();
            ResultSet rs = st.executeQuery(sql);
           
            if (rs.next()) {
            	attrs = new DeviceAttrs();
            	attrs.setId(rs.getInt("id"));
            	attrs.setDeviceSn(rs.getString("device_sn"));
            	attrs.setAttrName(rs.getString("attr_name"));
            	attrs.setAttrValue(rs.getString("attr_value"));
            }
            
            rs.close();
            st.close();
            
		} catch (Exception e) {
			 logger.error(e);
	         throw new DaoException(e);
		}
		return attrs;
	}

	@Override
	public List<DeviceAttrs> fatchList(String cond) throws DaoException {
		List<DeviceAttrs> list = new ArrayList<DeviceAttrs>();
		String sql = (new StringBuilder()).
				append("select id, device_sn, attr_name, attr_value  " +
						" from device_attrs where 1=1 ").append(cond).toString();
		try {
			Statement st = getConnection().createStatement();
			ResultSet rs;
			rs = st.executeQuery(sql);
			while (rs.next()) {
				DeviceAttrs attrs = new DeviceAttrs();
            	attrs.setId(rs.getInt("id"));
            	attrs.setDeviceSn(rs.getString("device_sn"));
            	attrs.setAttrName(rs.getString("attr_name"));
            	attrs.setAttrValue(rs.getString("attr_value"));
            	
            	list.add(attrs);
			}
			
			rs.close();
			st.close();
			return list;
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
