package com.greycells.dateone.client.activity;

import com.google.gwt.activity.shared.AbstractActivity;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.web.bindery.event.shared.EventBus;

public abstract class ActivityBase extends AbstractActivity {
	// Forward to the web.bindery EventBus instead
	@Override
	public void start(AcceptsOneWidget panel, com.google.gwt.event.shared.EventBus eventBus) {
	   start(panel, (EventBus)eventBus);
	}
	
	@Override
	public void onStop(){
		stop();
	}

	public abstract void stop();

	public abstract void start(AcceptsOneWidget panel, EventBus eventBus);
}

