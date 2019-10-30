package com.epam.chat.listeners;

import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

import com.epam.chat.datalayer.UserDAO;
import com.epam.chat.datalayer.dto.User;

/**
 * 
 * Implementation of a session listener
 * 
 * @author Sergey Vorobyev	
 * @since 09/27/2019
 * @version 1.0
 * 
 */
public class SessionListener implements HttpSessionListener {
    private static final String USER_DAO_CONTEXT = "userDAO";
    private static final String USER_SESSION = "user";

    @Override
    public void sessionCreated(HttpSessionEvent event) {
	// actions when a session created
    }

    @Override
    public void sessionDestroyed(HttpSessionEvent event) {
	HttpSession session = event.getSession();
	User user = (User) session.getAttribute(USER_SESSION);
	UserDAO userDAO = (UserDAO) session.getServletContext()
		.getAttribute(USER_DAO_CONTEXT);
	userDAO.logout(user);
    }
}
