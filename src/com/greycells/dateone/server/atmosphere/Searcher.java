package com.greycells.dateone.server.atmosphere;

import java.util.Timer;
import java.util.logging.Logger;

import org.atmosphere.cpr.AtmosphereResource;

import com.google.inject.Inject;
import com.greycells.dateone.server.SearcherTimerTask;

public class Searcher {
	
	static final Logger logger = Logger.getLogger("AtmosphereHandler");

	int seconds = 4;

	private Timer timer;

	@Inject
	private SearcherTimerTask searcherTask;

	public void onOpen(AtmosphereResource resource) {
		logger.info("Searcher::onOpen");		
		
		StartSearching(resource);
	}

	private void StartSearching(AtmosphereResource resource) {
		timer = new Timer();
		searcherTask.start(resource);
		timer.schedule(searcherTask,seconds * 1000,seconds * 1000);
	}

	public void onClose(AtmosphereResource resource) {
		timer.cancel();
		timer.purge();
	}

	protected void onMessage(AtmosphereResource resource, String message) {
		logger.info("Received json message from client: " + message);
		try {
			if (message.contains("Close.cxn")) {
				StopSearching();
			} else if (message.contains("Open.cxn")){
				StartSearching(resource);
			}
		} catch (Exception e) {
			System.out.println(e);
		}
	}

	private void StopSearching() {
		searcherTask.cancel();
		timer.cancel();
		timer.purge();
	}

}
