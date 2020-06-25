package com.greycells.dateone.client.event;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.greycells.dateone.shared.IUser;
import com.greycells.dateone.shared.UserFromSearch;

public class UserPingEvent extends GwtEvent<UserPingEvent.Handler> {

	public interface Handler extends EventHandler {
		void displayUser(UserFromSearch user);
	}

	public static Type<UserPingEvent.Handler> TYPE = new Type<UserPingEvent.Handler>();

	private UserFromSearch _user;

	public UserPingEvent(UserFromSearch user) {
		_user = user;
	}

	@Override
	public Type<UserPingEvent.Handler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(UserPingEvent.Handler handler) {
		handler.displayUser(_user);
	}
}