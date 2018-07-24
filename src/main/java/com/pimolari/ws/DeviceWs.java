package com.pimolari.ws;

import java.util.List;
import java.util.logging.Logger;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.google.gson.Gson;
import com.pimolari.biz.DeviceBiz;
import com.pimolari.biz.IncomingReadingBiz;
import com.pimolari.entity.Device;
import com.pimolari.entity.IncomingReading;
import com.pimolari.entity.Param;
import com.pimolari.exception.BizException;


//TODO: Javadoc
@Path("/devices")
public class DeviceWs {

    private static Logger logger = Logger.getLogger(DeviceWs.class.getName());

    @GET
    @Path("/ping")
    public Response ping() {
      
      String[] s = new String[10];
      
      s[10] = "";
      
      
    	return Response.ok("Device service running...").header("Access-Control-Allow-Origin","*").build();
    }
    
    @GET
    @Path("/{id}")
    public Response get(@PathParam("id") String id) {
    	
    	Device device = null;
    	//DeviceDao dao = new DeviceDao();
    	DeviceBiz dbiz = new DeviceBiz();
    	
    	try {
    		device = dbiz.get(id, "lastRead");
    		//device = dao.get(id);
    	} catch(BizException e) {
    		e.printStackTrace();
    		logger.info(e.getDescription());
    		return Response.status(e.getCode()).build();
    	}
    	
    	return Response.ok().entity(device).header("Access-Control-Allow-Origin","*").build();
    }
    
    @GET
    @Path("/{id}/readings")
    public Response getReadings(@PathParam("id") String id, @QueryParam("p") String page, @QueryParam("psize") String pageSize) {
    	
    	IncomingReadingBiz irbiz = new IncomingReadingBiz();
    	List<IncomingReading> readings = null;
    	
    	page = (page == null || page.equals("")) ? "0" : page;
    	pageSize = (pageSize == null || pageSize.equals("")) ? "10" : pageSize;
    	
    	try {
    		readings = irbiz.listByDevice(id, page, pageSize);
    	} catch(BizException e) {
    		logger.info(e.getDescription());
    		return Response.status(e.getCode()).build();
    	}
    	
    	return Response.ok().entity(readings).header("Access-Control-Allow-Origin","*").build();
    }
    
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/")
    public Response list() {
    	
    	List<Device> devices = null;
    	//DeviceDao ddao = new DeviceDao();
    	DeviceBiz dbiz = new DeviceBiz();
    	
    	try {
    		devices = dbiz.list(null);
    	} catch(BizException e) {
    		e.printStackTrace();
    		logger.info(e.getDescription());
    		return Response.status(e.getCode()).build();
    	}
    	
    	return Response.ok().entity(devices).header("Access-Control-Allow-Origin","*").build();
    }
    
    /**
     * Saves a device object
     * @param String id
     * @return Response
     */
    @POST
    @Path("/")
    public Response post(String d) {
    	
    	logger.info("Device to save: " + d);
    	
    	Device device = new Gson().fromJson(d, Device.class);
    	//DeviceDao dao = new DeviceDao();
    	DeviceBiz dbiz = new DeviceBiz();
    	
    	try {
    		dbiz.save(device);
    	} catch(BizException e) {
    		e.printStackTrace();
    		logger.info(e.getDescription());
    		return Response.status(e.getCode()).build();
    	}
    	
    	return Response.ok(device).header("Access-Control-Allow-Origin","*").build();
    }
    
    /**
     * Saves a device object
     * @param String id
     * @return Response
     */
    @PUT
    @Path("/")
    public Response put(String d) {
    	
    	logger.info("Device to update: " + d);
    	
    	Device device = new Gson().fromJson(d, Device.class);
    	DeviceBiz dbiz = new DeviceBiz();
    	
    	try {
    		dbiz.update(device);
    	} catch(BizException e) {
    		e.printStackTrace();
    		logger.info(e.getDescription());
    		return Response.status(e.getCode()).build();
    	}
    	
    	return Response.ok(device).header("Access-Control-Allow-Origin","*").build();
    }
    
    /**
     * Updates a device by adding a new param
     * @param String id
     * @param Param param
     * @return Response
     */
    @PUT
    @Path("/{id}")
    public Response put(@PathParam("id") String id, String param) {
        
        logger.info("Device to update: " + id + " adding param: " + param);
        
        Param p = new Gson().fromJson(param, Param.class);
        DeviceBiz dbiz = new DeviceBiz();
        Device device = null;
        try {
          device = dbiz.addParam(id, p);
        } catch(BizException e) {
                e.printStackTrace();
                logger.info(e.getDescription());
                return Response.status(e.getCode()).build();
        }
        
        return Response.ok(device).header("Access-Control-Allow-Origin","*").build();
    }
}
