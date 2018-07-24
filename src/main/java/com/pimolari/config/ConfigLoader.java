package com.pimolari.config;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;
import java.util.logging.Logger;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;



/**
 * @version 1.0
 * @author CIBER 2012
 * */
public class ConfigLoader implements ServletContextListener {
  
  protected static final Logger logger = Logger.getLogger(ConfigLoader.class.getName());
  
  
  @Override
  public void contextDestroyed(ServletContextEvent event) {
  }
  
  @Override
  public void contextInitialized(ServletContextEvent event) {
    
    ServletContext c = event.getServletContext();
    load();
    logger.info("Application configuration loaded");
  }
  
  public void load() {
    logger.info("Loading application configuration from app.properties");
    try {
      Properties properties = new Properties();
      properties.load(this.getClass().getResourceAsStream("/app.properties"));
      ConfigUtil util = new ConfigUtil(properties);
      logger.info("Config loaded for service " + ConfigUtil.get("service.name") + " with configured address " + ConfigUtil.get("service.environment.address"));
    } catch(Exception e) {
      logger.severe("Config file not found or not readable. Make sure the file /config/[environment]/app.properties is reachable");
      //e.printStackTrace();
    }
  }
  
}