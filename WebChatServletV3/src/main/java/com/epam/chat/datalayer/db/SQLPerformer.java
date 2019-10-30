package com.epam.chat.datalayer.db;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import org.apache.log4j.Logger;

import com.epam.chat.datalayer.dto.Message;
import com.epam.chat.datalayer.dto.User;
import com.epam.chat.datalayer.dto.UserRole;

/**
 * Implements the all the work with the database. Gets queries from property
 * files and returns data to OracleMessegeDAO and OracleUserDAO
 * 
 * @author Sergey Vorobyev
 * @since 09/27/2019
 * @version 1.0
 * 
 */
public class SQLPerformer {
    private static final String EMAIL_MAP_NAME = "email";
    private static final String TELEPHONE_MAP_NAME = "telephone";
    private static final String FULL_NAME_MAP_NAME = "fullName";
    private static final String USER_MAP_NAME = "user";
    private static final Logger logger = Logger.getLogger(SQLPerformer.class);
    private static final String GET_ROLE_BUNDLE = "getRole";
    private static final String GET_ALL_LOGGED_BUNDLE = "getAllLoggedUsers";
    private static final String ADD_NEW_USER_BUNDLE = "addNewUser";
    private static final String GET_USER_DATA_BUNDLE = "getUserData";
    private static final String ADD_NEW_PASS_BUNDLE = "addPassword";
    private static final String GET_LAST_STATUS_BUNDLE = "getLastStatus";
    private static final String GET_LAST_USER_MESSAGE_BUNDLE = 
	    "getLastUserMessage";
    private static final String GET_USER_PASS_BUNDLE = "checkPassword";
    private static final String GET_LAST_MESSAGES_BUNDLE = "getLastMessages";
    private static final String CHECK_USER_NICK_BUNDLE = "checkUserNick";
    private static final String DATA_ADDED_SIGN = "Done!!! Initial data added!";
    private static final String TABLE_CREATED_SIGN = "Done!!! Table created!";
    private static final String TEST_DATA_QUERIES = "resources\\test_data.sql";
    private static final String CREATE_TABLE_QUERIES = 
	    "resources\\create_db.sql";
    private static final String SQL_QUERIES_BUNDLE = "sqlQueries";
    private static final int FIRST_VARIABLE_PREPARED_STATEMENT = 1;
    private static final int SECOND_VARIABLE_PREPARED_STATEMENT = 2;
    private static final int THIRD_VARIABLE_PREPARED_STATEMENT = 3;
    private static final int FOURTH_VARIABLE_PREPARED_STATEMENT = 4;
    private static final int FIFTH_VARIABLE_PREPARED_STATEMENT = 5;
    private ResourceBundle bundle = ResourceBundle
	    .getBundle(SQL_QUERIES_BUNDLE);

    public void createInitialTables() {
	try (BufferedReader bufferedReader = 
		new BufferedReader(new FileReader(CREATE_TABLE_QUERIES))) {
	    while (bufferedReader.ready()) {
		createTable(bufferedReader.readLine());
	    }
	} catch (IOException e) {
	    logger.error("Creation SQL initial tables error ", e);
	}
    }

    public void createInitialData() {
	try (BufferedReader bufferedReader = 
		new BufferedReader(new FileReader(TEST_DATA_QUERIES))) {
	    while (bufferedReader.ready()) {
		createData(bufferedReader.readLine());
	    }
	} catch (IOException e) {
	    logger.error("Creation SQL initial data error ", e);
	}
    }

    public void createTable(String query) {
	try (Connection connection = OracleConnectionFactory.getConnection()) {
	    try (Statement statement = connection.createStatement()) {
		try (ResultSet messageResult = statement.executeQuery(query)) {
		    logger.info(TABLE_CREATED_SIGN);
		}
	    }
	} catch (SQLException e) {
	    logger.error("Creation SQL table error ", e);
	}
    }

    public void createData(String query) {
	try (Connection connection = OracleConnectionFactory.getConnection()) {
	    try (Statement statement = connection.createStatement()) {
		try (ResultSet messageResult = statement.executeQuery(query)) {
		    logger.info(DATA_ADDED_SIGN);
		}
	    }
	} catch (SQLException e) {
	    logger.error("Creation SQL data error ", e);
	}
    }

    public void createQueryToBase(String query, Message message) {
	try (Connection connection = OracleConnectionFactory.getConnection()) {
	    try (PreparedStatement statement = connection
		    .prepareStatement(query)) {
		statement.setString(FIRST_VARIABLE_PREPARED_STATEMENT, 
			message.getUserFrom());
		statement.setString(SECOND_VARIABLE_PREPARED_STATEMENT, 
			message.getMessage());
		statement.executeUpdate();
	    }
	} catch (SQLException e) {
	    logger.error("SQL query to the database error ", e);
	}
    }

