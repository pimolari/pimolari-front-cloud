package com.pimolari.ws;

import java.util.List;
import java.util.logging.Logger;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import com.google.gson.Gson;
import com.pimolari.biz.DeviceBiz;
import com.pimolari.biz.IncomingReadingBiz;
import com.pimolari.biz.ProjectBiz;
import com.pimolari.entity.Device;
import com.pimolari.entity.IncomingReading;
import com.pimolari.entity.Project;
import com.pimolari.exception.BizException;

//TODO: Javadoc
@Path("/projects")
public class ProjectWs {
  
  private static Logger logger = Logger.getLogger(ProjectWs.class.getName());
  
  @GET
  @Path("/ping")
  public Response ping() {
    return Response.ok("Project service running...").header("Access-Control-Allow-Origin", "*").build();
  }
  
  @GET
  @Path("/{id}")
  public Response get(@PathParam("id") String id) {
    
    Project project = null;
    ProjectBiz pbiz = new ProjectBiz();
    
    try {
      project = pbiz.get(id, "devices,lastRead");
      
    } catch (BizException e) {
      // e.printStackTrace();
      logger.info(e.getMessage());
      return Response.status(e.getCode()).build();
    }
    
    return Response.ok().entity(project).header("Access-Control-Allow-Origin", "*").build();
  }
  
  @GET
  @Path("/{id}/monitor")
  public Response monitor(@PathParam("id") String id) {
    
    List<Device> devices = null;
    ProjectBiz pbiz = new ProjectBiz();
    
    try {
      devices = pbiz.monitor(id);
      
    } catch (BizException e) {
      // e.printStackTrace();
      logger.info(e.getMessage());
      return Response.status(e.getCode()).build();
    }
    
    return Response.ok().entity(devices).header("Access-Control-Allow-Origin", "*").build();
  }
  
  @GET
  @Path("/{id}/readings")
  public Response getReadings(@PathParam("id") String id) {
    
    IncomingReadingBiz irbiz = new IncomingReadingBiz();
    List<IncomingReading> readings = null;
    
    try {
      readings = irbiz.listByProject(id);
    } catch (BizException e) {
      logger.info(e.getDescription());
      return Response.status(e.getCode()).header("Access-Control-Allow-Origin", "*").build();
    }
    
    return Response.ok().entity(readings).build();
  }
  
  @GET
  @Produces(MediaType.APPLICATION_JSON)
  @Path("/")
  public Response list() {
    
    // ProjectDao pdao = new ProjectDao();
    // DeviceDao ddao = new DeviceDao();
    ProjectBiz pbiz = new ProjectBiz();
    DeviceBiz dbiz = new DeviceBiz();
    
    List<Project> projects = null;
    
    try {
      // projects = pdao.list(null);
      projects = pbiz.list("devices,lastRead");
    } catch (BizException e) {
      e.printStackTrace();
      logger.info(e.getDescription());
      return Response.status(e.getCode()).build();
    }
    
    return Response.ok().entity(projects).header("Access-Control-Allow-Origin", "*").build();
  }
  
  /**
   * Saves a project
   * 
   * @param String
   *          data The request data
   * @return Response
   */
  @POST
  @Path("/")
  public Response save(String p) {
    
    logger.info("Project to save: " + p);
    
    Project project = new Gson().fromJson(p, Project.class);
    // ProjectDao pdao = new ProjectDao();
    ProjectBiz pbiz = new ProjectBiz();
    
    try {
      pbiz.save(project);
    } catch (BizException e) {
      e.printStackTrace();
      logger.info(e.getDescription());
      return Response.status(e.getCode()).build();
    }
    return Response.ok(project).header("Access-Control-Allow-Origin", "*").build();
  }
  
  /**
   * Adds a device to a project and saves project
   * 
   * @param String
   *          id Project id
   * @param String
   *          d The device json serial representation
   * @return Response
   */
  @POST
  @Path("/{id}/devices")
  public Response addDevice(@PathParam("id") String id, String d) {
    
    logger.info("Adds device to project: " + id);
    
    if (d != null && !"".equals(d) && id != null && !"".equals(id)) {
      Project project = null;
      Device device = new Gson().fromJson(d, Device.class);
      ProjectBiz pbiz = new ProjectBiz();
      
      try {
        project = pbiz.addDevice(id, device);
      } catch (BizException e) {
        logger.fine(e.getDescription());
        return Response.status(e.getCode()).build();
      }
      
      return Response.ok(project).build();
    }
    
    return Response.status(Status.INTERNAL_SERVER_ERROR).build();
  }
  
  /**
   * Returns the latest project readings by device
   * 
   * @param String
   *          id Project id
   * @return Response List<IncomingReading>
   */
  /*
   * @GET
   * 
   * @Path("/{id}/monitor") public Response monitor(@PathParam("id") String id)
   * {
   * 
   * logger.info("Retrieving project readings to monitor. Project Id: " + id);
   * 
   * IncomingReadingDao dao = new IncomingReadingDao(); IncomingReadings
   * readings = null;
   * 
   * try { readings = new IncomingReadings(dao.listByProject(id)); }
   * catch(DaoException e) { e.printStackTrace();
   * logger.info(e.getDescription()); return
   * Response.status(e.getCode()).build(); }
   * 
   * return Response.ok(readings).build(); }
   */
}
