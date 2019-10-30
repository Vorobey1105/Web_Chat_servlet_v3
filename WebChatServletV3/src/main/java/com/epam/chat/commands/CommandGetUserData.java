package com.epam.chat.commands;

import java.io.File;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.epam.chat.datalayer.UserDAO;
import com.epam.chat.utilities.Utility;

/**
 * 
 * Command for retrieving user data and sending it to a JSP page. Tries to find
 * an image of a user in the folder. When the search is successful, sends the
 * name of an image. The search is restricted by the extensions written in
 * "getPermittedImageExtensions" class. Alternatively, the "default" picture if
 * the "found file" String save its value.
 *
 * @author Sergey Vorobyev
 * @since 09/27/2019
 * @version 1.0
 * 
 */
public class CommandGetUserData implements Command {
    private static final String USER_IMAGE_RESP = "userImage";
    private static final String PATH_DELIMITER = "/";
    private static final String TOMCAT_FOLDER = "catalina.base";
    private static final String UPLOAD_DIRECTORY_CONTEXT = "uploadDirectory";
    private static final String DEFAULT_USER_IMAGE = "defaultUser.bmp";
    private static final String USER_CONTEXT = "user";
    private static final String USER_DAO_CONTEXT = "userDAO";
    private static final String DELIMITER_FILE_NAME = PATH_DELIMITER;
    private static final String FILE_NOT_FOUND_INDICATOR = "fileNotFound";
    private String foundFile = FILE_NOT_FOUND_INDICATOR;
    private List<String> extensions = Utility.getPermittedImageExtensions();

    @Override
    public void execute(HttpServletRequest req, HttpServletResponse resp) {
	String userImage;
	UserDAO userDao = (UserDAO) req.getServletContext()
		.getAttribute(USER_DAO_CONTEXT);
	String user = req.getParameter(USER_CONTEXT);
	foundFile = checkUserImageExistence(user, req);

	if (foundFile.equals(FILE_NOT_FOUND_INDICATOR)) {
	    userImage = DEFAULT_USER_IMAGE;
	} else {
	    userImage = foundFile;
	}

	Map<String, Object> userData = userDao.getUserData(user);
	userData.put(USER_IMAGE_RESP, userImage);
	Utility.sendForm(resp, userData);
    }

    public String checkUserImageExistence(String user, HttpServletRequest req) {
	for (Iterator<String> iterator = extensions.iterator(); 
		iterator.hasNext();) {
	    String curentextension = iterator.next();
	    File file = new File(System.getProperty(TOMCAT_FOLDER) 
		    + PATH_DELIMITER + req.getServletContext()
		    	.getInitParameter(UPLOAD_DIRECTORY_CONTEXT) 
		    		+ DELIMITER_FILE_NAME + user + curentextension);

	    if (file.exists()) {
		foundFile = user + curentextension;
		break;
	    }
	    
	}
	return foundFile;
    }
}