    public void createQueryToBaseUser(String query, User user, String message) {
	try (Connection connection = OracleConnectionFactory.getConnection()) {
	    try (PreparedStatement statement = connection
		    .prepareStatement(query)) {
		statement.setString(FIRST_VARIABLE_PREPARED_STATEMENT, 
			user.getNick());
		statement.setString(SECOND_VARIABLE_PREPARED_STATEMENT, 
			message);
		statement.executeUpdate();
	    }
	} catch (SQLException e) {
	    logger.error("SQL query to the database user error ", e);
	}
    }

    public List<Message> getLastMessages(int count) {
	List<Message> messages = new ArrayList<>();
	String query = bundle.getString(GET_LAST_MESSAGES_BUNDLE);

	try (Connection connection = OracleConnectionFactory.getConnection()) {
	    try (PreparedStatement statement = connection
		    .prepareStatement(query)) {
		statement.setInt(1, count);
		try (ResultSet messageResult = statement.executeQuery()) {
		    while (messageResult.next()) {
			Message message = buildMessage(messageResult);
			messages.add(message);
		    }
		}
	    }
	} catch (SQLException e) {
	    logger.error("SQL get last messages error ", e);
	}
	return messages;
    }

    public Message buildMessage(ResultSet messageResult) {
	Message messages = new Message();
	
	try {
	    messages.setUserFrom(messageResult
		    .getString(SECOND_VARIABLE_PREPARED_STATEMENT));
	    messages.setMessage(messageResult
		    .getString(THIRD_VARIABLE_PREPARED_STATEMENT));
	    messages.setTimeStamp(messageResult
		    .getString(FOURTH_VARIABLE_PREPARED_STATEMENT));
	    messages.setStatus(messageResult
		    .getString(FIFTH_VARIABLE_PREPARED_STATEMENT));
	} catch (SQLException e) {
	    logger.error("Building message error ", e);
	}
	return messages;
    }

    public Message getlastUserMessage(User user) {
	String query = bundle.getString(GET_LAST_USER_MESSAGE_BUNDLE);
	Message message = null;

	try (Connection connection = OracleConnectionFactory
		.getConnection()) {
	    try (PreparedStatement statement = connection
		    .prepareStatement(query)) {
		statement.setString(FIRST_VARIABLE_PREPARED_STATEMENT, 
			user.getNick());
		statement.setString(SECOND_VARIABLE_PREPARED_STATEMENT, 
			user.getNick());
		try (ResultSet messageResult = statement.executeQuery()) {
		    while (messageResult.next()) {
			message = buildMessage(messageResult);

		    }
		}
	    }
	} catch (SQLException e) {
	    logger.error("SQL the last user message error ", e);
	}
	return message;
    }

    public String getUserPassword(String user) {
	String query = bundle.getString(GET_USER_PASS_BUNDLE);
	String password = null;

	try (Connection connection = OracleConnectionFactory.getConnection()) {
	    try (PreparedStatement statement = connection
		    .prepareStatement(query)) {
		statement.setString(FIRST_VARIABLE_PREPARED_STATEMENT, user);
		try (ResultSet messageResult = statement.executeQuery()) {
		    while (messageResult.next()) {
			password = messageResult.getString(1);
		    }
		}
	    }
	} catch (SQLException e) {
	    logger.error("SQL get user password error ", e);
	}
	return password;
    }

    public String checkEmptyUserLogin(String user) {
	String query = bundle.getString(CHECK_USER_NICK_BUNDLE);
	String userLogin = null;

	try (Connection connection = OracleConnectionFactory.getConnection()) {
	    try (PreparedStatement statement = connection
		    .prepareStatement(query)) {
		statement.setString(FIRST_VARIABLE_PREPARED_STATEMENT, user);
		try (ResultSet messageResult = statement.executeQuery()) {
		    while (messageResult.next()) {
			userLogin = messageResult.getString(1);
		    }
		}
	    }
	} catch (SQLException e) {
	    logger.error("SQL check empty user error ", e);
	}
	return userLogin;
    }

    public String getLastStatus(User user) {
	String query = bundle.getString(GET_LAST_STATUS_BUNDLE);
	String status = null;

	try (Connection connection = OracleConnectionFactory
		.getConnection()) {
	    try (PreparedStatement statement = connection
		    .prepareStatement(query)) {
		statement.setString(FIRST_VARIABLE_PREPARED_STATEMENT, 
			user.getNick());
		statement.setString(SECOND_VARIABLE_PREPARED_STATEMENT, 
			user.getNick());
		try (ResultSet messageResult = statement.executeQuery()) {
		    while (messageResult.next()) {
			status = messageResult.getString(1);
		    }
		}
	    }
	} catch (SQLException e) {
	    logger.error("SQL get last status error ", e);
	}
	return status;
    }

