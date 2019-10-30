package com.epam.chat.commands;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import com.epam.chat.utilities.Utility;

/**
 * 
 * Command for checking user session. Sends "false" if a session "not exists" 
 * and "true" if "exists".
 * 
 *
 * @author Sergey Vorobyev
 * @since 10/30/2019
 * @version 1.0
 * 
 */

public class CommandSessionCheck implements Command {

    private static final String SESSION_EXISTS_SERVER_ANSWER = "sessionExists";
    private Map<String, Object> serverAnswer = new HashMap<>();

    @Override
    public void execute(HttpServletRequest req, HttpServletResponse resp) {
	HttpSession session = req.getSession(false);
	boolean sessionExists = false;
	
	if (session == null) {
	    serverAnswer.put(SESSION_EXISTS_SERVER_ANSWER, sessionExists);
	} else {
	    sessionExists = true;
	    serverAnswer.put(SESSION_EXISTS_SERVER_ANSWER, sessionExists);
	}
	
	Utility.sendForm(resp, serverAnswer);
    }
}