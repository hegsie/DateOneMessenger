package com.greycells.dateone.client.place;

import java.util.Set;

import org.waveprotocol.wave.client.events.Log;
import org.waveprotocol.wave.model.id.WaveId;
import org.waveprotocol.wave.model.wave.ParticipantId;
import com.google.gwt.place.shared.PlaceTokenizer;
import com.google.gwt.place.shared.Prefix;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.greycells.dateone.client.activity.page.MessageActivity;
import com.greycells.dateone.client.mvp.ActivityPlace;

public class MessagePlace extends ActivityPlace<MessageActivity>
{	
    @Inject
    public MessagePlace(MessageActivity activity) {
    	super(activity);
    }
    
    public void openWave(WaveId ref, boolean isNewWave, Set<ParticipantId> participants){
    	activity.openWave(ref, isNewWave, participants);
    }
    
	@Prefix("message")
	public static class Tokenizer implements PlaceTokenizer<MessagePlace>
	{
		static Log LOG = Log.get(MessagePlace.Tokenizer.class);
		
		// Since the place is injectable, we'll let Gin do the construction.
		private final Provider<MessagePlace> messageProvider; 
		
		@Inject
        public Tokenizer(Provider<MessagePlace> messageProvider) {
            this.messageProvider = messageProvider;
        } 
		
		@Override
		public String getToken(MessagePlace place) {
			return place.getTokenName();
		}

		@Override
		public MessagePlace getPlace(String token) {
			MessagePlace place = messageProvider.get();
			place.setTokenName(token);
			
			return place;
		}
	}
	
}
