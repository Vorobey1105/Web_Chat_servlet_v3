package com.epam.chat.commands;

import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.epam.chat.datalayer.MessageDAO;
import com.epam.chat.datalayer.dto.Message;
import com.epam.chat.utilities.Utility;

/**
 * 
 * Command for displaying and sending the last chat messages to a HTML page.
 * LAST_MESSAGES_NUMBER allows configuring the number of massages in the chat.
 * 
 * @author Sergey Vorobyev	
 * @since 09/27/2019
 * @version 1.0
 * 
 */

public class CommandChatMessages implements Command {
    private static final Logger logger = Logger
	    .getLogger(CommandChatMessages.class);
    private static final String MESSAGE_RESPONSE = "message";
    private static final String USER_RESPONSE = "user";
    private static final String DATE_RESPONSE = "date";
    private static final String MESSAGE_DAO_CONTEXT = "messageDAO";
    private static final int LAST_MESSAGES_NUMBER = 50;
    private Map<String, Object> messages = new LinkedHashMap<>();
    
    public List<Message> getMessages(MessageDAO messageDAO) {
	List<Message> messagesList = null;

	try {
	    messagesList = messageDAO.getLast(LAST_MESSAGES_NUMBER);
	} catch (SQLException e) {
	    logger.error("SQL query get last error ", e);
	}

	return messagesList;
    }

    @Override
    public void execute(HttpServletRequest req, HttpServletResponse resp) {
	MessageDAO messageDAO = (MessageDAO) req.getServletContext()
		.getAttribute(MESSAGE_DAO_CONTEXT);
	List<Message> messageSend = getMessages(messageDAO);

	for (int i = 0; i < messageSend.size(); i++) {
	    messages.put(DATE_RESPONSE + i, messageSend.get(i)
		    .getTimeStampDisplay());
	    messages.put(USER_RESPONSE + i, messageSend.get(i).getUserFrom());
	    messages.put(MESSAGE_RESPONSE + i, messageSend.get(i).getMessage());
	}
	
	Utility.sendForm(resp, messages);
    }
}
