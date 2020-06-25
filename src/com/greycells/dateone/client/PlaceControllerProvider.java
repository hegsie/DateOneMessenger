package com.greycells.dateone.client;

import com.google.gwt.place.shared.PlaceController;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.web.bindery.event.shared.EventBus;

public class PlaceControllerProvider implements Provider<PlaceController> {

	@Inject
	public PlaceControllerProvider(EventBus eventBus) {
		this.controller = new PlaceController(eventBus);
	}

	private final PlaceController controller;
	
	@Override
	public PlaceController get() {
		return controller;
	}

}
