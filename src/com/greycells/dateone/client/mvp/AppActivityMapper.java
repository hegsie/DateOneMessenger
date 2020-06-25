package com.greycells.dateone.client.mvp;

import com.google.gwt.activity.shared.Activity;
import com.google.gwt.activity.shared.ActivityMapper;
import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceController;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;

public class AppActivityMapper implements ActivityMapper {
	
	@Inject
	public AppActivityMapper(EventBus eventBus, PlaceController placeController) {
		super();
	}
	
	@SuppressWarnings("rawtypes")
	@Override
	public Activity getActivity(Place place) {
		if (place instanceof ActivityPlace) {
			Activity activity = ((ActivityPlace) place).getActivity();
            return activity;
        }

        return null;
		
	}

}
