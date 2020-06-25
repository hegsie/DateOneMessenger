package com.greycells.dateone.shared;

import java.util.Date;

public interface IUser {

	public String getFullName();

	public String getPhotoFileId();

	public void setPhotoFileId(String photoFileId);

	public String getPwdHash();

	public void setPwdHash(String pwdHash);

	public InterestedIn getInterestedIn();

	public void setInterestedIn(InterestedIn interestedIn);

	public Gender getGender();

	public void setGender(Gender gender);

	public Date getDob();

	public void setDob(Date dob);

	public Address getAddress();

	public void setAddress(Address address);

	public String getFirstName();

	public void setFirstName(String firstName);

	public String getLastName();

	public void setLastName(String lastName);

	public String getEmail();

	public void setEmail(String email);

	public String getUsername();

	public void setUsername(String username);

	public long getLastLogin();

	public void setLastLogin(long lastLogin);

	public String getSession();

	public void setSession(String session);

	public Boolean getLoggedIn();

	public void setLoggedIn(Boolean loggedIn);

	public void setWaveId(String waveId);

	public String getWaveId();

}