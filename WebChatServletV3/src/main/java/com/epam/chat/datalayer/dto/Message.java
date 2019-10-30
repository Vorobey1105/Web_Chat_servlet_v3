package com.epam.chat.datalayer.dto;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;

/**
 * Message object class.
 * 
 * @author Sergey Vorobyev	
 * @since 09/27/2019
 * @version 1.0
 * 
 */
public class Message {
    private String userFrom;
    private String messageContent;
    private String timeStamp;
    private String status;

    public String getUserFrom() {
	return userFrom;
    }

    public void setUserFrom(String userFrom) {
	this.userFrom = userFrom;
    }

    public String getMessage() {
	return messageContent;
    }

    public void setMessage(String messageContent) {
	this.messageContent = messageContent;
    }

    public String getTimeStamp() {
	return timeStamp;
    }
    
    public String getTimeStampDisplay() {
	Timestamp time = Timestamp.valueOf(timeStamp);
	return new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(time);
    }

    public void setTimeStamp(String timeStamp) {
	this.timeStamp = timeStamp;
    }

    public String getStatus() {
	return status;
    }

    public void setStatus(String status) {
	this.status = status;
    }

    @Override
    public String toString() {
	return "Message [userFrom=" + userFrom + ", messageContent=" 
    + messageContent + ", timeStamp=" + timeStamp
		+ ", status=" + status + "]";
    }

    @Override
    public int hashCode() {
	final int prime = 31;
	int result = 1;
	result = prime * result + ((messageContent == null) 
		? 0 : messageContent.hashCode());
	result = prime * result + ((status == null) ? 0 : status.hashCode());
	result = prime * result + ((timeStamp == null) 
		? 0 : timeStamp.hashCode());
	result = prime * result + ((userFrom == null) 
		? 0 : userFrom.hashCode());
	return result;
    }

    @Override
    public boolean equals(Object obj) {
	if (this == obj)
	    return true;
	if (obj == null)
	    return false;
	if (getClass() != obj.getClass())
	    return false;
	Message other = (Message) obj;
	if (messageContent == null) {
	    if (other.messageContent != null)
		return false;
	} else if (!messageContent.equals(other.messageContent))
	    return false;
	if (status == null) {
	    if (other.status != null)
		return false;
	} else if (!status.equals(other.status))
	    return false;
	if (timeStamp == null) {
	    if (other.timeStamp != null)
		return false;
	} else if (!timeStamp.equals(other.timeStamp))
	    return false;
	if (userFrom == null) {
	    if (other.userFrom != null)
		return false;
	} else if (!userFrom.equals(other.userFrom))
	    return false;
	return true;
    }
}