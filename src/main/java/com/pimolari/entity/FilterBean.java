package com.pimolari.entity;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * @version 1.0
 * @author Gerardo Mongelli
 */
@SuppressWarnings("serial")
public class FilterBean implements Serializable {

	private Map<String, ? super Object> filter;

	private String order;
	
	private String orderBy;

	private String pageSize;

	private String page;

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public FilterBean() {
		super();
		filter = new HashMap<String, Object>();
	}

	/**
	 * @param filter
	 * @param order
	 * @param pageSize
	 * @param page
	 */
	public FilterBean(Map<String, Object> filter, String order, String pageSize, String page) {
		super();
		this.filter = filter;
		this.order = order;
		this.pageSize = pageSize;
		this.page = page;
	}

	/**
	 * @return the filter
	 */
	public Map<String, ?> getFilter() {
		return filter;
	}

	/**
	 * @return the order
	 */
	public String getOrder() {
		return order;
	}
	/**
	 * @return the order
	 */
	public String getOrderBy() {
		return orderBy;
	}

	/**
	 * @return the pageSize
	 */
	public String getPageSize() {
		return pageSize;
	}

	/**
	 * @return the page
	 */
	public String getPage() {
		return page;
	}

	/**
	 * @param filter
	 * the filer to set
	 */
	public void setFilter(Map<String, Object> filter) {
		this.filter = filter;
	}

	/**
	 * @param order
	 * the order to set
	 */
	public FilterBean setOrder(String order) {
		this.order = order;
		return this;
	}
	
	/**
	 * @param orderBy
	 * the order to set
	 */
	public FilterBean setOrderBy(String order) {
		this.orderBy = order;
		return this;
	}
	/**
	 * @param pageSize
	 * the pageSize to set
	 */
	public FilterBean setPageSize(String pageSize) {
		this.pageSize = pageSize;
		return this;
	}

	/**
	 * @param page
	 * the page to set
	 */
	public FilterBean setPage(String page) {
		this.page = page;
		return this;
	}
	
	public FilterBean add(String key, Object value) {
		this.filter.put(key, value);
		return this;
	}
	
}
