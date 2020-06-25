package com.greycells.dateone.client.place;

import com.google.gwt.place.shared.PlaceTokenizer;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.greycells.dateone.client.activity.page.SearchActivity;
import com.greycells.dateone.client.mvp.ActivityPlace;

public class SearchPlace extends ActivityPlace<SearchActivity>
{
    @Inject
    public SearchPlace(SearchActivity activity) {
    	super(activity);
    }

	public static class Tokenizer implements PlaceTokenizer<SearchPlace>
	{
		// Since the place is injectable, we'll let Gin do the construction.
		private final Provider<SearchPlace> placeProvider; 
		
		@Inject
        public Tokenizer(Provider<SearchPlace> placeProvider) {
            this.placeProvider = placeProvider;
        } 
		
		@Override
		public String getToken(SearchPlace place) {
			return place.getTokenName();
		}

		@Override
		public SearchPlace getPlace(String token) {
			SearchPlace place = placeProvider.get();
			place.setTokenName(token);
			return place;
		}
	}
	
}
