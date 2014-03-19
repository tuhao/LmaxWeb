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

public class CloseOrderServlet extends HttpServlet{

	/**
	 * 
	 */
	private static final long serialVersionUID = 2115819853096793972L;
	
	private static Log log = LogFactory.getLog(CloseOrderServlet.class);

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		doPost(req, resp);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		String instructionId = req.getParameter("instructionId");
		String instrumentId = req.getParameter("instrumentId");
		String quantity = req.getParameter("quantity");
		PrintWriter out = null;
		try{
			out = resp.getWriter();
			out.print(SessionListener.getLmaxThread().closeOrder(instructionId, Long.parseLong(instrumentId), Long.parseLong(quantity)));
		}catch (Exception e) {
			// TODO: handle exception
			log.error(e);
		}finally{
			if(out != null){
				out.close();
			}
		}
	}

	
}
