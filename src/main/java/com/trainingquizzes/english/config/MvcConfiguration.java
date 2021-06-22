//package com.trainingquizzes.english.config;
//
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.ComponentScan;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.web.servlet.ViewResolver;
//import org.springframework.web.servlet.config.annotation.EnableWebMvc;
//import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
//import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
//import org.springframework.web.servlet.view.InternalResourceViewResolver;
//
//@Configuration
//@ComponentScan(basePackages="com.trainingquizzes.english")
//@EnableWebMvc
////@EnableJpaRepositories(basePackageClasses = UserDao.class)
//public class MvcConfiguration implements WebMvcConfigurer {
//
//	@Bean
//	public ViewResolver getViewResolver(){
//		InternalResourceViewResolver resolver = new InternalResourceViewResolver();
//		resolver.setPrefix("/jsf/");
//		resolver.setSuffix(".xhtml");
//		return resolver;
//	}
//	
////	@Override
////	public void addResourceHandlers(ResourceHandlerRegistry registry) {
////		registry.addResourceHandler("/resources/**").addResourceLocations("/resources/");
////	}
//
//	
//}
