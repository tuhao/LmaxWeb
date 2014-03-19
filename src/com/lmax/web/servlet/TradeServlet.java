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

public class TradeServlet extends HttpServlet{

	private static final long serialVersionUID = -2863917059754589571L;

	private static Log log = LogFactory.getLog(TradeServlet.class);

	
	@Override
	public void init() throws ServletException {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		String strInstrumentId = req.getParameter("instrumentId");
		PrintWriter out = null;
		try {
			long instrumentId = Long.parseLong(strInstrumentId);
			out = resp.getWriter();
			out.print(SessionListener.getInstance().orderBookEventMap.get(instrumentId));
		} catch (Exception e) {
			// TODO: handle exception
			log.error(e);
		} finally {
			if (out != null) {
				out.flush();
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
