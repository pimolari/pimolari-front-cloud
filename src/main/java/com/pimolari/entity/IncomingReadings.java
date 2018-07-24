package com.pimolari.entity;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;



@XmlRootElement
@XmlAccessorType(XmlAccessType.NONE)
public class IncomingReadings {

	private List<IncomingReading> readings = null;
	
	public IncomingReadings() {
		
	}
	
	public IncomingReadings(List<IncomingReading> readings) {
		this.readings = readings;
	}

	public List<IncomingReading> getReadings() {
		return readings;
	}

	public void setReadings(List<IncomingReading> readings) {
		this.readings = readings;
	}
	
	
	
}
