package com.epam.chat.commands;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.jsoup.Jsoup;
import org.jsoup.safety.Whitelist;

import com.epam.chat.datalayer.UserDAO;
import com.epam.chat.datalayer.db.OracleUserDAO;
import com.epam.chat.datalayer.dto.User;
import com.epam.chat.utilities.Utility;

/**
 * 
 * Command for handling the user login process. Checks whether a user exists in
 * the base and sends redirect information to AJAX. Depending on the result of a
 * particular user inquiry, sends error or redirect to the chat page. Creates
 * the session for a user in case of success. Sends login message only if the
 * last message has the status of "MESSAGE" or "LOGIN"
 * 
 * @author Sergey Vorobyev	
 * @since 09/27/2019
 * @version 1.0
 * 
 */
public class CommandLogin implements Command {
    private static final String CHAT_PAGE = "chat";
    private static final String USER_PASS_REQUEST = "userPassword";
    private static final String USERNAME_REQUEST = "username";
    private static final String USER_DAO_CONTEXT = "userDAO";
    private static final String URL_LOGIN_RESPONSE = "URLLogin";
    private static final String USER_ROLE_NUMBER = "2";
    private static final String ADMIN_ROLE_NUMBER = "1";
    private static final String ADMIN_ROLE = "ADMIN";
    private static final String IS_VALID_RESPONSE = "isValid";
    private static final String USER_SESSION_SET = "user";
    private boolean isValid;
    private boolean loginPermission;
    private Map<String, Object> loginAccess = new HashMap<>();
    private Map<String, Object> errorLoginMap = new HashMap<>();

    @Override
    public void execute(HttpServletRequest req, HttpServletResponse resp) {
	UserDAO userDao = (UserDAO) req.getServletContext()
		.getAttribute(USER_DAO_CONTEXT);
	String userToLogin = Jsoup.clean(req.getParameter(USERNAME_REQUEST)
		.toLowerCase(), Whitelist.none());
	String userPassword = Jsoup.clean(req
		.getParameter(USER_PASS_REQUEST), Whitelist.none()); 
	loginPermission = loginPermission(userToLogin, userPassword, userDao);
	
	if (loginPermission && userToLogin.trim().length() != 0 && userPassword
		.trim().length() != 0) {
	    User user = new User();
	    user.setNick(userToLogin);

	    if (ADMIN_ROLE.equals(((OracleUserDAO) userDao).getRole(user)
		    .getTitle())) {
		user.setRole(ADMIN_ROLE_NUMBER);
	    } else {
		user.setRole(USER_ROLE_NUMBER);
	    }

	    HttpSession session = req.getSession();
	    session.setAttribute(USER_SESSION_SET, user);

	    if (userDao.isLoggedIn(user) == false) {
		userDao.login(user);
	    }

	    isValid = true;
	    loginAccess.put(IS_VALID_RESPONSE, isValid);
	    loginAccess.put(URL_LOGIN_RESPONSE, CHAT_PAGE);
	    Utility.sendForm(resp, loginAccess);

	} else {
	    isValid = false;
	    errorLoginMap.put(IS_VALID_RESPONSE, isValid);
	    Utility.sendForm(resp, errorLoginMap);
	}
    }

    private boolean loginPermission(String user, String password, 
	    UserDAO userDao) {
	boolean permission = false;
	boolean validPassword = false;
	boolean validLogin = false;
	validPassword = userDao.passwordCheck(user, password);
	validLogin = userDao.userNickCheck(user);

	if (validPassword && validLogin) {
	    permission = true;
	}
	return permission;
    }
}
