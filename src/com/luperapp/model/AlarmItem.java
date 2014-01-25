package com.luperapp.model;

import java.io.Serializable;
import java.util.ArrayList;


public class AlarmItem implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -225608816594124749L;
	private Integer alarmId;
	private Integer period;
	private String name;
	private String email;
	private String phone;
	private String nextContactTime;
	private String lastContactTime;
	ArrayList<String> notes;
	private boolean activate;
	private boolean phoneEnable;
	private boolean emailEnable;
	private boolean textEnable;
	
	
	public Integer getAlarmId() {
		return alarmId;
	}
	public void setAlarmId(int alarmId) {
		this.alarmId = alarmId;
	}
	
	public Integer getPeriod() {
		return period;
	}
	public void setPeriod(int period) {
		this.period = period;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getNextContactTime() {
		return nextContactTime;
	}
	public void setNextContactTime(String nextContactTime) {
		this.nextContactTime = nextContactTime;
	}

	public boolean isActivate() {
		//return activate;
		return true;
	}
	public void setActivate(boolean activate) {
		this.activate = activate;
	}
	public String getLastContactTime() {
		return lastContactTime;
	}
	public void setLastContactTime(String lastContactTime) {
		this.lastContactTime = lastContactTime;
	}
	
	public boolean isPhoneEnable() {
		return phoneEnable;
	}
	public ArrayList<String> getNotes()
	{
		return notes;
	}
	public void setNotes(ArrayList<String> notes)
	{
		this.notes = notes;
	}
	
	public void setPhoneEnable(boolean phoneEnable) {
		this.phoneEnable = phoneEnable;
	}
	public boolean isEmailEnable() {
		return emailEnable;
	}
	public void setEmailEnable(boolean emailEnable) {
		this.emailEnable = emailEnable;
	}
	public boolean isTextEnable() {
		return textEnable;
	}
	public void setTextEnable(boolean textEnable) {
		this.textEnable = textEnable;
	}
	
	
	public AlarmItem( String name, String phone, String email, int period)
	{		
		this.name= name;
		this.phone = phone;
		this.email = email;
		this.period = period;
		setId();
	}
	private void setId() {
		int id = period*1000;
		
		
		for(int i=0; i<name.length(); i++)
		{
			id+=name.charAt(i);
		}
		
		setAlarmId(id);
	}
	
}
