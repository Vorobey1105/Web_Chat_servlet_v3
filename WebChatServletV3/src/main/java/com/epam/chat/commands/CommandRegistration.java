package com.epam.chat.commands;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Part;

import org.apache.commons.io.FilenameUtils;
import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.safety.Whitelist;

import com.epam.chat.datalayer.UserDAO;
import com.epam.chat.datalayer.dto.User;
import com.epam.chat.utilities.Utility;

/**
 * 
 * Command for implementing a user registration. It gets data from
 * registration.jsp and check whether a nick is free. Return false if it is
 * already in use (not found in the base). In the case of success write new the
 * new user to the base and gets him "User" role. It also receives the user's
 * image and writes it to "uploadDirectory".
 *
 * @author Sergey Vorobyev
 * @since 09/27/2019
 * @version 1.0
 * 
 */
public class CommandRegistration implements Command {
    private static final String TOMCAT_FOLDER = "catalina.base";
    private static final String PATH_DELIMITER = "/";
    private static final Logger logger = Logger
	    .getLogger(CommandRegistration.class);
    private static final String EMAIL_REQUEST = "email";
    private static final String PHONE_NUMBER_REQUEST = "PhoneNumber";
    private static final String FULL_NAME_REQUEST = "fullName";
    private static final String USER_PASS_REQUEST = "userPassword";
    private static final String USER_ROLE_NUMBER = "2";
    private static final String DOT_FILE_NAME = ".";
    private static final String UPLOAD_DIRECTORY = "uploadDirectory";
    private static final String FILE_PART_REQUEST = "file";
    private static final String CHAT_PAGE = "chat";
    private static final String URL_LOGIN_PAGE = "URLLogin";
    private static final String PASSWORD_EMPTY = "";
    private static final String USERNAME_REQUEST = "username";
    private static final String USER_DAO_CONTEXT = "userDAO";
    private static final String IS_NICK_IN_USE_INDICATOR = "isNickInUse";
    private static final String USER = "user";
    private static UserDAO userDao;
    private Map<String, Object> loginAccess = new HashMap<>();
    private Map<String, Object> error = new HashMap<>();

    @Override
    public void execute(HttpServletRequest req, HttpServletResponse resp) {
	userDao = (UserDAO) req.getServletContext()
		.getAttribute(USER_DAO_CONTEXT);
	String userToLogin = Jsoup.clean(req.getParameter(USERNAME_REQUEST)
		.toLowerCase(), Whitelist.none());
	boolean isNickInUse = userDao.userNickCheck(userToLogin);
	if (isNickInUse) {
	    error.put(IS_NICK_IN_USE_INDICATOR, isNickInUse);
	    Utility.sendForm(resp, error);
	} else {
	    User newUser = getNewUser(req, userToLogin);
	    userDao.addNewUserToBase(newUser);
	    newUser.setPassword(PASSWORD_EMPTY);
	    saveUserImage(req, newUser);
	    HttpSession session = req.getSession();
	    session.setAttribute(USER, newUser);
	    userDao.login(newUser);
	    loginAccess.put(IS_NICK_IN_USE_INDICATOR, isNickInUse);
	    loginAccess.put(URL_LOGIN_PAGE, CHAT_PAGE);
	    Utility.sendForm(resp, loginAccess);
	}
    }

    private void saveUserImage(HttpServletRequest req, User user) {
	try {
	    Part image = req.getPart(FILE_PART_REQUEST);
	    String fileName = Paths.get(image
		    .getSubmittedFileName()).getFileName().toString();
	    String extension = FilenameUtils.getExtension(fileName)
		    .toLowerCase();
	    Path folder = FileSystems.getDefault().getPath(
		    System.getProperty(TOMCAT_FOLDER) + PATH_DELIMITER
			    + req.getServletContext()
			    	.getInitParameter(UPLOAD_DIRECTORY),
			    		user.getNick() + DOT_FILE_NAME 
			    			+ extension);
	    fileCopyFolder(image, folder);
	} catch (IOException | ServletException e) {
	    logger.error("Save user image error ", e);
	}
    }

    private void fileCopyFolder(Part image, Path folder) {
	try (InputStream is = image.getInputStream()) {
	    Files.copy(is, folder);
	} catch (IOException e) {
	    logger.error("File copy to folder error ", e);
	}
    }

    public User getNewUser(HttpServletRequest req, String userToLogin) {
	User newUser = new User();
	newUser.setNick(userToLogin);
	newUser.setRole(USER_ROLE_NUMBER);
	newUser.setPassword(Jsoup.clean(req.getParameter(USER_PASS_REQUEST)
		, Whitelist.none()));
	newUser.setFullName(new String (req.getParameter(FULL_NAME_REQUEST)
		.getBytes (StandardCharsets.ISO_8859_1)
			, StandardCharsets.UTF_8));
	newUser.setFullName(Jsoup.clean(newUser.getFullName()
		, Whitelist.none()));
	newUser.setTelephoneNumber(Jsoup.clean(req
		.getParameter(PHONE_NUMBER_REQUEST), Whitelist.none()));
	newUser.setEmail(Jsoup.clean(req.getParameter(EMAIL_REQUEST)
		, Whitelist.none()));
	return newUser;
    }
}