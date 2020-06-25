package com.greycells.dateone.client.event;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.greycells.dateone.shared.IUser;

public class RegisterUserEvent extends GwtEvent<RegisterUserEvent.Handler> {

	public interface Handler extends EventHandler {
		void onRegisterUser(IUser user, String password);
	}

	public static Type<RegisterUserEvent.Handler> TYPE = new Type<RegisterUserEvent.Handler>();

	private IUser _user;
	private String _password;

	public RegisterUserEvent(IUser user, String password) {
		_user = user;
		_password = password;
	}

	@Override
	public Type<RegisterUserEvent.Handler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(RegisterUserEvent.Handler handler) {
		handler.onRegisterUser(_user, _password);
	}
}
