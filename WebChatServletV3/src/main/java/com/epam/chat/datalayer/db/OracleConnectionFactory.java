package com.epam.chat.datalayer.db;

import java.sql.Connection;
import java.sql.SQLException;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import org.apache.log4j.Logger;

/**
 * Creates and returns connection with the database.
 * Gets data source with the help of JNDI Tomcat context.
 * 
 * @author Sergey Vorobyev	
 * @since 09/27/2019
 * @version 1.0
 * 
 */
public final class OracleConnectionFactory {
    private static final Logger logger = Logger
	    .getLogger(OracleConnectionFactory.class);
    private static final String JNDI_NAME = "java:comp/env/jdbc/XE";

    private OracleConnectionFactory() {
        throw new UnsupportedOperationException();
    }

    public static Connection getConnection() {
	DataSource dataSource = null;
	Context context = null;
	Connection connection = null;

	try {
	    context = new InitialContext();
	    dataSource = (DataSource) context.lookup(JNDI_NAME);
	} catch (NamingException e) {
	    logger.error("Data source error ", e);
	}
	
	try {
	    connection = dataSource.getConnection();
	} catch (NullPointerException | SQLException e) {
	    logger.error("Conncetion error ", e);
	}
	return connection;
    }
}
