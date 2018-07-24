package com.pimolari.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.appengine.api.memcache.ErrorHandlers;
import com.google.appengine.api.memcache.MemcacheService;
import com.google.appengine.api.memcache.MemcacheServiceFactory;
import com.google.gson.Gson;
import com.googlecode.objectify.Key;
import com.googlecode.objectify.NotFoundException;
import com.googlecode.objectify.ObjectifyService;
import com.googlecode.objectify.cmd.Query;
import com.pimolari.entity.Device;
import com.pimolari.entity.FilterBean;
import com.pimolari.entity.IncomingReading;
import com.pimolari.exception.BaseException;
import com.pimolari.exception.DaoException;
import com.pimolari.util.Util;

public class IncomingReadingDao {
  
  /** Logger */
  private static final Logger logger = Logger.getLogger(IncomingReadingDao.class.getName());
  
  /**
   * @return Long
   * @param Content
   *          content
   * @throws DaoException
   */
  public Long save(IncomingReading reading, Device device) throws DaoException {
    
    logger.info("Saves new incoming reading for project " + device.getProjectId());
    Key<IncomingReading> key = null;
    try {
      
      key = ObjectifyService.ofy().save().entity(reading).now();
      
      logger.info("Reading properly saved (DS) with key " + key.getString());
      
      MemcacheService syncCache = MemcacheServiceFactory.getMemcacheService();
      
      syncCache.setErrorHandler(ErrorHandlers.getConsistentLogAndContinue(Level.INFO));
      List<Device> devices = (List<Device>) syncCache.get("project-" + device.getProjectId());
      
      if (devices != null) {
        
        if (devices.size() == 0) {
          devices.add(device);
        } else {
          for (int n = 0; n < devices.size(); n++) {
            if (devices.get(n).getId().equals(device.getId())) {
              devices.set(n, device);
              break;
            }
          }
        }
        
        syncCache.put("project-" + device.getProjectId(), devices);
        logger.info("Reading properly cached on existing project cache " + key.getString());
      } else {
        devices = new ArrayList<Device>();
        devices.add(device);
        syncCache.put("project-" + device.getProjectId(), devices);
        logger.info("Reading properly cached on a new project cache " + key.getString());
      }
      
      List<Device> dd = (List<Device>) syncCache.get("project-" + device.getProjectId());
      System.out.println("Object from cache: " + new Gson().toJson(dd));
      
    } catch (Exception e) {
      
      throw new DaoException("Error saving reading " + e.getMessage(), BaseException.ERROR, 500);
    }
    
    return key != null ? key.getId() : null;
  }
  
  /**
   * @param String
   *          id
   * @return Stats
   * @throws DaoException
   */
  public IncomingReading get(String id) throws DaoException, NotFoundException {
    
    logger.info("Retrieves content with id: " + id);
    IncomingReading reading = null;
    try {
      reading = ObjectifyService.ofy().load().type(IncomingReading.class).id(id).now();
    } catch (Exception e) {
      if (e instanceof NotFoundException)
        // throw (NotFoundException)e;
        throw new DaoException("Reading with id " + id + " not found ", BaseException.ERROR, 404);
      else
        throw new DaoException("Error retrieving reading with id " + id + ": " + e.getMessage(), BaseException.ERROR,
            500);
    }
    
    return reading;
  }
  
  /**
   * @param FilterBean
   *          filter
   * @return List<Content>
   * @throws DaoException
   */
  public List<IncomingReading> list(FilterBean filter) throws DaoException {
    
    logger.info("Listing readings " + filter != null ? "using filter " + filter.toString() : "without filter");
    List<IncomingReading> list = null;
    
    try {
      Query<IncomingReading> query = ObjectifyService.ofy().load().type(IncomingReading.class);
      
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
          query = query.order("-" + "created");
      }
      
      list = query.list();
      
    } catch (Exception e) {
      if (e instanceof NotFoundException)
        throw new DaoException("No readings found", BaseException.ERROR, 404);
      // throw (NotFoundException)e;
      else
        throw new DaoException("Error retrieving list of readings " + e.getMessage(), BaseException.ERROR, 500);
    }
    
    return list;
  }
  
  /**
   * @param String
   *          id The project id to obtain the list of readings from
   * @return List<Project>
   * @throws DaoException
   */
  public List<IncomingReading> listByProject(String id) throws DaoException {
    return this.list(new FilterBean().add("projectId", id));
  }
}
