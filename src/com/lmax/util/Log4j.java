package com.lmax.util;


import javax.servlet.http.HttpServlet;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class Log4j extends HttpServlet {

	
	
	public static void main(String[] args) {
		Log logger = LogFactory.getLog(Log4j.class);

		// These requests will be enabled.
		logger.info("This is an info.");
		logger.warn("This is a warning.");
		logger.error("This is an error.");
		logger.fatal("This is a fatal error.");
	}

}
