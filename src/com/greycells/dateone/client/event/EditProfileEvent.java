package com.greycells.dateone.client.event;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;

public class EditProfileEvent extends GwtEvent<EditProfileEvent.Handler> {

	public interface Handler extends EventHandler {
		void onEditProflie();
	}

	public static Type<EditProfileEvent.Handler> TYPE = new Type<EditProfileEvent.Handler>();

	@Override
	public Type<EditProfileEvent.Handler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(EditProfileEvent.Handler handler) {
		handler.onEditProflie();
	}

}
