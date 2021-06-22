//package com.trainingquizzes.english.config;
//
//import java.util.logging.Logger;
//
//import javax.faces.webapp.FacesServlet;
//import javax.servlet.ServletContextEvent;
//import javax.servlet.ServletContextListener;
//import javax.servlet.ServletRegistration.Dynamic;
//import javax.servlet.annotation.WebListener;
//
//@WebListener
//public class MyServletContextListener implements ServletContextListener {
//	
//	@Override
//	public void contextInitialized(ServletContextEvent sce) {
//		ServletContextListener.super.contextInitialized(sce);
//
//		 Dynamic facesServlet = sce.getServletContext().addServlet("FacesServlet", FacesServlet.class);
//	      //Specifying the Servlet Mapping
//		 facesServlet.addMapping("*.xhtml");
//	      //Setting Priority, 0 or higher for eager, if negative then it's lazy
//	     facesServlet.setLoadOnStartup(1);
//	 
//	     Logger.getGlobal().info("Servlet Context has been initialized");
//		
//	}
//	
//	
//
//}
