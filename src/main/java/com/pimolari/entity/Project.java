package com.pimolari.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.google.gson.Gson;
import com.google.gson.annotations.Expose;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;

@Entity
@XmlRootElement
@XmlAccessorType(XmlAccessType.NONE)
public class Project implements Serializable {

	
	@Id @XmlElement @Expose
	public String id = null;
	
	@XmlElement @Expose
	public String name = null;
	
	@XmlElement @Expose
	public Date created = null;
	
	@XmlElement @Expose
	public Date modified = null;
	
	@XmlElement @Expose //@Embedded @Transient
	public List<Device> devices = null;
	
	//@XmlElement @Expose
	//public List<String> deviceIds = null;
	
	@XmlElement @Expose
	public boolean active = true;
	
	public Project() {
		this.setCreated(new Date());
		this.setModified(new Date());
		this.devices = new ArrayList<Device>();
		//this.deviceIds = new ArrayList<String>();
	}
		
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Date getCreated() {
		return created;
	}

	public void setCreated(Date created) {
		this.created = created;
	}

	public Date getModified() {
		return modified;
	}

	public void setModified(Date modified) {
		this.modified = modified;
	}

	public List<Device> getDevices() {
		return devices;
	}
	
	public void setDevices(List<Device> devices) {
		this.devices = devices;
	}

	public void addDevice(Device device) {
		this.devices.add(device);
		//this.deviceIds.add(device.getId());
	}
	
	/*public void flushDevices() {
		this.deviceIds = new ArrayList<String>();
		for (Device device : devices) {
			this.deviceIds.add(device.getId());
		}
	}*/
	
	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	/*public List<String> getDeviceIds() {
		return deviceIds;
	}

	public void setDeviceIds(List<String> deviceIds) {
		this.deviceIds = deviceIds;
	}*/
	
	public void addDeviceId(String id) {
		this.addDeviceId(id);
	}

	public static void main(String[] args) {
		Project p = new Project();
		
		p.setId("");
		p.setCreated(new Date());
		p.setModified(new Date());
		p.setName("Test Project");
		p.addDevice(new Device("Test Device 1", "Device description"));
		p.addDevice(new Device("Test Device 2", "Device description"));
		
		System.out.println(new Gson().toJson(p));
	}
	
	
}
