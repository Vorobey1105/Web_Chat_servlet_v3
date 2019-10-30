package com.epam.chat.commands;

import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.epam.chat.datalayer.UserDAO;
import com.epam.chat.datalayer.dto.User;
import com.epam.chat.utilities.Utility;

/**
 * 
 * Command for implementing user logout. Destroys user's session and sends URL
 * to the main page to AJAX.
 * 
 * @author Sergey Vorobyev	
 * @since 09/27/2019
 * @version 1.0
 * 
 */
public class CommandLogout implements Command {
    private static final String URL_LOGOUT_PAGE_REDIRECT = "URLLogout";
    private static final String USER_SESSION = "user";
    private static final String USER_DAO_CONTEXT = "userDAO";
    private Map<String, Object> loginAccess = new HashMap<>();

    @Override
    public void execute(HttpServletRequest req, HttpServletResponse resp) {
	UserDAO userDao = (UserDAO) req.getServletContext()
		.getAttribute(USER_DAO_CONTEXT);
	User user = (User) req.getSession().getAttribute(USER_SESSION);	
	userDao.logout(user);
	HttpSession session = req.getSession();
	session.invalidate();
	loginAccess.put(URL_LOGOUT_PAGE_REDIRECT, Utility.getBaseUrl(req));
	Utility.sendForm(resp, loginAccess);
    }
}
