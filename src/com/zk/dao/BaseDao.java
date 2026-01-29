package com.zk.dao;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.log4j.Logger;

import com.zk.exception.DaoException;
import com.zk.pushsdk.util.Constants;
import com.zk.util.ConfigUtil;

public class BaseDao {
	protected static Connection connect = null;
	protected static Connection connect2 = null;
	private static Logger logger = Logger.getLogger(BaseDao.class);

	public BaseDao() {
		try {
			if (connect == null || connect.isClosed()) {
				connectionBase();
			}
		} catch (SQLException e) {
			logger.error(e);
		}

	}

	public Connection getConnection(boolean refresh) {
		if (refresh) {
			return createConnection();
		} else {
			try {
				if (connect == null || connect.isClosed()) {
					connectionBase();
				}
				/**Test Connection*/
				Statement st = null;
				try {
					st = connect.createStatement();
				} catch (SQLException e) {
					connectionBase();
				} finally {
					if (st != null) {
						st.close();
					}
				}

			} catch (SQLException e) {
				logger.error(e);
			}

			return connect;
		}
	}

	/**
	 * Get database connection object
	 * @return
	 * Database Connection Object
	 */
	public Connection getConnection() {
		return getConnection(false);
	}

	public void commit() {
		try {
			getConnection().commit();
		} catch (SQLException e) {
			rollback();
			logger.error(e);
		}
	}

	public void rollback() {
		try {
			getConnection().rollback();
		} catch (SQLException e) {
			logger.error(e);
		}
	}

	private void connectionBase() {
		try {
			String driver = ConfigUtil.getInstance().getValue(
					Constants.DATABASE_DRIVER);
			if (driver == null || driver.trim().isEmpty()) {
				logger.error("Database driver not found in config. Check config.xml file.");
				throw new RuntimeException("Database driver configuration is missing");
			}
			Class.forName(driver);
			String url = ConfigUtil.getInstance().getValue(
					Constants.DATABASE_URL);
			String user = ConfigUtil.getInstance().getValue(
					Constants.DATABASE_USER);
			String pwd = ConfigUtil.getInstance().getValue(
					Constants.DATABASE_PWD);
			logger.info(url + " user:" + user + " pass:" + pwd);
			/**Get Database Connection*/
			connect = DriverManager.getConnection(url, user, pwd);
			/**Set the transaction commit mode*/
			connect.setAutoCommit(false);
		} catch (ClassNotFoundException cnfe) {
			logger.error(cnfe);
			connect = null;
		} catch (SQLException sqle) {
			logger.error(sqle);
			connect = null;
		}
	}

	private Connection createConnection() {
		try {
			if (connect2 != null && !connect2.isClosed()) {
				try {
					connect2.commit();

					return connect2;
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}

			Class.forName(ConfigUtil.getInstance().getValue(
					Constants.DATABASE_DRIVER));
			String url = ConfigUtil.getInstance().getValue(
					Constants.DATABASE_URL);
			String user = ConfigUtil.getInstance().getValue(
					Constants.DATABASE_USER);
			String pwd = ConfigUtil.getInstance().getValue(
					Constants.DATABASE_PWD);
			connect2 = DriverManager.getConnection(url, user, pwd);
			connect2.setAutoCommit(false);
		} catch (ClassNotFoundException cnfe) {
			logger.error(cnfe);
			connect2 = null;
		} catch (SQLException sqle) {
			logger.error(sqle);
			connect2 = null;
		}

		return connect2;
	}

	public void close() {

	}

	protected int getIDENTITY(PreparedStatement pst) {
		try {
			// For MySQL, use LAST_INSERT_ID() instead of SQL Server's @@IDENTITY
			Statement st = getConnection().createStatement();
			ResultSet reset = st.executeQuery("SELECT LAST_INSERT_ID()");
			int rindex = -1;
			if (reset.next()) {
				rindex = reset.getInt(1);
			}
			reset.close();
			st.close();
			return rindex;
		} catch (Exception e) {
			logger.error(e);
			return -1;
		}
	}

	protected boolean isRecordExisted(String sql) {
		try {
			Statement st = getConnection().createStatement();
			ResultSet reset = st.executeQuery(sql);

			if (reset.next()) {
				reset.close();
				st.close();

				return true;
			} else {
				reset.close();
				st.close();

				return false;
			}

		} catch (Exception e) {
			logger.error(e);
			throw new DaoException(e);
		} finally {

		}
	}

	protected void executeUpdateSql(String sql) {
		try {
			Statement st = getConnection().createStatement();
			st.executeUpdate(sql);
			st.close();
		} catch (Exception e) {
			rollback();
			logger.error(e);
			throw new DaoException(e);
		} finally {

		}
	}

	protected String getSingleData(String sql) {
		String strData = "";
		try {
			Statement st = getConnection().createStatement();
			ResultSet reset = st.executeQuery(sql);
			if (reset.next()) {
				strData = reset.getString(1);
			}

			reset.close();
			st.close();
		} catch (Exception e) {
			logger.error(e);
			return null;
		}

		return strData;
	}

	/**
	 * Test database connection
	 * @return true if connection is successful, false otherwise
	 */
	public boolean testConnection() {
		try {
			Connection conn = getConnection(true); // Get fresh connection
			if (conn != null && !conn.isClosed()) {
				DatabaseMetaData metaData = conn.getMetaData();
				logger.info("Database connection test successful. Product: " + metaData.getDatabaseProductName());
				return true;
			} else {
				logger.error("Database connection test failed - connection is null or closed");
				return false;
			}
		} catch (SQLException e) {
			logger.error("Database connection test failed", e);
			return false;
		}
	}

}
