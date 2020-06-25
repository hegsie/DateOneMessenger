package com.greycells.dateone.shared;

import java.io.Serializable;

public class MessageStatus implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 7072741996742823433L;
	
	public enum Statuses {
		Success,
		Warning,
		Error
	}
	
	public String Message;
	public Statuses Status;
}
