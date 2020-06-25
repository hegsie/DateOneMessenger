package com.greycells.dateone.client;

import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.History;

import org.waveprotocol.wave.client.events.ClientEvents;
import org.waveprotocol.wave.client.events.Log;
import org.waveprotocol.wave.client.events.WaveSelectionEvent;
import org.waveprotocol.wave.model.waveref.InvalidWaveRefException;
import org.waveprotocol.wave.model.waveref.WaveRef;
import org.waveprotocol.wave.util.escapers.GwtWaverefEncoder;

/**
 * The listener interface for receiving historyChange events from browser history.
 *
 * @see {@link ValueChangeEvent}
 */
public class DateOneHistoryChangeListener {
  private static final Log LOG = Log.get(DateOneHistoryChangeListener.class);

  /**
   * Commonly we start to listen history changes when webclient starts calling this
   * method. If you are using wave client integrated with other different GWT application
   * and with a different History management, you can avoid to use this and just
   * call to the {@link WaveSelectionEvent} events (for example) or other uses.
   */
  public static void init() {
    History.addValueChangeHandler(new ValueChangeHandler<String>() {
      @Override
      public void onValueChange(ValueChangeEvent<String> event) {
        String encodedToken = event.getValue();
        if (encodedToken == null || encodedToken.length() == 0) {
          return;
        }
        if (encodedToken.startsWith("message")){
        	encodedToken = encodedToken.replace("message:", "");
        }
        WaveRef waveRef;
        try {
          waveRef = GwtWaverefEncoder.decodeWaveRefFromPath(encodedToken);
        } catch (InvalidWaveRefException e) {
          LOG.info("History token contains invalid path: " + encodedToken);
          return;
        }
        LOG.info("Changing selected wave based on history event to " + waveRef.toString());
        ClientEvents.get().fireEvent(new WaveSelectionEvent(waveRef));
      }
    });
  }

  public DateOneHistoryChangeListener() {
  }
}
