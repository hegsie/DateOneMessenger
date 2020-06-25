package com.greycells.dateone.client.event;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.greycells.dateone.shared.IUser;

public class LogoutUserEvent extends GwtEvent<LogoutUserEvent.Handler> {
	public interface Handler extends EventHandler {
		void onLogoutUser(IUser user);
	}

	public static Type<LogoutUserEvent.Handler> TYPE = new Type<LogoutUserEvent.Handler>();

	private IUser _user;

	public LogoutUserEvent(IUser user) {
		_user = user;
	}

	@Override
	public Type<LogoutUserEvent.Handler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(LogoutUserEvent.Handler handler) {
		handler.onLogoutUser(_user);
	}
}
