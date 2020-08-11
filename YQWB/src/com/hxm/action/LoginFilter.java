package com.hxm.action;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class LoginFilter implements Filter {
	 public FilterConfig config;

	    public void destroy() {
	        this.config = null;
	    }


	    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
	        HttpServletRequest hrequest = (HttpServletRequest)request;
	        HttpServletResponse hresponse = (HttpServletResponse)response;
	        Object currentUser = hrequest.getSession().getAttribute("currentUser");
	        if(currentUser!=null){
	        	chain.doFilter(request, response);
	        }else{
	        	String uri = hrequest.getRequestURI();
	        	String extend[] = {".js",".jpg",".png",".css",".gif",".jpeg","index.jsp"};
	        	boolean query = false;
	        	found:
	        	for(int i=0;i<extend.length;i++){
	        		  if(uri.endsWith(extend[i])){
	        			  query = true;
	        			  break found;
	        		  }
	        	}
	        	if(query){
	        		chain.doFilter(request, response);
	        	}else{
	        		hresponse.sendRedirect("index.jsp");
	        	}
	        }

	    }

	    public void init(FilterConfig filterConfig) throws ServletException {
	        config = filterConfig;
	    }

}
