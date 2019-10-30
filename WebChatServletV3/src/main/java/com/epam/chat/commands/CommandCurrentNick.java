package com.epam.chat.commands;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.epam.chat.datalayer.dto.User;
import com.epam.chat.utilities.Utility;

/**
 * 
 * Command for displaying and sending the current user nick to HTML page.
 * 
 * @author Sergey Vorobyev	
 * @since 10/30/2019
 * @version 1.0
 * 
 */

public class CommandCurrentNick implements Command {
    private Map<String, Object> userNick = new HashMap<>();

    @Override
    public void execute(HttpServletRequest req, HttpServletResponse resp) {
	HttpSession session = req.getSession();
	User user = (User) session.getAttribute("user");
	String nick = user.getNick();
	userNick.put("loginUser", nick);
	Utility.sendForm(resp, userNick);
    }
}