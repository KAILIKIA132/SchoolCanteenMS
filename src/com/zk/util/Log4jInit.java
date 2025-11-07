package com.zk.util;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

import org.apache.log4j.PropertyConfigurator;

public class Log4jInit extends HttpServlet{
	private static final long serialVersionUID = 1L;
	 public void destroy() {
	  super.destroy();
	 }

	 public Log4jInit() {
	  super();
	 }

	 /**
	  * Initialization of the servlet. <br>
	  *
	  * @throws ServletException if an error occurs
	  */
	public void init() throws ServletException {
		String prefix = this.getServletContext().getRealPath("/"); 
        String file = this.getInitParameter("log4j"); 
        String filePath = prefix + file; 
        Properties props = new Properties(); 
        try { 
            FileInputStream istream = new FileInputStream(filePath); 
            props.load(istream); 
            istream.close(); 
            //toPrint(props.getProperty("log4j.appender.file.File")); 
            String logFile = prefix + props.getProperty("log4j.appender.file.File");//Sets the path
            props.setProperty("log4j.appender.file.File",logFile); 
            PropertyConfigurator.configure(props);//Loads the configuration of log4j
        } catch (IOException e) { 
//            toPrint("Could not read configuration file [" + filePath + "]."); 
//            toPrint("Ignoring configuration file [" + filePath + "]."); 
            return; 
        } 
	}
}
