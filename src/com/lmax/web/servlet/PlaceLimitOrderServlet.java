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

public class PlaceLimitOrderServlet extends HttpServlet{

	private static Log log = LogFactory.getLog(PlaceOrderServlet.class);
	
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
		String instrumentId = req.getParameter("instrumentId");
		String price = req.getParameter("price");
		String quantity = req.getParameter("quantity");
		PrintWriter out = null;
		try{
			out = resp.getWriter();
			out.print(SessionListener.getInstance().placeLimitOrder(Long.parseLong(instrumentId),price,Long.parseLong(quantity)));
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
