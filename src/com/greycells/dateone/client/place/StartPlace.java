package com.greycells.dateone.client.place;

import com.google.gwt.place.shared.PlaceTokenizer;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.greycells.dateone.client.activity.page.StartActivity;
import com.greycells.dateone.client.mvp.ActivityPlace;

public class StartPlace extends ActivityPlace<StartActivity>
{
    @Inject
    public StartPlace(StartActivity activity) {
    	super(activity);
    }

	public static class Tokenizer implements PlaceTokenizer<StartPlace>
	{
		// Since the place is injectable, we'll let Gin do the construction.
		private final Provider<StartPlace> placeProvider; 
		
		@Inject
        public Tokenizer(Provider<StartPlace> placeProvider) {
            this.placeProvider = placeProvider;
        }
		
		@Override
		public String getToken(StartPlace place) {
			return place.getTokenName();
		}

		@Override
		public StartPlace getPlace(String token) {
			StartPlace place = placeProvider.get();
			place.setTokenName(token);
			return place;
		}
	}
	
}
