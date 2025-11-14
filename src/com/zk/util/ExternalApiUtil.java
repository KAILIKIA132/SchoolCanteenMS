package com.zk.util;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.log4j.Logger;

import com.zk.dao.impl.ApiVerificationReport;
import com.zk.dao.impl.ApiVerificationReportDao;
import com.zk.exception.DaoException;

/**
 * Utility class for calling external APIs when verifications occur
 * @author system
 */
public class ExternalApiUtil {
	
	private static Logger logger = Logger.getLogger(ExternalApiUtil.class);
	private static ExecutorService executorService = Executors.newFixedThreadPool(5);
	
	/**
	 * Base URL for external API
	 * Configure this in your config file or environment variable
	 */
	private static final String EXTERNAL_API_URL = "http://192.168.192.45:8001/api/meal-cards/generate-with-check";
	
	/**
	 * Get meal type based on current time
	 * - 6am to 11am: "mid morning"
	 * - 11am to 3pm: "lunch"
	 * 
	 * @return Meal type string
	 */
	private static String getMealType() {
		java.util.Calendar cal = java.util.Calendar.getInstance();
		int hour = cal.get(java.util.Calendar.HOUR_OF_DAY);
		
		if (hour >= 6 && hour < 11) {
			return "mid morning";
		} else if (hour >= 11 && hour < 15) {
			return "lunch";
		} else {
			// Default to lunch if outside these hours
			return "lunch";
		}
	}
	
	/**
	 * Call external API when a verification occurs
	 * This method runs asynchronously to avoid blocking the main processing
	 * 
	 * @param userId The user ID (userPin) from the verification
	 * @param verificationTime The verification timestamp
	 * @param userName The user name from the verification
	 */
	public static void notifyVerification(String userId, String verificationTime, String userName) {
		logger.info("=== EXTERNAL API: notifyVerification called with userId: " + userId + " ===");
		
		if (userId == null || userId.isEmpty()) {
			logger.warn("Cannot notify external API: userId is null or empty");
			return;
		}
		
		logger.info("EXTERNAL API: Submitting async task for userId: " + userId);
		
		// Execute asynchronously
		executorService.submit(new Runnable() {
			@Override
			public void run() {
				logger.info("EXTERNAL API: Async task started for userId: " + userId);
				callExternalApi(userId, verificationTime, userName);
			}
		});
		
		logger.info("EXTERNAL API: Async task submitted for userId: " + userId);
	}
	
	/**
	 * Call external API when a verification occurs (backward compatibility)
	 * @param userId The user ID (userPin) from the verification
	 * @param verificationTime The verification timestamp
	 */
	public static void notifyVerification(String userId, String verificationTime) {
		notifyVerification(userId, verificationTime, null);
	}
	
	/**
	 * Call external API when a verification occurs (backward compatibility)
	 * @param userId The user ID (userPin) from the verification
	 */
	public static void notifyVerification(String userId) {
		notifyVerification(userId, null, null);
	}
	
