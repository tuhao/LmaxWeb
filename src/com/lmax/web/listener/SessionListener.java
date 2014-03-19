package com.lmax.web.listener;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lmax.api.SessionDisconnectedListener;
import com.lmax.util.Constant;

public class SessionListener implements ServletContextListener,SessionDisconnectedListener {

	private static Log log = LogFactory.getLog(SessionListener.class);
	@Override
	public void contextDestroyed(ServletContextEvent arg0) {
		// TODO Auto-generated method stub
		log.info(arg0);
	}
	
	public static LmaxThread getLmaxThread(){
		return lmaxThread;
	}
	
	private static LmaxThread lmaxThread = null;

	@Override
	public void contextInitialized(ServletContextEvent arg0) {
		// TODO Auto-generated method stub
		initThread();
	}
	
	private void initThread(){
		Properties properties = new Properties();
		try {
			properties.load(SessionListener.class.getResourceAsStream(Constant.LMAX_ACCOUNT));
			String url = properties.getProperty("url");
			String username = properties.getProperty("username");
			String password = properties.getProperty("password");
			String demo = properties.getProperty("demo");
			lmaxThread = new LmaxThread(url, username,password, demo, this);
			lmaxThread.start();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			log.error(e);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			log.error(e);
		}
	}

	@Override
	public void notifySessionDisconnected() {
		// TODO Auto-generated method stub
		initThread();
	}
	
	
	
	
	
	

}
