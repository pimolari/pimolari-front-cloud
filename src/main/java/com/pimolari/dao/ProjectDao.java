package com.pimolari.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.appengine.api.memcache.ErrorHandlers;
import com.google.appengine.api.memcache.MemcacheService;
import com.google.appengine.api.memcache.MemcacheServiceFactory;
import com.googlecode.objectify.Key;
import com.googlecode.objectify.NotFoundException;
import com.googlecode.objectify.Objectify;
import com.googlecode.objectify.ObjectifyService;
import com.googlecode.objectify.cmd.Query;
import com.pimolari.entity.Device;
import com.pimolari.entity.FilterBean;
import com.pimolari.entity.Project;
import com.pimolari.exception.BaseException;
import com.pimolari.exception.DaoException;
import com.pimolari.util.Util;

public class ProjectDao {
  
  /** Logger */
  private static final Logger logger = Logger.getLogger(ProjectDao.class.getName());
  
  /**
   * @return Long
   * @param Project
   *          project
   * @throws DaoException
   */
  public Long save(Project project) throws DaoException {
    
    logger.info("Saves new project " + project.getName());
    Key<Project> key = null;
    try {
      key = ObjectifyService.ofy().save().entity(project).now();
      logger.info("Project properly saved with id " + project.getId());
      
    } catch (Exception e) {
      e.printStackTrace();
      throw new DaoException("Error saving Project " + e.getMessage(), BaseException.ERROR, 500);
    }
    
    return key != null ? key.getId() : null;
  }
  
  /**
   * @param String
   *          id
   * @return Project
   * @throws DaoException
   */
  public Project get(String id) throws DaoException {
    
    logger.info("Retrieves Project with id: " + id);
    Project project = null;
    try {
      project = ObjectifyService.ofy().load().type(Project.class).id(id).now();
    } catch (Exception e) {
      if (e instanceof NotFoundException)
        // throw (NotFoundException)e;
        throw new DaoException("Project with id " + id + " not found ", BaseException.ERROR, 404);
      else
        throw new DaoException("Error retrieving Project with id " + id + ": " + e.getMessage(), BaseException.ERROR,
            500);
    }
    
    return project;
  }
  
  /**
   * @param String
   *          id
   * @return List<Device> an updated device list for project with id, only if
   *         devices have already been updated in memcache with a PUT
   *         /ew/readings invoke by a third party
   * @throws DaoException
   */
  public List<Device> monitor(String id) throws DaoException {
    
    logger.info("Monitor Project with id: " + id);
    List<Device> devices = null;
    
    MemcacheService syncCache = MemcacheServiceFactory.getMemcacheService();
    syncCache.setErrorHandler(ErrorHandlers.getConsistentLogAndContinue(Level.INFO));
    
    try {
      devices = (List<Device>) syncCache.get("project-" + id);
      
      if (devices != null) {
        logger.info("Project found in memcache with " + devices.size() + " updated devices");
      } else {
        // throw new DaoException("Not found project for monitoring " + id,
        // BaseException.WARNING, 404);
        syncCache.put("project-" + id, new ArrayList<Device>());
        logger.info(
            " ** Project does not have any updated device readings to retrieve. Creates an empty cached registry");
      }
      
    } catch (Exception e) {
      throw new DaoException("Error monitoring Project with id " + id + ": " + e.getMessage(), BaseException.ERROR,
          500);
    }
    
    return devices;
  }
  
  /**
   * @param FilterBean
   *          filter
   * @return List<Project>
   * @throws DaoException
   */
  public List<Project> list(FilterBean filter) throws DaoException {
    
    logger.info("Listing Projects " + (filter != null ? "using filter " + filter.toString() : "without filter"));
    List<Project> list = null;
    
    try {
      Query<Project> query = ObjectifyService.ofy().load().type(Project.class);
      
      if (filter != null && (filter.getFilter() != null && !filter.getFilter().isEmpty())) {
        
        for (Map.Entry e : filter.getFilter().entrySet()) {
          if (e.getKey() != null && e.getValue() != null) {
            query = query.filter((String) e.getKey(), e.getValue());
          }
        }
        
        /*
         * Iterator<?> it = filter.getFilter().entrySet().iterator(); while
         * (it.hasNext()) { Map.Entry e = (Map.Entry) it.next(); if (e.getKey()
         * != null && e.getValue() != null){ query.filter((String) e.getKey(),
         * e.getValue()); } }
         */
      }
      
      if (filter != null) {
        // pagination
        if (filter.getPage() != null && !filter.getPage().isEmpty() && filter.getPageSize() != null
            && !filter.getPageSize().isEmpty()) {
          int page = Integer.parseInt(filter.getPage()) - 1;
          if (page < 0) {
            page = 0;
          }
          int pageSize = Integer.parseInt(filter.getPageSize());
          if (pageSize > 0) {
            query = query.limit(pageSize).offset(page * pageSize);
          }
        }
        if (!Util.isNullOrEmptyString(filter.getOrder()) && !Util.isNullOrEmptyString(filter.getOrderBy()))
          query = query.order(("asc".equals(filter.getOrder().toLowerCase()) ? "" : "-") + filter.getOrderBy());
        else
          query = query.order("-" + "lastModified");
      }
      
      list = query.list();
      
    } catch (Exception e) {
      if (e instanceof NotFoundException)
        throw new DaoException("No reading found", BaseException.ERROR, 404);
      // throw (NotFoundException)e;
      else
        throw new DaoException("Error retrieving list of stats " + e.getMessage(), BaseException.ERROR, 500);
    }
    
    return list;
  }
}
