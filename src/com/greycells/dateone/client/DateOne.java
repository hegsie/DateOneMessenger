package com.greycells.dateone.client;

import org.waveprotocol.wave.client.debug.logger.DomLogger;
import org.waveprotocol.wave.common.logging.AbstractLogger.Level;

import com.google.gwt.activity.shared.ActivityManager;
import com.google.gwt.activity.shared.ActivityMapper;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.place.shared.PlaceHistoryHandler;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.web.bindery.event.shared.EventBus;
import com.greycells.dateone.client.mvp.AppPlaceFactory;
import com.greycells.dateone.client.mvp.AppPlaceHistoryMapper;
import com.greycells.dateone.client.place.StartPlace;

public class DateOne implements EntryPoint {

	private SimplePanel appWidget = new SimplePanel();

	private final IClientFactory clientFactory = GWT
			.create(IClientFactory.class);

	public void onModuleLoad() {

		final DomLogger logger = new DomLogger("test");

		GWT.setUncaughtExceptionHandler(new GWT.UncaughtExceptionHandler() {
			public void onUncaughtException(Throwable e) {
				logger.log(Level.FATAL, e);
			}
		});

		EventBus eventBus = clientFactory.getEventBus();
		PlaceController placeController = clientFactory.getPlaceController();

		ActivityMapper activityMapper = clientFactory.getActivityMapper();
		ActivityManager activityManager = new ActivityManager(activityMapper,
				eventBus);
		activityManager.setDisplay(appWidget);

		AppPlaceFactory factory = clientFactory.getAppPlaceFactory();
		StartPlace defaultPlace = factory.getStartPlace();

		AppPlaceHistoryMapper historyMapper = GWT
				.create(AppPlaceHistoryMapper.class);
		historyMapper.setFactory(factory);

		PlaceHistoryHandler historyHandler = new PlaceHistoryHandler(
				historyMapper);
		historyHandler.register(placeController, eventBus, defaultPlace);

		RootPanel.get().add(appWidget);

		historyHandler.handleCurrentHistory();
	}
}
