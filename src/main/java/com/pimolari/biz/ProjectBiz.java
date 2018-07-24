package com.pimolari.biz;

import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

import com.pimolari.dao.ProjectDao;
import com.pimolari.entity.Device;
import com.pimolari.entity.Project;
import com.pimolari.exception.BizException;
import com.pimolari.exception.DaoException;
import com.pimolari.util.Util;

public class ProjectBiz {
  
  private static Logger logger = Logger.getLogger(ProjectBiz.class.getName());
  
  public Project get(String id, String filter) throws BizException {
    
    ProjectDao pdao = new ProjectDao();
    Project project = null;
    
    try {
      project = pdao.get(id);
      fillUp(project, filter);
    } catch (DaoException e) {
      throw new BizException(e);
    }
    
    return project;
  }
  
  public List<Device> monitor(String id) throws BizException {
    
    ProjectDao pdao = new ProjectDao();
    List<Device> devices = null;
    
    try {
      devices = pdao.monitor(id);
      // fillUp(project, filter);
    } catch (DaoException e) {
      throw new BizException(e);
    }
    
    return devices;
  }
  
  public List<Project> list(String filter) throws BizException {
    
    ProjectDao pdao = new ProjectDao();
    
    List<Project> l = null;
    
    try {
      l = pdao.list(null);
      
      for (Project project : l) {
        fillUp(project, filter);
      }
    } catch (DaoException e) {
      throw new BizException(e);
    }
    
    return l;
  }
  
  public Project save(Project project) throws BizException {
    
    if (project == null)
      throw new BizException("Project can not be null", null, 500);
    
    if (project.getId() == null || "".equals(project.getId()))
      project.setId(Util.randomKey());
    
    project.setCreated(new Date());
    project.setModified(new Date());
    
    try {
      ProjectDao pdao = new ProjectDao();
      pdao.save(project);
      
    } catch (DaoException e) {
      throw new BizException(e);
    }
    
    DeviceBiz dbiz = new DeviceBiz();
    if (project.getDevices() != null) {
      for (Device device : project.getDevices()) {
        if (!"".equals(device.getId())) {
          
          try {
            Device ddevice = dbiz.get(device.getId(), null);
            
            if (ddevice.getProjectId() != null && !"".equals(ddevice.getProjectId())) {
              logger.warning("Device " + device.getId() + " can not be linked to this project " + project.getId()
                  + " because it is already linked to a project " + ddevice.getProjectId());
            } else {
              ddevice.setProjectId(project.getId());
              logger.fine("Device " + device.getId() + " has been linked to this project " + project.getId());
              dbiz.update(ddevice);
            }
          } catch (BizException e) {
            logger.warning("Something wrong happened when saving devices to this project: " + e.getMessage());
          }
        } else {
          logger.warning("Tried to save a null id device for this project " + project.getId());
        }
      }
    }
    
    fillUp(project, "devices");
    
    return project;
  }
  
  public Project update(Project project) throws BizException {
    
    if (project == null)
      throw new BizException("Project can not be null", null, 500);
    
    if (project.getId() == null || "".equals(project.getId()))
      throw new BizException("Project id can not be null", null, 500);
    
    project.setModified(new Date());
    
    try {
      ProjectDao pdao = new ProjectDao();
      pdao.save(project);
      
    } catch (DaoException e) {
      throw new BizException(e);
    }
    
    DeviceBiz dbiz = new DeviceBiz();
    if (project.getDevices() != null) {
      for (Device device : project.getDevices()) {
        if (!"".equals(device.getId())) {
          
          try {
            Device ddevice = dbiz.get(device.getId(), null);
            
            if (ddevice.getProjectId() != null || !"".equals(ddevice.getProjectId())) {
              logger.warning("Device " + device.getId() + " can not be linked to this project " + project.getId()
                  + " because it is already linked to a project " + ddevice.getProjectId());
            } else {
              device.setProjectId(project.getId());
              logger.fine("Device " + device.getId() + " has been linked to this project " + project.getId());
              dbiz.update(ddevice);
            }
          } catch (BizException e) {
            logger.warning("Something wrong happened when saving devices to this project: " + e.getMessage());
          }
        } else {
          logger.warning("Tried to save a null id device for this project " + project.getId());
        }
      }
    }
    
    fillUp(project, "devices,lastRead");
    
    return project;
  }
  
  public Project addDevice(String id, Device device) throws BizException {
    
    Project project = null;
    
    ProjectDao pdao = new ProjectDao();
    DeviceBiz dbiz = new DeviceBiz();
    
    try {
      // Retrieves project
      project = pdao.get(id);
      project = this.get(id, "devices,lastRead");
    } catch (DaoException e) {
      throw new BizException("Project " + id + " not found", null, 404);
    }
    
    project.setModified(new Date());
    
    try {
      
      // dbiz.save(device);
      if (device != null) {
        if (device.getId() == null || "".equals(device.getId())) {
          // device.setId(Util.randomKey());
          device.setProjectId(project.getId());
          dbiz.save(device);
        } else {
          device = dbiz.get(device.getId(), null);
          device.setProjectId(project.getId());
          dbiz.update(device);
        }
      }
      
      project.addDevice(device);
      pdao.save(project);
    } catch (DaoException e) {
      throw new BizException(e);
    }
    
    // fillUp(project, "devices");
    
    return project;
  }
  
  /*
   * public Device delete(String id) throws DaoException {
   * 
   * DeviceDao dao = new DeviceDao(); dao.delete(id); return device; }
   */
  
  private Project fillUp(Project project, String filter) throws BizException {
    
    if (filter != null && !"".equals(filter)) {
      
      if ("*".equals(filter))
        filter = "devices,lastRead";
      
      if (filter.indexOf("devices") >= 0) {
        
        // Retrieves devices for project
        DeviceBiz dbiz = new DeviceBiz();
        List<Device> devices = dbiz.getByProjectId(project.getId(), filter);
        project.setDevices(devices);
      }
    }
    
    return project;
  }
  
}
