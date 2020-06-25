package com.greycells.dateone.client;

import com.greycells.dateone.shared.IUser;

public interface IUserContainer {

	public abstract IUser getUser();

	public abstract void setUser(IUser user);

}