    public Map<String, Object> getAllLoggedUsers() {
	int counter = 0;
	String query = bundle.getString(GET_ALL_LOGGED_BUNDLE);
	Map<String, Object> onlineUsers = new HashMap<>();

	try (Connection connection = OracleConnectionFactory.getConnection()) {
	    try (PreparedStatement statement = connection
		    .prepareStatement(query)) {
		try (ResultSet messageResult = statement.executeQuery()) {
		    while (messageResult.next()) {
			onlineUsers.put(USER_MAP_NAME + counter, messageResult
				.getString(FIRST_VARIABLE_PREPARED_STATEMENT));
			counter++;
		    }
		}
	    }
	} catch (SQLException e) {
	    logger.error("SQL query get all logged users error ", e);
	}
	return onlineUsers;
    }

    public UserRole getRole(User user) {
	String query = bundle.getString(GET_ROLE_BUNDLE);
	UserRole userRole = new UserRole();

	try (Connection connection = OracleConnectionFactory.getConnection()) {
	    try (PreparedStatement statement = connection
		    .prepareStatement(query)) {
		statement.setString(1, user.getNick());
		try (ResultSet messageResult = statement.executeQuery()) {
		    while (messageResult.next()) {
			userRole.setTitle(messageResult
				.getString(FIRST_VARIABLE_PREPARED_STATEMENT));
			userRole.setDescription(messageResult
				.getString(SECOND_VARIABLE_PREPARED_STATEMENT));
		    }
		}
	    }
	} catch (SQLException e) {
	    logger.error("SQL query get user role error ", e);
	}
	return userRole;
    }

    public void addNewUserToBase(User user) {
	String query = bundle.getString(ADD_NEW_USER_BUNDLE);

	try (Connection connection = OracleConnectionFactory.getConnection()) {
	    try (PreparedStatement statement = connection
		    .prepareStatement(query)) {
		statement.setString(FIRST_VARIABLE_PREPARED_STATEMENT, 
			user.getNick());
		statement.setString(SECOND_VARIABLE_PREPARED_STATEMENT, 
			user.getRole());
		statement.setString(THIRD_VARIABLE_PREPARED_STATEMENT, 
			user.getFullName());
		statement.setString(FOURTH_VARIABLE_PREPARED_STATEMENT, 
			user.getTelephoneNumber());
		statement.setString(FIFTH_VARIABLE_PREPARED_STATEMENT, 
			user.getEmail());
		statement.executeUpdate();
		logger.info("user " + user + " added to the base");
	    }
	} catch (SQLException e) {
	    logger.error("SQL add a new user error ", e);
	}
    }

    public void addPasswordToBase(User user) {
	String query = bundle.getString(ADD_NEW_PASS_BUNDLE);

	try (Connection connection = OracleConnectionFactory
		.getConnection()) {
	    try (PreparedStatement statement = connection
		    .prepareStatement(query)) {
		statement.setString(FIRST_VARIABLE_PREPARED_STATEMENT, 
			user.getNick());
		statement.setString(SECOND_VARIABLE_PREPARED_STATEMENT, 
			user.getPassword());
		statement.executeUpdate();
		logger.info("password added to the base");
	    }
	} catch (SQLException e) {
	    logger.error("SQL add user password error ", e);
	}
    }

    public Map<String, Object> getUserData(String user) {
	String query = bundle.getString(GET_USER_DATA_BUNDLE);
	Map<String, Object> onlineUsers = new HashMap<>();

	try (Connection connection = OracleConnectionFactory.getConnection()) {
	    try (PreparedStatement statement = connection
		    .prepareStatement(query)) {
		statement.setString(1, user);
		try (ResultSet messageResult = statement.executeQuery()) {
		    while (messageResult.next()) {
			onlineUsers.put(FULL_NAME_MAP_NAME, messageResult
				.getString(FIRST_VARIABLE_PREPARED_STATEMENT));
			onlineUsers.put(TELEPHONE_MAP_NAME, messageResult
				.getString(SECOND_VARIABLE_PREPARED_STATEMENT));
			onlineUsers.put(EMAIL_MAP_NAME, messageResult
				.getString(THIRD_VARIABLE_PREPARED_STATEMENT));
		    }
		}
	    }
	} catch (SQLException e) {
	    logger.error("SQL query get user data error ", e);
	}
	return onlineUsers;
    }
}
