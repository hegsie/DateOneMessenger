package com.greycells.dateone.client.mvp;

import com.google.gwt.activity.shared.Activity;
import com.google.gwt.place.shared.Place;

/**
 * Mapping an Activity to a Place
 * 
 */
public abstract class ActivityPlace<T extends Activity> extends Place {

	protected T activity;

	public ActivityPlace(T activity) {
		this.activity = activity;
	}

	public T getActivity() {
		return activity;
	}

	private String tokenName;

	public void setTokenName(String token) {
		this.tokenName = token;
	}

	public String getTokenName() {
		return tokenName;
	}

}
