package com.epam.chat.filters;

import java.io.IOException;
import java.security.SecureRandom;
import java.util.concurrent.TimeUnit;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.RandomStringUtils;
import org.apache.log4j.Logger;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;

/**
 * CSRF prevention filter. Creates tokens for validating pages. Tokens created
 * for every post request and saved.
 * 
 * @author Sergey Vorobyev	
 * @since 10/30/2019
 * @version 1.1
 * 
 */

public class CreateToken implements Filter {
    private static final String CSRF_PREVENTION_COOKIE = "csrfPrevention";
    private static final Logger logger = Logger
	    .getLogger(CreateToken.class);
    private static final String CSRF_PREVENTION_HTML = CSRF_PREVENTION_COOKIE;
    private static final String CSRF_PREVENTION_CACHE = "csrfPreventionCache";
    private static final String UNCHECKED_ANNOTATION = "unchecked";

    @SuppressWarnings(UNCHECKED_ANNOTATION)
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, 
	    FilterChain chain) {
	HttpServletResponse httpRes = (HttpServletResponse) response;
	HttpServletRequest httpReq = (HttpServletRequest) request;
	Cache<String, Boolean> csrfPreventionCache = 
		(Cache<String, Boolean>) httpReq.getSession()
			.getAttribute(CSRF_PREVENTION_CACHE);

	if (csrfPreventionCache == null) {
	    csrfPreventionCache = CacheBuilder.newBuilder().maximumSize(5000)
		    .expireAfterWrite(20, TimeUnit.MINUTES).build();
	    httpReq.getSession().setAttribute(CSRF_PREVENTION_CACHE, 
		    csrfPreventionCache);
	}

	String randomToken = RandomStringUtils.random(20, 0, 0, true, true, 
		null, new SecureRandom());
	csrfPreventionCache.put(randomToken, Boolean.TRUE);
	httpReq.setAttribute(CSRF_PREVENTION_HTML, randomToken);
	Cookie cookie = new Cookie(CSRF_PREVENTION_COOKIE, randomToken); 	
	httpRes.addCookie(cookie);

	try {
	    chain.doFilter(request, response);
	} catch (IOException | ServletException e) {
	    logger.error("Creation CSRF token error ", e);
	}
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
	//TO DO
    }

    @Override
    public void destroy() {
	//TO DO
    }
}
