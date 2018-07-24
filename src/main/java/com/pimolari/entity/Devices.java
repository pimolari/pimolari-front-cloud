package com.pimolari.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;



@XmlRootElement
@XmlAccessorType(XmlAccessType.NONE)
public class Devices implements Serializable {

	private List<Device> devices = null;
	

	public Devices() {
		this.devices = new ArrayList<Device>();
	}
	
	public Devices(List<Device> devices) {
		this.devices = devices;
	}

	public List<Device> getDevices() {
		return devices;
	}

	public void setDevices(List<Device> devices) {
		this.devices = devices;
	}
	
	public void add(Device device) {
		this.devices.add(device);
	}

	
}
