package com.zk.action;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.ServletResponseAware;

import com.zk.dao.impl.ApiVerificationReport;
import com.zk.dao.impl.ApiVerificationReportDao;
import com.zk.util.PagenitionUtil;

public class VerificationReportAction implements ServletRequestAware, ServletResponseAware {
	private static Logger logger = Logger.getLogger(VerificationReportAction.class);
	private HttpServletRequest request;
	private HttpServletResponse response;
	private List<ApiVerificationReport> reportList;
	private int successCount;
	private int failedCount;
	private int totalCount;
	
	public void setServletRequest(HttpServletRequest request) {
		this.request = request;
	}
	
	public void setServletResponse(HttpServletResponse response) {
		this.response = response;
	}
	
	/**
	 * Get verification reports list
	 * @return
	 */
	public String reportList() {
		try {
			String status = request.getParameter("status");
			String userPin = request.getParameter("userPin");
			String startDate = request.getParameter("startDate");
			String endDate = request.getParameter("endDate");
			String pageStr = request.getParameter("page");
			int page = 1;
			if (pageStr != null && !pageStr.isEmpty()) {
				try {
					page = Integer.parseInt(pageStr);
					if (page < 1) page = 1;
				} catch (NumberFormatException e) {
					page = 1;
				}
			}
			
			// Build query condition
			StringBuilder cond = new StringBuilder();
			if (status != null && !status.isEmpty() && !"ALL".equals(status)) {
				cond.append(" and status = '").append(status).append("'");
			}
			if (userPin != null && !userPin.isEmpty()) {
				cond.append(" and user_pin = '").append(userPin).append("'");
			}
			if (startDate != null && !startDate.isEmpty()) {
				cond.append(" and DATE(api_call_time) >= '").append(startDate).append("'");
			}
			if (endDate != null && !endDate.isEmpty()) {
				cond.append(" and DATE(api_call_time) <= '").append(endDate).append("'");
			}
			
			// Get counts
			ApiVerificationReportDao dao = new ApiVerificationReportDao();
			totalCount = dao.fetchCount(cond.toString());
			successCount = dao.fetchCount(cond.toString() + " and status = 'SUCCESS'");
			failedCount = dao.fetchCount(cond.toString() + " and status = 'FAILED'");
			
			// Get paginated list
			int pageSize = 50;
			int startRec = (page - 1) * pageSize;
			String limitCond = cond.toString() + " limit " + startRec + ", " + pageSize;
			reportList = dao.query(limitCond);
			
			// Set pagination info
			request.setAttribute("currentPage", page);
			request.setAttribute("totalPages", (totalCount + pageSize - 1) / pageSize);
			request.setAttribute("pageSize", pageSize);
			request.setAttribute("statusFilter", status != null ? status : "ALL");
			request.setAttribute("userPinFilter", userPin != null ? userPin : "");
			request.setAttribute("startDateFilter", startDate != null ? startDate : "");
			request.setAttribute("endDateFilter", endDate != null ? endDate : "");
			
			dao.close();
		} catch (Exception e) {
			logger.error("Error getting verification reports: " + e.getMessage(), e);
		}
		return "reportList";
	}
	
	public List<ApiVerificationReport> getReportList() {
		return reportList;
	}
	
	public void setReportList(List<ApiVerificationReport> reportList) {
		this.reportList = reportList;
	}
	
	public int getSuccessCount() {
		return successCount;
	}
	
	public void setSuccessCount(int successCount) {
		this.successCount = successCount;
	}
	
	public int getFailedCount() {
		return failedCount;
	}
	
	public void setFailedCount(int failedCount) {
		this.failedCount = failedCount;
	}
	
	public int getTotalCount() {
		return totalCount;
	}
	
	public void setTotalCount(int totalCount) {
		this.totalCount = totalCount;
	}
	
