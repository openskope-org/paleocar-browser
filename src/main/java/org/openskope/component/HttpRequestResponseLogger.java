package org.openskope.component;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
public class HttpRequestResponseLogger extends OncePerRequestFilter {

	private Long nextRequestId = 1L;
	
	@Override 
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
		   throws ServletException, IOException {

		Long requestId;
		
		synchronized(HttpRequestResponseLogger.class) {
			requestId = nextRequestId++;
		}
		
		System.out.println(String.format("%s REQUEST  [id=%d] %s",
			   request.getMethod(), requestId, request.getRequestURI()));
	   
		filterChain.doFilter(request, response);
	
		System.out.println(String.format("%s RESPONSE [id=%d] %s STATUS %s", 
				request.getMethod(), requestId, request.getRequestURI(), response.getStatus()));
	}      
}
