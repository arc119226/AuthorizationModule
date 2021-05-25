package com.arcare.oauth.listener;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

public class InitServletContextListener implements ServletContextListener{

	@Override
	public void contextInitialized(ServletContextEvent arg0) {
		System.out.println("init");
	}
	
	@Override
	public void contextDestroyed(ServletContextEvent arg0) {

	}

}
