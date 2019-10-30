package com.epam.chat.datalayer.db;

import java.util.List;
import java.util.ResourceBundle;

import com.epam.chat.datalayer.MessageDAO;
import com.epam.chat.datalayer.dto.Message;

/**
 * Implements message DAO and perform the interface tasks
 * 
 * @author Sergey Vorobyev	
 * @since 09/27/2019
 * @version 1.0
 * 
 */
public class OracleMessageDAO implements MessageDAO {
    private static final String SQL_QUERIES_BUNDLE = "sqlQueries";
    private static final String SEND_MESSAGE_BUNDLE = "sendMessage";
    private SQLPerformer performer = new SQLPerformer();
    private ResourceBundle bundle = ResourceBundle
	    .getBundle(SQL_QUERIES_BUNDLE);

    @Override
    public void sendMessage(Message message) {
	String query = bundle.getString(SEND_MESSAGE_BUNDLE);
	performer.createQueryToBase(query, message);
    }

    @Override
    public List<Message> getLast(int count) {
	return performer.getLastMessages(count);
    }
}
