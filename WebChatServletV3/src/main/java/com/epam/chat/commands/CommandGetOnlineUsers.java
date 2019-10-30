package com.epam.chat.commands;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.epam.chat.datalayer.UserDAO;
import com.epam.chat.utilities.Utility;

/**
 * 
 * Command for displaying and sending all the online users to the HTML page. *
 * 
 * @author Sergey Vorobyev
 * @since 09/27/2019
 * @version 1.0
 * 
 */
public class CommandGetOnlineUsers implements Command {
    private static final String USER_DAO_CONTEXT = "userDAO";

    @Override
    public void execute(HttpServletRequest req, HttpServletResponse resp) {
	UserDAO userDao = (UserDAO) req.getServletContext()
		.getAttribute(USER_DAO_CONTEXT);
	Map<String, Object> userOnlineList = userDao.getAllLogged();
	Utility.sendForm(resp, userOnlineList);
    }
}
