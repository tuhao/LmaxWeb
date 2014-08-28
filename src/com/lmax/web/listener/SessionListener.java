package com.lmax.web.listener;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lmax.api.SessionDisconnectedListener;
import com.lmax.api.orderbook.OrderBookEvent;
import com.lmax.util.ActionCallback;
import com.lmax.util.Constant;
import com.lmax.util.CurrentOrder;

public class SessionListener implements ServletContextListener,SessionDisconnectedListener {

	private static Log log = LogFactory.getLog(SessionListener.class);
	
	private LmaxThread lmaxThread = null;
	
	public Map<Long, OrderBookEvent> orderBookEventMap = Collections.synchronizedMap(new HashMap<Long, OrderBookEvent>());
	
	public Map<String,CurrentOrder> orderMap = Collections.synchronizedMap(new HashMap<String,CurrentOrder>());
	
	public String positionInfo;
	public String accountInfo;
	
	private static SessionListener instance = null;
	
	public static SessionListener getInstance(){
		return instance;
	}
	
	public String placeOrder(long instrumentId,long quantity){
		ActionCallback actionCallback = new ActionCallback();
		lmaxThread.placeOrder(instrumentId, quantity,actionCallback);
		return actionCallback.result;
	}
	
	public String placeLimitOrder(long instrumentId,String price,long quantity){
		ActionCallback actionCallback = new ActionCallback();
		lmaxThread.placeLimiteOrder(instrumentId, price,quantity,actionCallback);
		return actionCallback.result;
	}
	
	public String closeOrder(String instructionId,long instrumentId,long quantity){
		ActionCallback actionCallback = new ActionCallback();
		lmaxThread.closeOrder(instructionId, instrumentId, quantity,actionCallback);
		return actionCallback.result;
	}

	@Override
	public void contextInitialized(ServletContextEvent arg0) {
		// TODO Auto-generated method stub
		instance = this;
		initThread();
	}
	
	@Override
	public void contextDestroyed(ServletContextEvent arg0) {
		// TODO Auto-generated method stub
		log.error(arg0);
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
