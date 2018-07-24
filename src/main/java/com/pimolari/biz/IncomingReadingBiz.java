package com.pimolari.biz;

import java.util.Date;
import java.util.List;

import com.pimolari.dao.IncomingReadingDao;
import com.pimolari.entity.Device;
import com.pimolari.entity.FilterBean;
import com.pimolari.entity.IncomingReading;
import com.pimolari.exception.BaseException;
import com.pimolari.exception.BizException;
import com.pimolari.exception.DaoException;
import com.pimolari.util.Util;

public class IncomingReadingBiz {
	
	public IncomingReading get(String id, String filter) throws BizException {
		
		
		IncomingReadingDao rdao = new IncomingReadingDao();
		IncomingReading reading = null;
		
		try {
			reading = rdao.get(id);
			fillUp(reading, filter);	
		} catch(DaoException e) {
			throw new BizException(e);
		}
		
		return reading;
	}
	
	public List<IncomingReading> list(String projects) throws BizException {

		IncomingReadingDao rdao = new IncomingReadingDao();

		List<IncomingReading> l = null;
		
		try {
			l = rdao.list(null);
				
			/*for (IncomingReading reading : l) {
				fillUp(reading, null);
			}*/	
		} catch(DaoException e) {
			throw new BizException(e);
		}
		
		return l;
	}
	
	public List<IncomingReading> listByProject(String id) throws BizException {
		
		if (id == null || "".equals(id))
			throw new BizException("Project id is null, cannot retrieve readings", BaseException.ERROR, 500);
		
		IncomingReadingDao rdao = new IncomingReadingDao();

		List<IncomingReading> l = null;
		
		try {
			l = rdao.list(new FilterBean().add("projectId", id));

			/*for (IncomingReading reading : l) {
				fillUp(reading, null);
			}*/	
		} catch(DaoException e) {
			throw new BizException(e);
		}
		
		return l;
	}
	
	public List<IncomingReading> listByDevice(String id, String page, String pageSize) throws BizException {

		if (id == null || "".equals(id))
			throw new BizException("Device id is null, cannot retrieve readings", BaseException.ERROR, 500);
		
		IncomingReadingDao rdao = new IncomingReadingDao();

		List<IncomingReading> l = null;
		
		try {
			l = rdao.list(new FilterBean().add("deviceId", id).setPage(page).setPageSize(pageSize).setOrder("desc").setOrderBy("received"));

			/*for (IncomingReading reading : l) {
				fillUp(reading, null);
			}*/	
		} catch(DaoException e) {
			throw new BizException(e);
		}
		
		return l;
	}
	
	public IncomingReading lastByDevice(String id) throws BizException {

		if (id == null || "".equals(id))
			throw new BizException("Device id is null, cannot retrieve readings", BaseException.ERROR, 500);
		
		IncomingReadingDao rdao = new IncomingReadingDao();

		List<IncomingReading> l = null;
		
		try {
			l = rdao.list(new FilterBean().add("deviceId", id).setPage("1").setPageSize("1"));

			if (l != null && l.size() > 0)
				return l.get(0);
		} catch(DaoException e) {
			throw new BizException(e);
		}
		
		return null;
	}
	
	
	
	public IncomingReading save(IncomingReading reading) throws BizException {
		
		if (reading == null) 
			throw new BizException("Reading can not be null", null, 500);
		
		if (reading.getId() == null || "".equals(reading.getId())) {
			reading.setId(Util.randomKey());
			reading.setCreated(new Date());
			reading.setReceived(new Date());
		}
		
		ProjectBiz pbiz = new ProjectBiz();
		DeviceBiz dbiz = new DeviceBiz();
		Device device = null;
		
		try {
			//Project project = pbiz.get(reading.getProjectId(), "devices");
			
			device = dbiz.get(reading.getDeviceId(), null);
			
			//if (device == null) {
				// Device does not exist
			//	throw new BizException("Unknow device", "SEVERE", 404);
			//} else {
				if (!device.getProjectId().equals(reading.getProjectId())) {
					// Project and device do not match
					throw new BizException("Device and project do not match", BaseException.ERROR, 403);
				}
				//}
			
		} catch(BizException e) {
			throw e;
		}
		
		try {
			IncomingReadingDao rdao = new IncomingReadingDao();
			device.setLastRead(reading);
			rdao.save(reading, device);
			fillUp(reading, null); // TODO: Does not perform any action
		} catch(DaoException e) {
			throw new BizException(e);
		}
		return reading;
	}
	
	public IncomingReading update(IncomingReading reading) throws BizException {
		
		if (reading == null) 
			throw new BizException("Reading can not be null", null, 500);
		
		if (reading.getId() == null || "".equals(reading.getId()))
			throw new BizException("Reading id can not be null", null, 500);
		
		return this.save(reading);
		
		/*try {
			IncomingReadingDao rdao = new IncomingReadingDao();
			rdao.save(reading);
			
		} catch(DaoException e) {
			throw new BizException(e);
		}
		
		return reading;*/
	}
	


	/*public Device delete(String id) throws DaoException {

		DeviceDao dao = new DeviceDao();
		dao.delete(id);
		return device;
	}*/
	
	private IncomingReading fillUp(IncomingReading reading, String filter) throws BizException {
		
		
		
		return reading;
	}
	
}
