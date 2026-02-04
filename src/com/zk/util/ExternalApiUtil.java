package com.zk.util;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

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
	private static final java.util.TimeZone NAIROBI_TIMEZONE = java.util.TimeZone.getTimeZone("Africa/Nairobi");
	
	/**
	 * Load external API URL from config.xml
	 * @return External API URL string
	 */
	private static String loadExternalApiUrl() {
		try {
			String configPath = ExternalApiUtil.class.getClassLoader().getResource("config.xml").getPath();
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			Document doc = builder.parse(configPath);
			
			NodeList externalApiNodes = doc.getElementsByTagName("externalapi");
			if (externalApiNodes.getLength() > 0) {
				Element externalApiElement = (Element) externalApiNodes.item(0);
				NodeList urlNodes = externalApiElement.getElementsByTagName("url");
				if (urlNodes.getLength() > 0) {
					String url = urlNodes.item(0).getTextContent().trim();
					logger.info("EXTERNAL API: Loaded URL from config: " + url);
					return url;
				}
			}
		} catch (Exception e) {
			logger.warn("EXTERNAL API: Failed to load URL from config.xml, using default: " + e.getMessage());
		}
		
		// Fallback to default URL
		String defaultUrl = "http://10.35.200.1:8003/api/meal-cards/generate-with-check";
		logger.warn("EXTERNAL API: Using fallback URL: " + defaultUrl);
		return defaultUrl;
	}
	
	/**
	 * Base URL for external API
	 * Read from config.xml file
	 */
	private static String EXTERNAL_API_URL = loadExternalApiUrl();
	
	/**
	 * Get current time in Nairobi timezone
	 * @return Calendar instance set to Nairobi timezone
	 */
	private static java.util.Calendar getNairobiTime() {
		return java.util.Calendar.getInstance(NAIROBI_TIMEZONE);
	}
	
	/**
	 * Get current Date in Nairobi timezone
	 * @return Date object representing current time in Nairobi
	 */
	private static Date getNairobiDate() {
		return getNairobiTime().getTime();
	}
	
	/**
	 * Format date in Nairobi timezone for logging
	 * @return Formatted date string in Nairobi timezone
	 */
	private static String getNairobiTimeString() {
		java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy");
		sdf.setTimeZone(NAIROBI_TIMEZONE);
		return sdf.format(new Date());
	}
	
	/**
	 * Convert verification time string to Nairobi timezone
	 * Assumes the input time is in UTC (as devices typically send UTC)
	 * @param verificationTime Time string in format "yyyy-MM-dd HH:mm:ss"
	 * @return Time string in Nairobi timezone format "yyyy-MM-dd HH:mm:ss"
	 */
	private static String convertToNairobiTime(String verificationTime) {
		if (verificationTime == null || verificationTime.trim().isEmpty()) {
			return verificationTime;
		}
		try {
			// Parse the verification time (assumed to be in UTC from device)
			java.text.SimpleDateFormat inputFormat = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			inputFormat.setTimeZone(java.util.TimeZone.getTimeZone("UTC"));
			java.util.Date date = inputFormat.parse(verificationTime.trim());
			
			// Format in Nairobi timezone
			java.text.SimpleDateFormat outputFormat = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			outputFormat.setTimeZone(NAIROBI_TIMEZONE);
			return outputFormat.format(date);
		} catch (Exception e) {
			logger.warn("Failed to convert verification time to Nairobi timezone: " + verificationTime + ", error: " + e.getMessage());
			// Return original if conversion fails
			return verificationTime;
		}
	}
	
	/**
	 * Get meal type based on current time in Nairobi (EAT timezone)
	 * - 6am to 11am: "mid-morning"
	 * - 11am to 4pm: "lunch"
	 * - else: "lunch"
	 * 
	 * @return Meal type string
	 */
	private static String getMealType() {
		// Use Nairobi timezone (Africa/Nairobi, UTC+3)
		java.util.Calendar cal = getNairobiTime();
		int hour = cal.get(java.util.Calendar.HOUR_OF_DAY);
		
		if (hour >= 6 && hour < 11) {
			return "mid-morning";
		} else if (hour >= 11 && hour < 16) {
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
	 * @param deviceSn The device serial number
	 */
	public static void notifyVerification(String userId, String verificationTime, String userName, String deviceSn) {
		logger.info("═══════════════════════════════════════════════════════════");
		logger.info("🔔 EXTERNAL API CALL REQUESTED - notifyVerification()");
		logger.info("═══════════════════════════════════════════════════════════");
		logger.info("EXTERNAL API: User ID: " + userId);
		logger.info("EXTERNAL API: Verification Time: " + verificationTime);
		logger.info("EXTERNAL API: User Name: " + userName);
		logger.info("EXTERNAL API: Device SN: " + deviceSn);
		logger.info("═══════════════════════════════════════════════════════════");
		
		if (userId == null || userId.isEmpty()) {
			logger.warn("❌ EXTERNAL API: Cannot notify - userId is null or empty");
			return;
		}
		
		logger.info("✅ EXTERNAL API: Submitting async task for userId: " + userId + ", device: " + deviceSn);
		
		// Execute asynchronously
		executorService.submit(new Runnable() {
			@Override
			public void run() {
				logger.info("EXTERNAL API: Async task started for userId: " + userId + ", device: " + deviceSn);
				callExternalApi(userId, verificationTime, userName, deviceSn);
			}
		});
		
		logger.info("EXTERNAL API: Async task submitted for userId: " + userId + ", device: " + deviceSn);
	}
	
	/**
	 * Call external API when a verification occurs (backward compatibility)
	 * @param userId The user ID (userPin) from the verification
	 * @param verificationTime The verification timestamp
	 */
	public static void notifyVerification(String userId, String verificationTime) {
		notifyVerification(userId, verificationTime, null, null);
	}
	
	/**
	 * Call external API when a verification occurs (backward compatibility)
	 * @param userId The user ID (userPin) from the verification
	 */
	public static void notifyVerification(String userId) {
		notifyVerification(userId, null, null, null);
	}
	
	/**
	 * Call external API when a verification occurs (backward compatibility with deviceSn)
	 * @param userId The user ID (userPin) from the verification
	 * @param verificationTime The verification timestamp
	 * @param userName The user name from the verification
	 * @param deviceSn The device serial number
	 */
	public static void notifyVerification(String userId, String verificationTime, String userName) {
		notifyVerification(userId, verificationTime, userName, null);
	}
	
	/**
	 * Make HTTP POST request to external API with JSON body
	 * 
	 * @param userId The user ID (student_id) to pass to the API
	 * @param verificationTime The verification timestamp
	 * @param userName The user name from verification
	 * @param deviceSn The device serial number
	 */
	private static void callExternalApi(String userId, String verificationTime, String userName, String deviceSn) {
		logger.info("═══════════════════════════════════════════════════════════");
		logger.info("🚀 EXTERNAL API CALL INITIATED");
		logger.info("═══════════════════════════════════════════════════════════");
		logger.info("EXTERNAL API: User ID: " + userId);
		logger.info("EXTERNAL API: Device SN: " + deviceSn);
		logger.info("EXTERNAL API: Current URL: " + EXTERNAL_API_URL);
		logger.info("EXTERNAL API: Method: POST");
		logger.info("EXTERNAL API: Timestamp (Nairobi/EAT): " + getNairobiTimeString());
		
		HttpURLConnection connection = null;
		try {
			logger.info("EXTERNAL API: Creating connection to: " + EXTERNAL_API_URL);
			URL url = new URL(EXTERNAL_API_URL);
			connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod("POST");
			connection.setConnectTimeout(30000); // 30 seconds timeout for connection
			connection.setReadTimeout(30000); // 30 seconds timeout for reading response
			connection.setRequestProperty("Content-Type", "application/json");
			connection.setDoOutput(true);
			
			logger.info("EXTERNAL API: Connection configured, formatting student ID...");
			
			// Format student_id with leading zeros (3 digits: 001, 002, etc.)
			String formattedStudentId = formatStudentId(userId);
			logger.info("EXTERNAL API: Student ID formatted - Original: " + userId + ", Formatted: " + formattedStudentId);
			
			// Determine meal type based on current time in Nairobi
			String mealType = getMealType();
			java.util.Calendar cal = getNairobiTime();
			int hour = cal.get(java.util.Calendar.HOUR_OF_DAY);
			int minute = cal.get(java.util.Calendar.MINUTE);
			logger.info("EXTERNAL API: Current time (Nairobi/EAT): " + String.format("%02d:%02d", hour, minute) + " - Meal type determined: " + mealType);
			
			// Build JSON request body (matching exact curl format with camera_sn)
			StringBuilder jsonBodyBuilder = new StringBuilder();
			jsonBodyBuilder.append("{\"student_id\": \"").append(formattedStudentId).append("\", \"meal_type\": \"").append(mealType).append("\"");
			
			// Add camera_sn if deviceSn is available
			if (deviceSn != null && !deviceSn.isEmpty()) {
				jsonBodyBuilder.append(", \"camera_sn\": \"").append(deviceSn).append("\"");
			}
			jsonBodyBuilder.append("}");
			String jsonBody = jsonBodyBuilder.toString();
			logger.info("EXTERNAL API: Request body: " + jsonBody);
			
			// Write request body
			logger.info("EXTERNAL API: Writing request body to connection...");
			OutputStreamWriter writer = new OutputStreamWriter(connection.getOutputStream(), "UTF-8");
			writer.write(jsonBody);
			writer.flush();
			writer.close();
			logger.info("EXTERNAL API: Request body written, making HTTP POST request...");
			logger.info("═══════════════════════════════════════════════════════════");
			logger.info("📡 EXTERNAL API: SENDING HTTP REQUEST NOW");
			logger.info("═══════════════════════════════════════════════════════════");
			
			int responseCode = connection.getResponseCode();
			logger.info("═══════════════════════════════════════════════════════════");
			logger.info("📥 EXTERNAL API: HTTP RESPONSE RECEIVED");
			logger.info("═══════════════════════════════════════════════════════════");
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
			
			String responseBody = response.toString();
			
			// Parse JSON response to check actual success status
			boolean apiSuccess = false;
			String reason = null;
			try {
				// Simple JSON parsing to extract "success" and "reason" fields
				if (responseBody.contains("\"success\":true")) {
					apiSuccess = true;
				} else if (responseBody.contains("\"success\":false")) {
					apiSuccess = false;
					// Extract reason if present
					int reasonStart = responseBody.indexOf("\"reason\":\"");
					if (reasonStart >= 0) {
						reasonStart += 10; // Skip "reason":"
						int reasonEnd = responseBody.indexOf("\"", reasonStart);
						if (reasonEnd > reasonStart) {
							reason = responseBody.substring(reasonStart, reasonEnd);
						}
					}
				}
			} catch (Exception e) {
				logger.warn("EXTERNAL API: Could not parse response JSON: " + e.getMessage());
			}
			
			// Save report to database
			ApiVerificationReport report = new ApiVerificationReport();
			report.setUserPin(userId);
			report.setUserName(userName != null ? userName : "");
			report.setStudentId(formattedStudentId);
			report.setVerificationTime(verificationTime != null ? convertToNairobiTime(verificationTime) : "");
			report.setApiCallTime(getNairobiDate());
			report.setMealType(mealType);
			report.setApiUrl(EXTERNAL_API_URL);
			// Set device SN if available
			if (deviceSn != null && !deviceSn.isEmpty()) {
				report.setDeviceSn(deviceSn);
			}
			
			// Check both HTTP status and API response success
			boolean httpSuccess = (responseCode >= 200 && responseCode < 300);
			boolean overallSuccess = httpSuccess && apiSuccess;
			
			if (overallSuccess) {
				logger.info("═══════════════════════════════════════════════════════════");
				logger.info("✅ EXTERNAL API CALL SUCCESSFUL");
				logger.info("═══════════════════════════════════════════════════════════");
				logger.info("EXTERNAL API: Student ID: " + formattedStudentId + " (original: " + userId + ")");
				logger.info("EXTERNAL API: HTTP Status Code: " + responseCode);
				logger.info("EXTERNAL API: API Response Success: true");
				logger.info("EXTERNAL API: Response Body: " + responseBody);
				logger.info("═══════════════════════════════════════════════════════════");
				
				report.setStatus("SUCCESS");
				report.setResponseCode(responseCode);
				report.setResponseMessage(responseBody);
			} else {
				logger.warn("═══════════════════════════════════════════════════════════");
				logger.warn("⚠️  EXTERNAL API CALL FAILED OR RETURNED ERROR");
				logger.warn("═══════════════════════════════════════════════════════════");
				logger.warn("EXTERNAL API: Student ID: " + formattedStudentId + " (original: " + userId + ")");
				logger.warn("EXTERNAL API: HTTP Status Code: " + responseCode + (httpSuccess ? " (OK)" : " (ERROR)"));
				logger.warn("EXTERNAL API: API Response Success: " + apiSuccess);
				if (reason != null && !reason.isEmpty()) {
					logger.warn("EXTERNAL API: Error Reason: " + reason);
				}
				logger.warn("EXTERNAL API: Response Body: " + responseBody);
				logger.warn("═══════════════════════════════════════════════════════════");
				
				report.setStatus("FAILED");
				report.setResponseCode(responseCode);
				report.setResponseMessage(responseBody);
				if (reason != null && !reason.isEmpty()) {
					report.setErrorMessage(reason);
				}
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
				report.setVerificationTime(verificationTime != null ? convertToNairobiTime(verificationTime) : "");
				report.setApiCallTime(getNairobiDate());
				report.setMealType(getMealType());
				report.setStatus("FAILED");
				report.setErrorMessage(e.getMessage());
				report.setApiUrl(EXTERNAL_API_URL);
				// Set device SN if available
				if (deviceSn != null && !deviceSn.isEmpty()) {
					report.setDeviceSn(deviceSn);
				}
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

