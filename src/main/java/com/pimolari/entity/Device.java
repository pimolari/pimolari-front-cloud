package com.pimolari.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.google.gson.annotations.Expose;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;


@Entity
@XmlRootElement
@XmlAccessorType(XmlAccessType.NONE)
public class Device implements Serializable {

	
	@Id @Index  @XmlElement @Expose 
	public String id = null;
	
	@XmlElement @Expose
	public String name = null;
	
	@XmlElement @Expose
	public String description = null;
	
	@Index @XmlElement @Expose
	public String projectId = null;
	
	@XmlElement @Expose
	public String type = null;
	
	@XmlElement @Expose
	public Date created = null;
	
	@XmlElement @Expose
	public Date modified = null;
	
	@XmlElement @Expose
	public boolean active = true;
	
	@XmlElement @Expose //@Embedded
	public List<Param> params;
	
        @XmlElement @Expose //@Transient
	public IncomingReading lastRead = null;

	public Device() {
		this.setCreated(new Date());
		this.setModified(new Date());
	}
	
	/*public Device(String id) {
		this.id = id;
		this.setCreated(new Date());
		this.setModified(new Date());
	}*/
	
	public Device(String name) {
		this.name = name;
		this.setCreated(new Date());
		this.setModified(new Date());
	}
	
	public Device(String name, String description) {
		this.name = name;
		this.description = description;
		this.setCreated(new Date());
		this.setModified(new Date());
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

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getProjectId() {
		return projectId;
	}

	public void setProjectId(String projectId) {
		this.projectId = projectId;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
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

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public IncomingReading getLastRead() {
		return lastRead;
	}

	public void setLastRead(IncomingReading lastRead) {
		this.lastRead = lastRead;
	}
	
	public List<Param> getParams() {
	  return params;
	}

	public void setParams(List<Param> params) {
	  this.params = params;
	}
	
	public void addParam(Param param) {
	  if (this.params == null)
	    this.params = new ArrayList<Param>();
	  
	  this.params.add(param);
	}
	
	public void removeParam(String key) {
	  if (this.params != null && this.params.size() > 0) {
	    List<Param> _params = new ArrayList<Param>();
	    for (Param param : this.params) {
	      if (!param.getKey().equals(key)) {
	        _params.add(param);
	      } 
	    }
	  }
	}
	
	public void removeParams() {
	  this.params = new ArrayList<Param>();
	}
}
