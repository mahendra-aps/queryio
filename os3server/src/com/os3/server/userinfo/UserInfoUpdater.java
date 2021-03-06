package com.os3.server.userinfo;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.queryio.common.QueryIOConstants;
import com.queryio.common.util.SecurityHandler;

public class UserInfoUpdater extends HttpServlet {
	
	protected static final Logger logger = Logger.getLogger(UserInfoUpdater.class);
	
	private static final long serialVersionUID = 1L;

	public UserInfoUpdater() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		process(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		process(request, response);
	}
	
	protected void process(HttpServletRequest request, HttpServletResponse response) throws IOException{
		logger.debug("User information update request received");
		
		String userInfo = request.getHeader(QueryIOConstants.USER_INFO_REQUEST_HEADER_KEY);
		
		if(userInfo==null){
			response.sendError(HttpServletResponse.SC_BAD_REQUEST, QueryIOConstants.USER_INFO_REQUEST_HEADER_KEY + " header not received");
			return;
		}
		
		try{
			UserInfoContainer.setUserInfo(SecurityHandler.decryptData(userInfo));
		} catch (Exception e){
			logger.fatal(e.getMessage(), e);
			response.sendError(HttpServletResponse.SC_BAD_REQUEST, "OS3 Server User Information could not be updated");
			return;
		}
		
		logger.debug("User Information updated");
	}
}
