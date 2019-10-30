package com.epam.chat.datalayer.db;

import java.util.Map;
import java.util.ResourceBundle;
import org.apache.log4j.Logger;
import com.epam.chat.datalayer.UserDAO;
import com.epam.chat.datalayer.dto.User;
import com.epam.chat.datalayer.dto.UserRole;

/**
 * Implements message DAO and perform the interface tasks
 * 
 * @author Sergey Vorobyev	
 * @since 09/27/2019
 * @version 1.0
 * 
 */
public class OracleUserDAO implements UserDAO {
    private static final Logger logger = Logger
	    .getLogger(OracleUserDAO.class);
    private static final String MESSAGE_STATUS = "MESSAGE";
    private static final String LOGIN_STATUS = "LOGIN";
    private static final String BUNDLE_PATH = "sqlQueries";
    private static final String LOGOUT_BUNDLE = "logout";
    private static final String ADDED_SIGN = " has been added to the base!";
    private static final String IS_LOGOUT_SIGN = " is logout!";
    private static final String IS_LOGIN_SIGN = " is login!";
    private static final String LOGIN_BUNDLE = "login";
    private static final String USERSIGN = "The user ";
    private ResourceBundle bundle = ResourceBundle.getBundle(BUNDLE_PATH);
    private SQLPerformer performer = new SQLPerformer();

    @Override
    public void login(User userToLogin) {
	String message = USERSIGN + userToLogin.getNick() + IS_LOGIN_SIGN;
	String query = bundle.getString(LOGIN_BUNDLE);
	performer.createQueryToBaseUser(query, userToLogin, message);
	logger.info(USERSIGN + userToLogin.getNick() + IS_LOGIN_SIGN);
    }

    @Override
    public boolean isLoggedIn(User user) {
	boolean isLoggedIn = false;
	String userStatus = performer.getLastStatus(user);

	if (LOGIN_STATUS.equals(userStatus) || MESSAGE_STATUS
		.equals(userStatus)) {
	    isLoggedIn = true;
	}
	return isLoggedIn;
    }

    @Override
    public void logout(User userToLogout) {
	String message = USERSIGN + userToLogout.getNick() + IS_LOGOUT_SIGN;
	String query = bundle.getString(LOGOUT_BUNDLE);
	performer.createQueryToBaseUser(query, userToLogout, message);
	logger.info(USERSIGN + userToLogout.getNick() + IS_LOGOUT_SIGN);
    }

    @Override
    public Map<String, Object> getAllLogged() {
	return performer.getAllLoggedUsers();
    }

    @Override
    public UserRole getRole(User user) {
	return performer.getRole(user);
    }

    @Override
    public boolean passwordCheck(String user, String password) {
	Boolean passCheck = false;
	String userPassword = null;
	userPassword = performer.getUserPassword(user);

	if (userPassword != null && userPassword.equals(password)) {
	    passCheck = true;
	}
	return passCheck;
    }

    @Override
    public boolean userNickCheck(String user) {
	String userNickCheck = null;
	Boolean nickInBase = false;
	userNickCheck = performer.checkEmptyUserLogin(user);

	if (userNickCheck != null) {
	    nickInBase = true;
	}
	return nickInBase;
    }

    @Override
    public void addNewUserToBase(User user) {
	performer.addNewUserToBase(user);
	performer.addPasswordToBase(user);
	logger.info(USERSIGN + user.getNick() + ADDED_SIGN);
    }

    @Override
    public Map<String, Object> getUserData(String user) {
	Map<String, Object> userData = performer.getUserData(user);
	return userData;
    }

    public void createInitialTables() {
	performer.createInitialTables();
    }

    public void createInitialData() {
	performer.createInitialData();
    }
}
