package com.pimolari;

import java.io.BufferedReader;
import java.io.IOException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
/**
 *  Testing ew servlet 0.3
 * */
@SuppressWarnings("serial")
public class EwatererServlet extends HttpServlet {
	
	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		
		System.out.println("Get request!");
		resp.setContentType("text/plain");
		resp.getWriter().println("Message from server");
		
		resp.flushBuffer();
	}
	
	public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		System.out.println("Post request!");
		resp.setContentType("text/plain");
		resp.getWriter().println("Sync OK");
		
		StringBuilder buffer = new StringBuilder();
	    BufferedReader reader = req.getReader();
	    String line;
	    while ((line = reader.readLine()) != null) {
	        buffer.append(line);
	    }
	    String data = buffer.toString();
		
		//String hvalue = req.getParameter("hvalue");
		//String ahvalue = (String)req.getAttribute("hvalue");
		System.out.println("Humidity retrieved: " + data);
		resp.flushBuffer();
	}
}
