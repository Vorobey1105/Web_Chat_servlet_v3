package com.epam.chat.commands;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.safety.Whitelist;

import com.epam.chat.datalayer.MessageDAO;
import com.epam.chat.datalayer.dto.Message;
import com.epam.chat.datalayer.dto.User;
import com.epam.chat.utilities.Utility;

/**
 * 
 * Command for sending messages. It gets a message from the HTML chat page and
 * writes it in the SQL base.
 * 
 * @author Sergey Vorobyev	
 * @since 09/27/2019
 * @version 1.0
 * 
 */
public class CommandMessage implements Command {
    private static final Logger logger = Logger.getLogger(CommandMessage.class);
    private static final String MESSAGE_SENT_RESPONSE = "messageSent";  
    private static final String MESSAGE_TO_SEND_REQUEST = "messageToSend";
    private static final String USER_SESSION = "user";
    private static final String MESSAGE_DAO_CONTEXT = "messageDAO";
    private Map<String, Object> serverAnswer = new HashMap<>();
    private Message message = new Message();

    @Override
    public void execute(HttpServletRequest req, HttpServletResponse resp) {
	MessageDAO messageDAO = (MessageDAO) req.getServletContext()
		.getAttribute(MESSAGE_DAO_CONTEXT);
	User user = (User) req.getSession().getAttribute(USER_SESSION);
	String messageSend = Jsoup.clean(req
		.getParameter(MESSAGE_TO_SEND_REQUEST), Whitelist.basic());	
	message.setUserFrom(user.getNick());
	message.setMessage(messageSend);

	try {
	    messageDAO.sendMessage(message);
	} catch (SQLException e) {
	    logger.error("Sending message error ", e);
	}

	boolean messageSent = true;
	serverAnswer.put(MESSAGE_SENT_RESPONSE, messageSent);
	Utility.sendForm(resp, serverAnswer);
    }
}
