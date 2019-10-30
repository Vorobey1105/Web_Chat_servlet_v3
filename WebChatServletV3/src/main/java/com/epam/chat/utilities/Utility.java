package com.epam.chat.utilities;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.google.gson.Gson;


/**
 * Utility class with static methods
 * 
 * @author Sergey Vorobyev	
 * @since 09/27/2019
 * @version 1.0
 * 
 */
public final class Utility {
    private static final String PATH_COLON = ":";
    private static final String EMPTY_RESULT = "";
    private static final String PATH_DOUBLE_SLASH = "://";
    private static final Logger logger = Logger
	    .getLogger(Utility.class);
    private static final String ICO_EXTENTION = ".ico";
    private static final String BMP_EXTENTION = ".bmp";
    private static final String JPEG_EXTENTION = ".jpeg";
    private static final String JPG_EXTENTION = ".jpg";
    private static final String UTF_8_ENCODING = "UTF-8";
    private static final String APPLICATION_JSON_CONTENT_TYPE = 
	    "application/json";
    
    private Utility() {
        throw new UnsupportedOperationException();
    }

    /**
     * 
     * Sends the form to JSP pages.
     * @param HttpServletResponse,  Map<String, Object>
     * 
     */
    public static void sendForm(HttpServletResponse resp, 
	    Map<String, Object> map) {
	resp.setContentType(APPLICATION_JSON_CONTENT_TYPE);
	resp.setCharacterEncoding(UTF_8_ENCODING);
	try {
	    resp.getWriter().write(new Gson().toJson(map));
	} catch (IOException e) {
	    logger.error("Writing to JSON error ", e);
	}
    }
    
    /**
     * 
     * Contains permitted extensions for using on the chat page
     * 
     */
    public static List<String> getPermittedImageExtensions() {
	List<String> extensions = new ArrayList<>();
	extensions.add(JPG_EXTENTION);
	extensions.add(JPEG_EXTENTION);
	extensions.add(BMP_EXTENTION);
	extensions.add(ICO_EXTENTION);
	return extensions;
    }
    
    /**
     * 
     * Helps to get the current URL of the Web application
     * 
     */
    public static String getBaseUrl(HttpServletRequest request) {
	    String scheme = request.getScheme() + PATH_DOUBLE_SLASH;
	    String serverName = request.getServerName();
	    String serverPort = (request.getServerPort() == 80) ? EMPTY_RESULT 
		    : PATH_COLON + request.getServerPort();
	    String contextPath = request.getContextPath();
	    return scheme + serverName + serverPort + contextPath;
	  }
}
