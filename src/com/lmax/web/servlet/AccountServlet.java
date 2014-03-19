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

public class AccountServlet extends HttpServlet{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -4650842291355805364L;
	private static Log log = LogFactory.getLog(AccountServlet.class);

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		PrintWriter out = null;
		try{
			out = resp.getWriter();
			String account = SessionListener.getInstance().accountInfo + "\n" + SessionListener.getInstance().positionInfo;
			out.print(account);
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
