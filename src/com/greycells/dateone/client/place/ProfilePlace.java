
package com.greycells.dateone.client.place;

import com.google.gwt.place.shared.PlaceTokenizer;
import com.google.gwt.place.shared.Prefix;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.greycells.dateone.client.activity.page.ProfileActivity;
import com.greycells.dateone.client.mvp.ActivityPlace;

public class ProfilePlace extends ActivityPlace<ProfileActivity>
{	
    @Inject
    public ProfilePlace(ProfileActivity activity) {
    	super(activity);
    }
    
	@Prefix("profile")
	public static class Tokenizer implements PlaceTokenizer<ProfilePlace>
	{
		// Since the place is injectable, we'll let Gin do the construction.
		private final Provider<ProfilePlace> placeProvider; 
		
		@Inject
        public Tokenizer(Provider<ProfilePlace> placeProvider) {
            this.placeProvider = placeProvider;
        } 
		
		@Override
		public String getToken(ProfilePlace place) {
			return place.getTokenName();
		}

		@Override
		public ProfilePlace getPlace(String token) {
			ProfilePlace place = placeProvider.get();
			place.setTokenName(token);
			return place;
		}
	}
	
}
