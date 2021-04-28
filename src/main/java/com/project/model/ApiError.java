package com.project.model;

public class ApiError {

	int status;
	String message;
	String path;
	
	
	public ApiError(int status,String message,String path) {
		super();
		this.status=status;
		this.message=message;
		this.path=path;
		
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}
}
