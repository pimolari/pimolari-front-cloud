package com.pimolari.biz;

import java.util.Date;
import java.util.List;

import com.google.gson.Gson;
import com.pimolari.dao.DeviceDao;
import com.pimolari.entity.Device;
import com.pimolari.entity.FilterBean;
import com.pimolari.entity.IncomingReading;
import com.pimolari.entity.Param;
import com.pimolari.exception.BizException;
import com.pimolari.exception.DaoException;
import com.pimolari.util.Util;

public class DeviceBiz {
  
  public Device get(String id, String filter) throws BizException {
    
    DeviceDao ddao = new DeviceDao();
    Device device = null;
    
    try {
      device = ddao.get(id);
      fillUp(device, filter);
    } catch (DaoException e) {
      throw new BizException(e);
    }
    return device;
  }
  
  public List<Device> getByProjectId(String projectId, String filter) throws BizException {
    
    DeviceDao ddao = new DeviceDao();
    List<Device> l = null;
    
    try {
      l = ddao.list(new FilterBean().add("projectId", projectId));
      
      for (Device device : l) {
        fillUp(device, filter);
      }
    } catch (DaoException e) {
      throw new BizException(e);
    }
    
    return l;
  }
  
  public List<Device> list(String filter) throws BizException {
    
    DeviceDao ddao = new DeviceDao();
    
    List<Device> l = null;
    
    try {
      l = ddao.list(null);
      
      for (Device device : l) {
        fillUp(device, filter);
      }
    } catch (DaoException e) {
      throw new BizException(e);
    }
    
    return l;
  }
  
  public Device save(Device device) throws BizException {
    
    if (device.getId() == null || "".equals(device.getId()))
      device.setId(Util.randomKey());
    
    device.setCreated(new Date());
    device.setModified(new Date());
    // TODO: In the future, devices should be preallocated and this method
    // should check here
    // for an existing device id
    
    DeviceDao ddao = new DeviceDao();
    
    try {
      ddao.save(device);
      // fillup? not yet, no nested objects to fill up
    } catch (DaoException e) {
      throw new BizException(e);
    }
    
    return device;
  }
  
  public Device update(Device device) throws BizException {
    
    if (device.getId() == null || "".equals(device.getId()))
      return null;
    
    Device pDevice = this.get(device.id, null);
    pDevice.setActive(device.isActive());
    pDevice.setDescription(device.getDescription());
    pDevice.setModified(new Date());
    pDevice.setName(device.getName());
    pDevice.setType(device.getType());
    pDevice.setParams(device.getParams());
    DeviceDao ddao = new DeviceDao();
    
    try {
      ddao.save(pDevice);
      // fillup? not yet, no nested objects to fill up
    } catch (DaoException e) {
      throw new BizException(e);
    }
    
    return device;
  }
  
  public Device addParam(String key, Param param) throws BizException {
    
    if (key == null || "".equals(key))
      return null;
    
    if (param == null)
      return null;
    
    Device pDevice = this.get(key, null);
    pDevice.setModified(new Date());
    pDevice.addParam(param);

    DeviceDao ddao = new DeviceDao();
    
    try {
      ddao.save(pDevice);
      // fillup? not yet, no nested objects to fill up
    } catch (DaoException e) {
      throw new BizException(e);
    }
    
    return pDevice;
  }
  
  
  
  public Device remove(Device device, boolean logical) throws BizException {
    
    if (logical) {
      device.setActive(false);
      this.update(device);
    } else {
      this.remove(device.id, false);
    }
    
    return device;
  }
  
  public Device remove(String id, boolean logical) throws BizException {
    
    if (logical) {
      Device device = this.get(id, null);
      return this.remove(device, true);
    } else {
      DeviceDao ddao = new DeviceDao();
      try {
        ddao.delete(id);
      } catch (DaoException e) {
        throw new BizException(e);
      }
    }
    
    return null;
  }
  
  /*
   * public Device delete(String id) throws DaoException {
   * 
   * DeviceDao dao = new DeviceDao(); dao.delete(id); return device; }
   */
  
  private Device fillUp(Device device, String filter) throws BizException {
    
    if (filter != null && !"".equals(filter)) {
      
      if ("*".equals(filter))
        filter = "lastRead";
      
      if (filter.indexOf("lastRead") >= 0) {
        // Retrieves last reading for device
        IncomingReadingBiz irbiz = new IncomingReadingBiz();
        IncomingReading reading = irbiz.lastByDevice(device.getId());
        System.out.println("Last reading retrieved for device is : " + new Gson().toJson(reading));
        device.setLastRead(reading);
      }
    }
    
    return device;
  }
  
}
