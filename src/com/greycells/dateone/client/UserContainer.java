package com.greycells.dateone.client;

import com.greycells.dateone.shared.IUser;

public class UserContainer implements IUserContainer {

	private IUser user;

	/* (non-Javadoc)
	 * @see com.greycells.dateone.client.IUserContainer#getUser()
	 */
	@Override
	public IUser getUser() {
		return user;
	}

	/* (non-Javadoc)
	 * @see com.greycells.dateone.client.IUserContainer#setUser(com.greycells.dateone.shared.IUser)
	 */
	@Override
	public void setUser(IUser user) {
		this.user = user;
	}
}
