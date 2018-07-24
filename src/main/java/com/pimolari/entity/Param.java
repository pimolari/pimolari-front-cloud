package com.pimolari.entity;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.google.gson.annotations.Expose;


@XmlRootElement
@XmlAccessorType(XmlAccessType.NONE)
public class Param implements Serializable {

	@XmlElement @Expose
	public String key = null;
	
	@XmlElement @Expose
	public String name = null;
	
	@XmlElement @Expose
	public String showLabel = null;
	
	@XmlElement @Expose
	public String showValue = null;
	
	@XmlElement @Expose
	public String type = null;
	
	
	public Param() {
	}
	
	public Param(String key, String name, String type, String showValue, String showLabel) {
		this.key = key;
		this.name = name; 
		this.type = type;
		this.showValue = showValue;
		this.showLabel = showLabel;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getShowLabel() {
		return showLabel;
	}

	public void setShowLabel(String showLabel) {
		this.showLabel = showLabel;
	}

	public String getShowValue() {
		return showValue;
	}

	public void setShowValue(String showValue) {
		this.showValue = showValue;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
}