	/**
	 * Make HTTP POST request to external API with JSON body
	 * 
	 * @param userId The user ID (student_id) to pass to the API
	 * @param verificationTime The verification timestamp
	 * @param userName The user name from verification
	 */
	private static void callExternalApi(String userId, String verificationTime, String userName) {
		logger.info("═══════════════════════════════════════════════════════════");
		logger.info("🚀 EXTERNAL API CALL INITIATED");
		logger.info("═══════════════════════════════════════════════════════════");
		logger.info("EXTERNAL API: User ID: " + userId);
		logger.info("EXTERNAL API: URL: " + EXTERNAL_API_URL);
		logger.info("EXTERNAL API: Method: POST");
		logger.info("EXTERNAL API: Timestamp: " + new java.util.Date());
		
		HttpURLConnection connection = null;
		try {
			logger.info("EXTERNAL API: Creating connection to: " + EXTERNAL_API_URL);
			URL url = new URL(EXTERNAL_API_URL);
			connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod("POST");
			connection.setConnectTimeout(5000); // 5 seconds timeout
			connection.setReadTimeout(5000);
			connection.setRequestProperty("Content-Type", "application/json");
			connection.setRequestProperty("Accept", "application/json");
			connection.setDoOutput(true);
			
			logger.info("EXTERNAL API: Connection configured, formatting student ID...");
			
			// Format student_id with leading zeros (3 digits: 001, 002, etc.)
			String formattedStudentId = formatStudentId(userId);
			logger.info("EXTERNAL API: Student ID formatted - Original: " + userId + ", Formatted: " + formattedStudentId);
			
			// Determine meal type based on current time
			String mealType = getMealType();
			java.util.Calendar cal = java.util.Calendar.getInstance();
			int hour = cal.get(java.util.Calendar.HOUR_OF_DAY);
			int minute = cal.get(java.util.Calendar.MINUTE);
			logger.info("EXTERNAL API: Current time: " + String.format("%02d:%02d", hour, minute) + " - Meal type determined: " + mealType);
			
			// Build JSON request body (matching exact curl format)
			String jsonBody = "{\"student_id\": \"" + formattedStudentId + "\", \"meal_type\": \"" + mealType + "\"}";
			logger.info("EXTERNAL API: Request body: " + jsonBody);
			
			// Write request body
			logger.info("EXTERNAL API: Writing request body to connection...");
			OutputStreamWriter writer = new OutputStreamWriter(connection.getOutputStream(), "UTF-8");
			writer.write(jsonBody);
			writer.flush();
			writer.close();
			logger.info("EXTERNAL API: Request body written, getting response...");
			
			int responseCode = connection.getResponseCode();
			logger.info("EXTERNAL API: Response code received: " + responseCode);
			
			// Read response
			BufferedReader in;
			if (responseCode >= 200 && responseCode < 300) {
				in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			} else {
				in = new BufferedReader(new InputStreamReader(connection.getErrorStream()));
			}
			
			StringBuilder response = new StringBuilder();
			String inputLine;
			while ((inputLine = in.readLine()) != null) {
				response.append(inputLine);
			}
			in.close();
			
			// Save report to database
			ApiVerificationReport report = new ApiVerificationReport();
			report.setUserPin(userId);
			report.setUserName(userName != null ? userName : "");
			report.setStudentId(formattedStudentId);
			report.setVerificationTime(verificationTime != null ? verificationTime : "");
			report.setApiCallTime(new Date());
			report.setMealType(mealType);
			report.setApiUrl(EXTERNAL_API_URL);
			
			if (responseCode >= 200 && responseCode < 300) {
				logger.info("═══════════════════════════════════════════════════════════");
				logger.info("✅ EXTERNAL API CALL SUCCESSFUL");
				logger.info("═══════════════════════════════════════════════════════════");
				logger.info("EXTERNAL API: Student ID: " + formattedStudentId + " (original: " + userId + ")");
				logger.info("EXTERNAL API: Response Code: " + responseCode);
				logger.info("EXTERNAL API: Response: " + response.toString());
				logger.info("═══════════════════════════════════════════════════════════");
				
				report.setStatus("SUCCESS");
				report.setResponseCode(responseCode);
				report.setResponseMessage(response.toString());
			} else {
				logger.warn("═══════════════════════════════════════════════════════════");
				logger.warn("⚠️  EXTERNAL API CALL RETURNED ERROR STATUS");
				logger.warn("═══════════════════════════════════════════════════════════");
				logger.warn("EXTERNAL API: Student ID: " + formattedStudentId + " (original: " + userId + ")");
				logger.warn("EXTERNAL API: Response Code: " + responseCode);
				logger.warn("EXTERNAL API: Response: " + response.toString());
				logger.warn("═══════════════════════════════════════════════════════════");
				
				report.setStatus("FAILED");
				report.setResponseCode(responseCode);
				report.setResponseMessage(response.toString());
			}
			
			saveReport(report);
			
		} catch (Exception e) {
			logger.error("═══════════════════════════════════════════════════════════");
			logger.error("❌ EXTERNAL API CALL FAILED");
			logger.error("═══════════════════════════════════════════════════════════");
			logger.error("EXTERNAL API: Student ID: " + userId);
			logger.error("EXTERNAL API: URL: " + EXTERNAL_API_URL);
			logger.error("EXTERNAL API: Error: " + e.getMessage());
			logger.error("EXTERNAL API: Exception: " + e.getClass().getName());
			logger.error("═══════════════════════════════════════════════════════════");
			logger.error("Full stack trace:", e);
			
			// Save failed report to database
			try {
				ApiVerificationReport report = new ApiVerificationReport();
				report.setUserPin(userId);
				report.setUserName(userName != null ? userName : "");
				report.setStudentId(formatStudentId(userId));
				report.setVerificationTime(verificationTime != null ? verificationTime : "");
				report.setApiCallTime(new Date());
				report.setMealType(getMealType());
				report.setStatus("FAILED");
				report.setErrorMessage(e.getMessage());
				report.setApiUrl(EXTERNAL_API_URL);
				saveReport(report);
			} catch (Exception ex) {
				logger.error("Failed to save error report to database: " + ex.getMessage());
			}
		} finally {
			if (connection != null) {
				connection.disconnect();
			}
		}
	}
	
	/**
	 * Format student ID with leading zeros to 3 digits (001, 002, etc.)
	 * 
	 * @param userId The user ID from verification
	 * @return Formatted student ID with leading zeros
	 */
	private static String formatStudentId(String userId) {
		if (userId == null || userId.isEmpty()) {
			return "001";
		}
		
		try {
			// Parse as integer to remove any leading zeros, then format with 3 digits
			int id = Integer.parseInt(userId);
			return String.format("%03d", id);
		} catch (NumberFormatException e) {
			// If not a number, return as-is or default to 001
			logger.warn("User ID is not a number: " + userId + ", using as-is");
			return userId;
		}
	}
	
	/**
	 * Save verification report to database
	 * @param report The verification report to save
	 */
	private static void saveReport(ApiVerificationReport report) {
		try {
			ApiVerificationReportDao dao = new ApiVerificationReportDao();
			dao.add(report);
			dao.commit();
			dao.close();
			logger.info("EXTERNAL API: Report saved to database - Status: " + report.getStatus() + ", User: " + report.getUserPin());
		} catch (DaoException e) {
			logger.error("Failed to save verification report to database: " + e.getMessage(), e);
		} catch (Exception e) {
			logger.error("Unexpected error saving verification report: " + e.getMessage(), e);
		}
	}
	
	/**
	 * Shutdown the executor service (call this on application shutdown)
	 */
	public static void shutdown() {
		if (executorService != null && !executorService.isShutdown()) {
			executorService.shutdown();
		}
	}
}

