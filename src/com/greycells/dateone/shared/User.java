package com.greycells.dateone.shared;

import java.io.Serializable;
import java.util.Date;


@SuppressWarnings("serial")
public class User implements Serializable, IUser {

	public User(String firstName, String lastName, String email,
				Address address, Date dob, Gender gender,
				InterestedIn interestedIn, String pwdHash, long lastLogin,
				String photoFileId, String waveId) {
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
		this.address = address;
		this.dob = dob;
		this.gender = gender;
		this.interestedIn = interestedIn;
		this.pwdHash = pwdHash;
		this.lastLogin = lastLogin;
		this.photoFileId = photoFileId;
		this.waveId = waveId;
	}

	private String firstName;
	private String lastName;
	private String email;
	private String waveId;

	private Address address;
	private Date dob;
	private Gender gender;
	private InterestedIn interestedIn;
	private String pwdHash;
	private long lastLogin;

	private String photoFileId;
	private String session;
	private Boolean loggedIn;

	public User() {

	}

	public User( String firstName, String lastName,String email,String pwdHash) {
		setUsername(email);
		this.firstName = firstName;
		this.lastName = lastName;
		this.pwdHash = pwdHash;
	}

	public User(String username) {
		setUsername(username);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((email == null) ? 0 : email.hashCode());
		result = prime * result
				+ ((firstName == null) ? 0 : firstName.hashCode());
		result = prime * result
				+ ((lastName == null) ? 0 : lastName.hashCode());

		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		User other = (User) obj;
		if (email == null) {
			if (other.email != null)
				return false;
		} else if (!email.equals(other.email))
			return false;
		return true;
	}

	/* (non-Javadoc)
	 * @see com.greycells.dateone.shared.IUser#getFullName()
	 */
	@Override
	public String getFullName() {
		return firstName + " " + lastName;
	}

	/* (non-Javadoc)
	 * @see com.greycells.dateone.shared.IUser#getPhotoFileId()
	 */
	@Override
	public String getPhotoFileId() {
		return photoFileId;
	}

	/* (non-Javadoc)
	 * @see com.greycells.dateone.shared.IUser#setPhotoFileId(java.lang.String)
	 */
	@Override
	public void setPhotoFileId(String photoFileId) {
//		UpdateOperations<User> ops = datastore.createUpdateOperations(
//				User.class).set("photoFileId", photoFileId);
//
//		datastore.update(queryToFindMe(), ops);
		
		this.photoFileId = photoFileId;
	}

	/* (non-Javadoc)
	 * @see com.greycells.dateone.shared.IUser#getPwdHash()
	 */
	@Override
	public String getPwdHash() {
		return pwdHash;
	}

	/* (non-Javadoc)
	 * @see com.greycells.dateone.shared.IUser#setPwdHash(java.lang.String)
	 */
	@Override
	public void setPwdHash(String pwdHash) {
		this.pwdHash = pwdHash;
	}

	/* (non-Javadoc)
	 * @see com.greycells.dateone.shared.IUser#getInterestedIn()
	 */
	@Override
	public InterestedIn getInterestedIn() {
		return interestedIn;
	}

	/* (non-Javadoc)
	 * @see com.greycells.dateone.shared.IUser#setInterestedIn(com.greycells.dateone.shared.InterestedIn)
	 */
	@Override
	public void setInterestedIn(InterestedIn interestedIn) {
		this.interestedIn = interestedIn;
	}

	/* (non-Javadoc)
	 * @see com.greycells.dateone.shared.IUser#getGender()
	 */
	@Override
	public Gender getGender() {
		return gender;
	}

	/* (non-Javadoc)
	 * @see com.greycells.dateone.shared.IUser#setGender(com.greycells.dateone.shared.Gender)
	 */
	@Override
	public void setGender(Gender gender) {
		this.gender = gender;
	}

	/* (non-Javadoc)
	 * @see com.greycells.dateone.shared.IUser#getDob()
	 */
	@Override
	public Date getDob() {
		return dob;
	}

	/* (non-Javadoc)
	 * @see com.greycells.dateone.shared.IUser#setDob(java.util.Date)
	 */
	@Override
	public void setDob(Date dob) {
		this.dob = dob;
	}

