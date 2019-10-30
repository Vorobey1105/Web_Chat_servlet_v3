package com.epam.chat.filters;

import com.google.common.cache.Cache;
import java.io.IOException;
import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;

/**
 * CSRF prevention filter. Validates received tokens for every post request.
 * 
 * @author Sergey Vorobyev
 * @since 10/30/2019
 * @version 1.1
 * 
 */

public class ValidateToken implements Filter {
    private static final String CSRF_ERROR_DETECTION = "Potential CSRF "
    	+ "detected!";
    private static final String CSRF_PREVENTION_CACHE = "csrfPreventionCache";
    private static final String CSRF_PREVENTION_HTML = "csrfPrevention";
    private static final String UNCHECKED_ANNOTATION = "unchecked";
    private static final Logger logger = Logger
	    .getLogger(CreateToken.class);
    
    @SuppressWarnings(UNCHECKED_ANNOTATION)
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, 
	    FilterChain chain) {
	HttpServletRequest httpReq = (HttpServletRequest) request;
	String randomToken = httpReq.getParameter(CSRF_PREVENTION_HTML);
	
	Cache<String, Boolean> csrfPreventionSaltCache = (Cache<String, 
		Boolean>) httpReq.getSession()
			.getAttribute(CSRF_PREVENTION_CACHE);
	
	if (csrfPreventionSaltCache != null && randomToken != null 
		&& csrfPreventionSaltCache.getIfPresent(randomToken) != null) {

	    try {
		chain.doFilter(request, response);
	    } catch (IOException | ServletException e) {
		logger.error("Validation CSRF token error ", e);
	    }
	    
	} else {
	    logger.error("Potential CSRF detected!");
	    try {
		throw new ServletException(CSRF_ERROR_DETECTION);
	    } catch (ServletException e) {
		logger.error("Potential CSRF detected! ", e);
	    }
	}
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void destroy() {
    }
}
