package com.pimolari.dao;

import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import com.googlecode.objectify.Key;
import com.googlecode.objectify.NotFoundException;
import com.googlecode.objectify.ObjectifyService;
import com.googlecode.objectify.cmd.Query;
import com.pimolari.entity.Device;
import com.pimolari.entity.FilterBean;
import com.pimolari.exception.BaseException;
import com.pimolari.exception.DaoException;
import com.pimolari.util.Util;

public class DeviceDao {
  
  /** Logger */
  private static final Logger logger = Logger.getLogger(DeviceDao.class.getName());
  
  /**
   * @return Long
   * @param Device
   *          device
   * @throws DaoException
   */
  public Long save(Device device) throws DaoException {
    
    logger.info("Saves device ");
    Key<Device> key = null;
    try {
      key = ObjectifyService.ofy().save().entity(device).now();
      logger.info("Device properly saved with id " + device.getId());
      
    } catch (Exception e) {
      throw new DaoException("Error saving Device " + e.getMessage(), BaseException.ERROR, 500);
    }
    
    return key != null ? key.getId() : null;
  }
  
  /**
   * @return Long
   * @param Device
   *          device
   * @throws DaoException
   */
  public void delete(String id) throws DaoException {
    
    logger.info("Deletes device " + id);
    try {
      ObjectifyService.ofy().delete().type(Device.class).id(id).now();
      
      logger.info("Device properly deleted with id " + id);
      
    } catch (Exception e) {
      throw new DaoException("Error deleting Device " + e.getMessage(), BaseException.ERROR, 500);
    }
    
  }
  
  /**
   * @param String
   *          id
   * @return Device
   * @throws DaoException
   */
  public Device get(String id) throws DaoException, NotFoundException {
    
    logger.info("Retrieves device with id: " + id);
    Device device = null;
    try {
      device = ObjectifyService.ofy().load().type(Device.class).id(id).now();
    } catch (Exception e) {
      if (e instanceof NotFoundException)
        // throw (NotFoundException)e;
        throw new DaoException("Device with id " + id + " not found ", BaseException.ERROR, 404);
      else
        throw new DaoException("Error retrieving Device with id " + id + ": " + e.getMessage(), BaseException.ERROR,
            500);
    }
    
    return device;
  }
  
  /**
   * @param FilterBean
   *          filter
   * @return List<Device>
   * @throws DaoException
   */
  public List<Device> list(FilterBean filter) throws DaoException {
    
    logger.info("Listing devices " + (filter != null ? "using filter " + filter.getFilter().toString() : "without filter"));
    List<Device> list = null;
    
    try {
      Query<Device> query = ObjectifyService.ofy().load().type(Device.class);
      
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
        //else
          //query = query.order("-" + "modified");
      }
      
      list = query.list();
      
    } catch (Exception e) {
      if (e instanceof NotFoundException)
        throw new DaoException("No device found", BaseException.ERROR, 404);
      // throw (NotFoundException)e;
      else
        throw new DaoException("Error retrieving list of devices " + e.getMessage(), BaseException.ERROR, 500);
    }
    
    // System.out.println((list == null? list : list.size()) + " devices found
    // for project " + filter.getFilter().get("projectId == "));
    return list;
  }
  
  /**
   * @param String
   *          id The project id to obtain the list of devivces from
   * @return List<Device>
   * @throws DaoException
   */
  public List<Device> listByProject(String id) throws DaoException {
    return this.list(new FilterBean().add("projectId", id));
  }
}
