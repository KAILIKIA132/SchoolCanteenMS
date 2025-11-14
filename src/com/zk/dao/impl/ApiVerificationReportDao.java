package com.zk.dao.impl;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.zk.dao.BaseDao;
import com.zk.dao.IBaseDao;
import com.zk.exception.DaoException;

/**
 * API Verification Report database access layer
 * @author system
 */
public class ApiVerificationReportDao extends BaseDao implements IBaseDao<ApiVerificationReport> {
	
	public static final String TABLE_NAME = "api_verification_report";
	private static Logger logger = Logger.getLogger(ApiVerificationReportDao.class);

	/**
	 * Add new verification report to the database
	 * @param entity Verification report object
	 * @throws DaoException
	 */
	public void add(ApiVerificationReport entity) throws DaoException {
		String sql = "insert into api_verification_report(user_pin, user_name, student_id, verification_time, " +
				"api_call_time, meal_type, status, response_code, response_message, error_message, api_url) " +
				"values(?,?,?,?,?,?,?,?,?,?,?)";
		try {
			PreparedStatement pst = getConnection().prepareStatement(sql);
			int index = 1;
			pst.setString(index++, entity.getUserPin());
			pst.setString(index++, entity.getUserName());
			pst.setString(index++, entity.getStudentId());
			pst.setString(index++, entity.getVerificationTime());
			pst.setTimestamp(index++, new java.sql.Timestamp(entity.getApiCallTime().getTime()));
			pst.setString(index++, entity.getMealType());
			pst.setString(index++, entity.getStatus());
			pst.setObject(index++, entity.getResponseCode());
			pst.setString(index++, entity.getResponseMessage());
			pst.setString(index++, entity.getErrorMessage());
			pst.setString(index++, entity.getApiUrl());
			
			pst.executeUpdate();
			pst.close();
			
		} catch (Exception e) {
			logger.error(e);
			throw new DaoException(e);
		}
	}

	/**
	 * Query verification reports with conditions
	 * @param cond Query condition (WHERE clause without "WHERE", may include LIMIT)
	 * @return List of verification reports
	 */
	public List<ApiVerificationReport> query(String cond) {
		List<ApiVerificationReport> list = new ArrayList<ApiVerificationReport>();
		// Check if cond already contains LIMIT clause
		String orderBy = " order by api_call_time desc";
		String sql;
		if (cond.toUpperCase().contains("LIMIT")) {
			// If LIMIT is already in cond, insert ORDER BY before LIMIT
			int limitIndex = cond.toUpperCase().indexOf("LIMIT");
			String beforeLimit = cond.substring(0, limitIndex);
			String afterLimit = cond.substring(limitIndex);
			sql = "select report_id, user_pin, user_name, student_id, verification_time, api_call_time, " +
					"meal_type, status, response_code, response_message, error_message, api_url " +
					"from api_verification_report where 1=1 " + beforeLimit + orderBy + " " + afterLimit;
		} else {
			// No LIMIT, add ORDER BY at the end
			sql = "select report_id, user_pin, user_name, student_id, verification_time, api_call_time, " +
					"meal_type, status, response_code, response_message, error_message, api_url " +
					"from api_verification_report where 1=1 " + cond + orderBy;
		}
		try {
			Statement st = getConnection().createStatement();
			ResultSet rs = st.executeQuery(sql);
			while (rs.next()) {
				ApiVerificationReport report = new ApiVerificationReport();
				report.setReportId(rs.getInt("report_id"));
				report.setUserPin(rs.getString("user_pin"));
				report.setUserName(rs.getString("user_name"));
				report.setStudentId(rs.getString("student_id"));
				report.setVerificationTime(rs.getString("verification_time"));
				report.setApiCallTime(rs.getTimestamp("api_call_time"));
				report.setMealType(rs.getString("meal_type"));
				report.setStatus(rs.getString("status"));
				report.setResponseCode(rs.getInt("response_code"));
				if (rs.wasNull()) {
					report.setResponseCode(null);
				}
				report.setResponseMessage(rs.getString("response_message"));
				report.setErrorMessage(rs.getString("error_message"));
				report.setApiUrl(rs.getString("api_url"));
				list.add(report);
			}
			rs.close();
			st.close();
		} catch (Exception e) {
			logger.error(e);
		}
		return list;
	}

	/**
	 * Get count of verification reports with conditions
	 * @param cond Query condition
	 * @return Count
	 */
	public int fetchCount(String cond) {
		int count = 0;
		String sql = "select count(*) count_rec from api_verification_report where 1=1 " + cond;
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
		}
		return count;
	}

	@Override
	public void delete(int id) throws DaoException {
		// Not implemented for reports
	}

	@Override
	public void delete(String cond) throws DaoException {
		// Not implemented for reports
	}

	@Override
	public void update(ApiVerificationReport entity) throws DaoException {
		// Not implemented for reports
	}

	@Override
	public ApiVerificationReport fatch(int id) throws DaoException {
		// Not implemented
		return null;
	}

	@Override
	public List<ApiVerificationReport> fatchList(String cond) throws DaoException {
		return query(cond);
	}

	@Override
	public int truncate() throws DaoException {
		// Not implemented
		return 0;
	}
}