	/**
	 * Export verification reports to Excel with selected columns
	 * @return
	 */
	public String exportExcel() {
		try {
			// Get filter parameters
			String status = request.getParameter("status");
			String userPin = request.getParameter("userPin");
			String startDate = request.getParameter("startDate");
			String endDate = request.getParameter("endDate");
			
			// Get selected columns
			String[] selectedColumns = request.getParameterValues("columns");
			Set<String> columnSet = new HashSet<String>();
			if (selectedColumns != null && selectedColumns.length > 0) {
				columnSet.addAll(Arrays.asList(selectedColumns));
			} else {
				// Default: all columns selected
				columnSet.addAll(Arrays.asList("id", "userPin", "userName", "studentId", "verificationTime", 
					"apiCallTime", "mealType", "status", "responseCode", "responseMessage", "errorMessage", "apiUrl"));
			}
			
			// Build query condition (same as reportList)
			StringBuilder cond = new StringBuilder();
			if (status != null && !status.isEmpty() && !"ALL".equals(status)) {
				cond.append(" and status = '").append(status).append("'");
			}
			if (userPin != null && !userPin.isEmpty()) {
				cond.append(" and user_pin = '").append(userPin).append("'");
			}
			if (startDate != null && !startDate.isEmpty()) {
				cond.append(" and DATE(api_call_time) >= '").append(startDate).append("'");
			}
			if (endDate != null && !endDate.isEmpty()) {
				cond.append(" and DATE(api_call_time) <= '").append(endDate).append("'");
			}
			
			// Get all reports (no pagination for export)
			ApiVerificationReportDao dao = new ApiVerificationReportDao();
			List<ApiVerificationReport> allReports = dao.query(cond.toString());
			dao.close();
			
			// Generate Excel
			Workbook workbook = new XSSFWorkbook();
			Sheet sheet = workbook.createSheet("Verification Reports");
			
			// Create header style
			CellStyle headerStyle = workbook.createCellStyle();
			Font headerFont = workbook.createFont();
			headerFont.setBold(true);
			headerFont.setFontHeightInPoints((short) 12);
			headerStyle.setFont(headerFont);
			headerStyle.setFillForegroundColor(org.apache.poi.ss.usermodel.IndexedColors.GREY_25_PERCENT.getIndex());
			headerStyle.setFillPattern(org.apache.poi.ss.usermodel.FillPatternType.SOLID_FOREGROUND);
			
			// Create header row
			Row headerRow = sheet.createRow(0);
			int colIndex = 0;
			List<String> columnOrder = new ArrayList<String>();
			
			// Define column order and headers
			String[][] columnDefs = {
				{"id", "ID"},
				{"userPin", "User PIN"},
				{"userName", "User Name"},
				{"studentId", "Student ID"},
				{"verificationTime", "Verification Time"},
				{"apiCallTime", "API Call Time"},
				{"mealType", "Meal Type"},
				{"status", "Status"},
				{"responseCode", "Response Code"},
				{"responseMessage", "Response Message"},
				{"errorMessage", "Error Message"},
				{"apiUrl", "API URL"}
			};
			
			for (String[] colDef : columnDefs) {
				if (columnSet.contains(colDef[0])) {
					Cell cell = headerRow.createCell(colIndex++);
					cell.setCellValue(colDef[1]);
					cell.setCellStyle(headerStyle);
					columnOrder.add(colDef[0]);
				}
			}
			
			// Create data rows
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			int rowIndex = 1;
			for (ApiVerificationReport report : allReports) {
				Row row = sheet.createRow(rowIndex++);
				colIndex = 0;
				
				for (String colName : columnOrder) {
					Cell cell = row.createCell(colIndex++);
					
					switch (colName) {
						case "id":
							cell.setCellValue(report.getReportId());
							break;
						case "userPin":
							cell.setCellValue(report.getUserPin() != null ? report.getUserPin() : "");
							break;
						case "userName":
							cell.setCellValue(report.getUserName() != null ? report.getUserName() : "");
							break;
						case "studentId":
							cell.setCellValue(report.getStudentId() != null ? report.getStudentId() : "");
							break;
						case "verificationTime":
							cell.setCellValue(report.getVerificationTime() != null ? report.getVerificationTime() : "");
							break;
						case "apiCallTime":
							if (report.getApiCallTime() != null) {
								cell.setCellValue(dateFormat.format(report.getApiCallTime()));
							}
							break;
						case "mealType":
							cell.setCellValue(report.getMealType() != null ? report.getMealType() : "");
							break;
						case "status":
							cell.setCellValue(report.getStatus() != null ? report.getStatus() : "");
							break;
						case "responseCode":
							if (report.getResponseCode() != null) {
								cell.setCellValue(report.getResponseCode());
							}
							break;
						case "responseMessage":
							cell.setCellValue(report.getResponseMessage() != null ? report.getResponseMessage() : "");
							break;
						case "errorMessage":
							cell.setCellValue(report.getErrorMessage() != null ? report.getErrorMessage() : "");
							break;
						case "apiUrl":
							cell.setCellValue(report.getApiUrl() != null ? report.getApiUrl() : "");
							break;
					}
				}
			}
			
			// Auto-size columns
			for (int i = 0; i < columnOrder.size(); i++) {
				sheet.autoSizeColumn(i);
			}
			
			// Write to response
			response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
			response.setHeader("Content-Disposition", "attachment; filename=verification_reports_" + 
				new SimpleDateFormat("yyyyMMdd_HHmmss").format(new java.util.Date()) + ".xlsx");
			
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			workbook.write(out);
			workbook.close();
			
			response.getOutputStream().write(out.toByteArray());
			response.getOutputStream().flush();
			response.getOutputStream().close();
			
			return "success"; // Return success - response already written
		} catch (Exception e) {
			logger.error("Error exporting Excel: " + e.getMessage(), e);
			try {
				response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error generating Excel file");
			} catch (IOException ioException) {
				logger.error("Error sending error response: " + ioException.getMessage(), ioException);
			}
			return "success";
		}
	}
}



