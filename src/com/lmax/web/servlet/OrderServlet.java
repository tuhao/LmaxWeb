package com.lmax.web.servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lmax.web.listener.SessionListener;

public class OrderServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4030844191056362563L;
	
	private static Log log = LogFactory.getLog(OrderServlet.class);

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		PrintWriter out = null;
		try{
			StringBuffer sb = new StringBuffer("");
			for(String key:SessionListener.getLmaxThread().orderMap.keySet()){
				sb.append(SessionListener.getLmaxThread().orderMap.get(key)).append("\n");
			}
			out = resp.getWriter();
			out.print(sb.toString());
		}catch (Exception e) {
			// TODO: handle exception
			log.error(e);
		}finally{
			if(out != null){
				out.close();
			}
		}
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		super.doPost(req, resp);
	}
	
	

}
