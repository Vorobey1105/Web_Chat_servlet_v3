package com.epam.chat.servlets;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.epam.chat.commands.CommandChatMessages;
import com.epam.chat.commands.CommandSessionCheck;
import com.epam.chat.commands.CommandCurrentNick;
import com.epam.chat.commands.CommandGetOnlineUsers;
import com.epam.chat.commands.CommandGetUserData;
import com.epam.chat.commands.CommandLogin;
import com.epam.chat.commands.CommandLogout;
import com.epam.chat.commands.CommandMessage;
import com.epam.chat.commands.CommandRegistration;
import com.epam.chat.datalayer.DAOFactory;
import com.epam.chat.datalayer.DBType;
import com.epam.chat.datalayer.MessageDAO;
import com.epam.chat.datalayer.UserDAO;

/**
 * 
 * Front controller implementation. Gets commands for JSP pages. It gets their
 * requests and redirects to commands.
 * 
 * @author Sergey Vorobyev	
 * @since 10/30/2019
 * @version 1.1
 *
 */

@WebServlet("/update")
@MultipartConfig
public class FrontController extends HttpServlet {
    private static final String CURRENT_NICK = "currentNick";
    private static final String SESSION_CHECK = "sessionCheck";
    private static final String USER_DAO_INIT_CONTEXT = "userDAO";
    private static final String MESSAGE_DAO_INIT_CONTEXT = "messageDAO";
    private static final String COMMAND_REQUEST = "command";
    private static final long serialVersionUID = 1L;
    private static final String LOGIN = "login";
    private static final String LOGOUT = "logout";
    private static final String MESSAGE = "message";
    private static final String REGISTRATION = "registration";
    private static final String GET_ONLINE_USERS = "getOnlineUsers";
    private static final String GET_LAST_MESSAGES = "getLastMessages";
    private static final String GET_USER_DATA = "getUserData";

    /**
     * Processing GET requests
     */
    public FrontController() {
	super();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
	// GET requests
    }

    /**
     * Processing POST requests
     */
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) {
	String formType = req.getParameter(COMMAND_REQUEST);

	switch (formType) {
	case LOGIN:
	    new CommandLogin().execute(req, resp);
	    break;
	case REGISTRATION:
	    new CommandRegistration().execute(req, resp);
	    break;
	case SESSION_CHECK:
	    new CommandSessionCheck().execute(req, resp);
	    break;
	case CURRENT_NICK:
	    new CommandCurrentNick().execute(req, resp);
	    break;    
	case GET_ONLINE_USERS:
	    new CommandGetOnlineUsers().execute(req, resp);
	    break;
	case GET_USER_DATA:
	    new CommandGetUserData().execute(req, resp);
	    break;
	case LOGOUT:
	    new CommandLogout().execute(req, resp);
	    break;
	case MESSAGE:
	    new CommandMessage().execute(req, resp);
	    break;
	case GET_LAST_MESSAGES:
	    new CommandChatMessages().execute(req, resp);
	    break;
	default:
	    break;
	}
    }

    /**
     * Define command by her name from request
     * 
     * @param commandName
     * @return chosen command
     */
    @Override
    public void init() throws ServletException {
	DAOFactory factory = DAOFactory.getInstance(DBType.ORACLE);
	MessageDAO messageDAO = factory.getMessageDAO();
	UserDAO userDAO = factory.getUserDAO();
	ServletContext context = getServletContext();
	context.setAttribute(MESSAGE_DAO_INIT_CONTEXT, messageDAO);
	context.setAttribute(USER_DAO_INIT_CONTEXT, userDAO);
	super.init();
    }
}
