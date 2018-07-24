package com.pimolari.entity;

import java.io.Serializable;
import java.util.Date;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.google.gson.annotations.Expose;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;


@Entity
@XmlRootElement
@XmlAccessorType(XmlAccessType.NONE)
public class IncomingReading implements Serializable {

	
	@Id @XmlElement @Expose
	public String id = null;
	
	@XmlElement @Expose
	public Date received = null;
	
	@XmlElement @Expose
	public String projectId = null;
	
	@XmlElement @Expose
	public String deviceId = null;
	
	@XmlElement @Expose
	public String deviceType = null;
	
	@XmlElement @Expose
	public String data = null;

	@XmlElement @Expose
	public Date created = null;
	
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
 
	public Date getReceived() {
		return received;
	}

	public void setReceived(Date received) {
		this.received = received;
	}

	public String getProjectId() {
		return projectId;
	}

	public void setProjectId(String projectId) {
		this.projectId = projectId;
	}

	public String getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}

	public String getDeviceType() {
		return deviceType;
	}

	public void setDeviceType(String deviceType) {
		this.deviceType = deviceType;
	}

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}

	public Date getCreated() {
		return created;
	}

	public void setCreated(Date created) {
		this.created = created;
	}
	
	
	
	
}
