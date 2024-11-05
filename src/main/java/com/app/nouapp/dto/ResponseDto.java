package com.app.nouapp.dto;

public class ResponseDto {

	private long enrollment;
	private String name;
	private String program;
	private String branch;
	private String year;
	private String responsetype;
	private String responsetext;
	private String posteddate;
	public long getEnrollment() {
		return enrollment;
	}
	public void setEnrollment(long enrollment) {
		this.enrollment = enrollment;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getProgram() {
		return program;
	}
	public void setProgram(String program) {
		this.program = program;
	}
	public String getBranch() {
		return branch;
	}
	public void setBranch(String branch) {
		this.branch = branch;
	}
	public String getYear() {
		return year;
	}
	public void setYear(String year) {
		this.year = year;
	}
	public String getResponsetype() {
		return responsetype;
	}
	public void setResponsetype(String responsetype) {
		this.responsetype = responsetype;
	}
	public String getResponsetext() {
		return responsetext;
	}
	public void setResponsetext(String responsetext) {
		this.responsetext = responsetext;
	}
	public String getPosteddate() {
		return posteddate;
	}
	public void setPosteddate(String posteddate) {
		this.posteddate = posteddate;
	}

}
