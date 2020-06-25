package com.greycells.dateone.client.event;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;

public class RegisterEvent extends GwtEvent<RegisterEvent.Handler> {

	public interface Handler extends EventHandler {
		void onRegister();
	}

	public static Type<RegisterEvent.Handler> TYPE = new Type<RegisterEvent.Handler>();

	public RegisterEvent() {
	}

	@Override
	public Type<RegisterEvent.Handler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(RegisterEvent.Handler handler) {
		handler.onRegister();
	}
}
