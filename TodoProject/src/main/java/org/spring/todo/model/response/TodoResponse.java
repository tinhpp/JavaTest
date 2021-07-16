package org.spring.todo.model.response;

import java.util.Date;

/**
 * TodoResponse
 * 
 * @author TinhPP
 *
 */
public class TodoResponse {
	/**
	 * ID of TODO
	 */
	private int id;
	/**
	 * Work Name
	 */
	private String workName;
	/**
	 * Starting Date
	 */
	private Date startingDate;
	/**
	 * Ending Date
	 */
	private Date endingDate;
	/**
	 * Status
	 */
	private String status;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getWorkName() {
		return workName;
	}
	public void setWorkName(String workName) {
		this.workName = workName;
	}
	public Date getStartingDate() {
		return startingDate;
	}
	public void setStartingDate(Date startingDate) {
		this.startingDate = startingDate;
	}
	public Date getEndingDate() {
		return endingDate;
	}
	public void setEndingDate(Date endingDate) {
		this.endingDate = endingDate;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
}
