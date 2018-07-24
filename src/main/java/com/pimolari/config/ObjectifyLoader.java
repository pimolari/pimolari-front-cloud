package com.pimolari.config;

import java.util.logging.Logger;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import com.googlecode.objectify.ObjectifyService;
import com.pimolari.entity.Device;
import com.pimolari.entity.IncomingReading;
import com.pimolari.entity.Project;

/**
 * @version 1.0
 * @author GMB 2016
 * */
public class ObjectifyLoader implements ServletContextListener {
  
  static Logger logger = Logger.getLogger(ObjectifyLoader.class.getName());
  
  static {
    logger.info("Registering objectify entities...");
    ObjectifyService.register(IncomingReading.class);
    ObjectifyService.register(Project.class);
    ObjectifyService.register(Device.class);
    logger.info("Entities successfuly registered");
    
    /*ObjectifyService.register(User.class);
		ObjectifyService.register(Account.class);
		ObjectifyService.register(Property.class);
		ObjectifyService.register(Label.class);*/
  }
  
  @Override
  public void contextDestroyed(ServletContextEvent event) {
  }
  
  @Override
  public void contextInitialized(ServletContextEvent event) {
  }
}
