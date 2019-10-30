package com.epam.chat.datalayer;

import java.sql.SQLException;
import java.util.List;

import com.epam.chat.datalayer.dto.Message;

/**
 * This interface describes methods to access data from DB
 */
public interface MessageDAO {

    /**
     * Send a message
     * 
     * @param message - message to send
     * @throws SQLException
     */
    void sendMessage(Message message) throws SQLException;

    /**
     * Get last number of messages
     * 
     * @param count number of last messages to get (sorted by date)
     * @return
     * @throws SQLException
     */
    List<Message> getLast(int count) throws SQLException;
}
