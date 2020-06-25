package com.greycells.dateone.client.search;

import com.greycells.dateone.client.event.UserPingEvent;
import com.greycells.dateone.shared.UserFromSearch;
import com.greycells.dateone.shared.jso.UserSearchingResponseJsoImpl;
import org.waveprotocol.box.search.jso.SearchResponseJsoImpl;
import org.waveprotocol.box.webclient.client.atmosphere.AtmosphereConnectionListener;

import com.greycells.dateone.client.IClientFactory;
import org.waveprotocol.wave.communication.gwt.JsonMessage;
import org.waveprotocol.wave.communication.json.JsonException;

public class MessageHandler implements AtmosphereConnectionListener {
	
	private IClientFactory clientFactory;
	
	public MessageHandler(IClientFactory factory){
		clientFactory = factory;
	}

	@Override
	public void onConnect() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onDisconnect() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onMessage(String message) {
		UserSearchingResponseJsoImpl searchResponse;
		try {
			searchResponse = JsonMessage.parse(message);

			UserFromSearch userFromSearch = searchResponse.getUser();

			UserPingEvent evnt = new UserPingEvent(userFromSearch);
			clientFactory.getEventBus().fireEvent(evnt);

		} catch (JsonException e) {
			e.printStackTrace();
		}
	}
}
