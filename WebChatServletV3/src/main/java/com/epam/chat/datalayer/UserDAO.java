package com.epam.chat.datalayer;

import java.util.Map;

import com.epam.chat.datalayer.dto.User;
import com.epam.chat.datalayer.dto.UserRole;

/**
 * Describes access interface to user Data Access Object
 */
public interface UserDAO {

    /**
     * Login user
     * 
     * @param userToLogin user we want to login
     */
    public void login(User userToLogin);

    /**
     * Checks whether a user is logged in
     * 
     * @param user user to check
     * @return boolean result of check
     */
    boolean isLoggedIn(User user);

    /**
     * Logout a user from system
     * 
     * @param userToLogout user we want to logout
     */
    void logout(User userToLogout);

    /**
     * Gets all users logged in the system
     * 
     * @return list of users
     */
    Map<String, Object> getAllLogged();

    /**
     * Gets password with of a user
     * 
     * with the help of the NickName
     */
    boolean passwordCheck(String user, String password);

    /**
     * Gets the current user data from the database
     * 
     * @param String user to get the data
     * @return Map<String, Object> of userData(fullName, email, telephone)
     */
    public Map<String, Object> getUserData(String user);

    /**
     * Checks whether nick already exists in the base
     * 
     * @param String user to get the data
     * @return boolean of the result
     */
    public boolean userNickCheck(String user);

    /**
     * Implements adding a new user the database
     * 
     * @param User user to add to the database
     */
    public void addNewUserToBase(User user);

    /**
     * Gets the current user role
     * 
     * @param User user to get the current role
     */
    public UserRole getRole(User user);
}
