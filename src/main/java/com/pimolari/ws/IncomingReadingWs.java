package com.pimolari.ws;

import java.util.logging.Logger;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;

import com.google.gson.Gson;
import com.pimolari.biz.IncomingReadingBiz;
import com.pimolari.entity.IncomingReading;
import com.pimolari.entity.IncomingReadings;
import com.pimolari.exception.BizException;

//TODO: Javadoc
@Path("/readings")
public class IncomingReadingWs {

    private static Logger logger = Logger.getLogger(IncomingReadingWs.class.getName());

    @GET
    @Path("/ping")
    public Response ping() {
    	return Response.ok("Service running...").build();
    }
    
    @GET
    @Path("/{id}")
    public Response get(@PathParam("id") String id) {
    	
    	IncomingReading reading = null;
    	//IncomingReadingDao dao = new IncomingReadingDao();
    	IncomingReadingBiz rbiz = new IncomingReadingBiz();
    	
    	try {
    		reading = rbiz.get(id, null);
    	} catch(BizException e) {
    		e.printStackTrace();
    		logger.info(e.getDescription());
    		return Response.status(e.getCode()).build();
    	}
    	
    	return Response.ok().entity(reading).build();
    }
    
    /**
     * Lists reading request by project
     * @param String id the project id
     * @return Response
     */
    @GET
    @Path("/")
    public Response list(String id) {

    	System.out.println("Lists readings for project " + id);
    	logger.info("Lists readings for project " + id);
    	
    	//IncomingReadingDao dao = new IncomingReadingDao();
    	IncomingReadingBiz rbiz = new IncomingReadingBiz();
    	IncomingReadings readings = null;
    	
    	try {
    		readings = new IncomingReadings(rbiz.listByProject(id));
    	} catch(BizException e) {
    		e.printStackTrace();
    		logger.info(e.getDescription());
    		return Response.status(e.getCode()).build();
    	}
    	
    	return Response.ok(readings).build();
    }
    
    /**
     * Saves a reading request sent by a device
     * @param String data The request data
     * @return Response
     */
    @POST
    @Path("/")
    public Response save(String data) {
    	
    	System.out.println("Incoming reading received: " + data);
    	logger.info("Incoming reading received: " + data);
    	
    	IncomingReadingBiz irbiz = new IncomingReadingBiz();
    	IncomingReading reading = new Gson().fromJson(data, IncomingReading.class);
    	
    	try {
    		irbiz.save(reading);
    	} catch(BizException e) {
    		e.printStackTrace();
    		logger.info(e.getDescription());
    		return Response.status(e.getCode()).entity("Received KO").build();
    	}
    	
    	return Response.ok("Received OK").build();
    }
}
