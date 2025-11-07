package com.zk.util;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.log4j.Logger;

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
	private static final String EXTERNAL_API_URL = "http://192.168.253.45:8001/api/meal-cards/generate-with-check";
	
	/**
	 * Meal type to send in the request
	 */
	private static final String MEAL_TYPE = "lunch";
	
	/**
	 * Call external API when a verification occurs
	 * This method runs asynchronously to avoid blocking the main processing
	 * 
	 * @param userId The user ID (userPin) from the verification
	 */
	public static void notifyVerification(String userId) {
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
				callExternalApi(userId);
			}
		});
		
		logger.info("EXTERNAL API: Async task submitted for userId: " + userId);
	}
	
	/**
	 * Make HTTP POST request to external API with JSON body
	 * 
	 * @param userId The user ID (student_id) to pass to the API
	 */
	private static void callExternalApi(String userId) {
		logger.info("=== EXTERNAL API: callExternalApi started for userId: " + userId + " ===");
		logger.info("EXTERNAL API: URL: " + EXTERNAL_API_URL);
		
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
			
			// Build JSON request body (matching exact curl format)
			String jsonBody = "{\"student_id\": \"" + formattedStudentId + "\", \"meal_type\": \"" + MEAL_TYPE + "\"}";
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
			
			if (responseCode >= 200 && responseCode < 300) {
				logger.info("External API call successful for student_id: " + formattedStudentId + 
					" (original: " + userId + "), Response Code: " + responseCode + ", Response: " + response.toString());
			} else {
				logger.warn("External API call returned status code: " + responseCode + 
					" for student_id: " + formattedStudentId + " (original: " + userId + "), Response: " + response.toString());
			}
			
		} catch (Exception e) {
			logger.error("Error calling external API for student_id: " + userId + 
				", URL: " + EXTERNAL_API_URL + ", Error: " + e.getMessage(), e);
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
	 * Shutdown the executor service (call this on application shutdown)
	 */
	public static void shutdown() {
		if (executorService != null && !executorService.isShutdown()) {
			executorService.shutdown();
		}
	}
}