	/* (non-Javadoc)
	 * @see com.greycells.dateone.shared.IUser#getAddress()
	 */
	@Override
	public Address getAddress() {
		return address;
	}

	/* (non-Javadoc)
	 * @see com.greycells.dateone.shared.IUser#setAddress(com.greycells.dateone.server.Address)
	 */
	@Override
	public void setAddress(Address address) {
		this.address = address;
	}

	/* (non-Javadoc)
	 * @see com.greycells.dateone.shared.IUser#getFirstName()
	 */
	@Override
	public String getFirstName() {
		return firstName;
	}

	/* (non-Javadoc)
	 * @see com.greycells.dateone.shared.IUser#setFirstName(java.lang.String)
	 */
	@Override
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	/* (non-Javadoc)
	 * @see com.greycells.dateone.shared.IUser#getLastName()
	 */
	@Override
	public String getLastName() {
		return lastName;
	}

	/* (non-Javadoc)
	 * @see com.greycells.dateone.shared.IUser#setLastName(java.lang.String)
	 */
	@Override
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	/* (non-Javadoc)
	 * @see com.greycells.dateone.shared.IUser#getEmail()
	 */
	@Override
	public String getEmail() {
		return email;
	}

	/* (non-Javadoc)
	 * @see com.greycells.dateone.shared.IUser#setEmail(java.lang.String)
	 */
	@Override
	public void setEmail(String email) {
		setUsername(email);
	}

	/* (non-Javadoc)
	 * @see com.greycells.dateone.shared.IUser#getUsername()
	 */
	@Override
	public String getUsername() {
		return email;
	}

	/* (non-Javadoc)
	 * @see com.greycells.dateone.shared.IUser#setUsername(java.lang.String)
	 */
	@Override
	public void setUsername(String username) {
		if (!username.contains("@") || !username.contains("."))
			throw new IllegalArgumentException(
					"The username must be an email address!");
		this.email = username;
	}


	/* (non-Javadoc)
	 * @see com.greycells.dateone.shared.IUser#getLastLogin()
	 */
	@Override
	public long getLastLogin() {
		return lastLogin;
	}

	/* (non-Javadoc)
	 * @see com.greycells.dateone.shared.IUser#setLastLogin(long)
	 */
	@Override
	public void setLastLogin(long lastLogin) {
		this.lastLogin = lastLogin;
	}
//
//	private Query<User> queryToFindMe() {
//		return datastore.createQuery(User.class).field(Mapper.ID_KEY).equal(getId());
//	}

	public void loggedIn() {
		long now = System.currentTimeMillis();
		loggedIn = true;
//		UpdateOperations<User> ops = datastore.createUpdateOperations(
//				User.class).set("lastLogin", now);
//
//		datastore.update(queryToFindMe(), ops);
		setLastLogin(now);
	}

	/* (non-Javadoc)
	 * @see com.greycells.dateone.shared.IUser#getSession()
	 */
	@Override
	public String getSession() {
		return session;
	}

	/* (non-Javadoc)
	 * @see com.greycells.dateone.shared.IUser#setSession(java.lang.String)
	 */
	@Override
	public void setSession(String session) {
		this.session = session;
	}

	/* (non-Javadoc)
	 * @see com.greycells.dateone.shared.IUser#getLoggedIn()
	 */
	@Override
	public Boolean getLoggedIn() {
		return loggedIn;
	}

	/* (non-Javadoc)
	 * @see com.greycells.dateone.shared.IUser#setLoggedIn(java.lang.Boolean)
	 */
	@Override
	public void setLoggedIn(Boolean loggedIn) {
		this.loggedIn = loggedIn;
	}

	/* (non-Javadoc)
	 * @see com.greycells.dateone.shared.IUser#getWaveId()
	 */
	@Override
	public String getWaveId() {
		return waveId;
	}

	/* (non-Javadoc)
	 * @see com.greycells.dateone.shared.IUser#setWaveId(java.lang.String)
	 */
	@Override
	public void setWaveId(String waveId) {
		this.waveId = waveId;
	}
}
