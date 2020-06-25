package com.greycells.dateone.client.mvp;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.greycells.dateone.client.place.MessagePlace;
import com.greycells.dateone.client.place.ProfilePlace;
import com.greycells.dateone.client.place.SearchPlace;
import com.greycells.dateone.client.place.StartPlace;

/**
 * A place factory which knows about all the tokenizers in the app
 */
public class AppPlaceFactory {

	// A single instance of the tokenizer should work, since they don't have
	// state.
	@Inject
	StartPlace.Tokenizer startPlaceTokenizer;
	@Inject
	SearchPlace.Tokenizer searchPlaceTokenizer;
	@Inject
	ProfilePlace.Tokenizer ProfilePlaceTokenizer;
	@Inject 
	MessagePlace.Tokenizer MessagePlaceTokenizer;
	
	@Inject
	Provider<StartPlace> startProvider;
	@Inject
	Provider<SearchPlace> searchProvider;
	@Inject
	Provider<ProfilePlace> profileProvider;
	@Inject
	Provider<MessagePlace> messageProvider;

	public StartPlace.Tokenizer getStartPlaceTokenizer() {
		return startPlaceTokenizer;
	}

	public StartPlace getStartPlace() {
		return startProvider.get();
	}
	
	public SearchPlace.Tokenizer getSearchPlaceTokenizer() {
		return searchPlaceTokenizer;
	}

	public SearchPlace getSearchPlace() {
		return searchProvider.get();
	}

	public ProfilePlace.Tokenizer getProfilePlaceTokenizer() {
		return ProfilePlaceTokenizer;
	}

	public ProfilePlace getProfilePlace() {
		return profileProvider.get();
	}
	
	public MessagePlace.Tokenizer getMessagePlaceTokenizer() {
		return MessagePlaceTokenizer;
	}

	public MessagePlace getMessagePlace() {
		return messageProvider.get();
	}
	
}
