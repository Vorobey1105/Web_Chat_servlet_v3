package com.epam.chat.datalayer;

/**
 * 
 * Implements DAO factory. It provides the choice of database type.
 *
 */
public abstract class DAOFactory {

    public static DAOFactory getInstance(DBType dbType) {
	return dbType.getDAOFactory();
    }

    public abstract MessageDAO getMessageDAO();

    public abstract UserDAO getUserDAO();
}
