package com.greycells.dateone.client.event;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;

public class LoginUserEvent extends GwtEvent<LoginUserEvent.Handler> {
	public interface Handler extends EventHandler {
		void onLoginUser(String username, String password);
	}

	public static Type<LoginUserEvent.Handler> TYPE = new Type<LoginUserEvent.Handler>();

	private String _username;
	private String _password;

	public LoginUserEvent(String username, String password) {
		_username = username;
		_password = password;
	}

	@Override
	public Type<LoginUserEvent.Handler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(LoginUserEvent.Handler handler) {
		handler.onLoginUser(_username, _password);
	}

}
