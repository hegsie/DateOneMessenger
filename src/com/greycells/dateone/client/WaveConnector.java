package com.greycells.dateone.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.GWT.UncaughtExceptionHandler;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.OptionElement;
import com.google.gwt.dom.client.SelectElement;
import com.google.gwt.i18n.client.LocaleInfo;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.inject.Inject;
import com.greycells.dateone.client.place.MessagePlace;

import org.waveprotocol.box.stat.Timing;
import org.waveprotocol.box.webclient.client.ClientIdGenerator;
import org.waveprotocol.box.webclient.client.HistoryProviderDefault;
import org.waveprotocol.box.webclient.client.HistorySupport;
import org.waveprotocol.box.webclient.client.RemoteViewServiceMultiplexer;
import org.waveprotocol.box.webclient.client.Session;
import org.waveprotocol.box.webclient.client.WaveWebSocketClient;
import org.waveprotocol.box.webclient.client.i18n.WebClientMessages;
import org.waveprotocol.box.webclient.profile.RemoteProfileManagerImpl;
import org.waveprotocol.box.webclient.search.RemoteSearchService;
import org.waveprotocol.box.webclient.search.Search;
import org.waveprotocol.box.webclient.search.SearchPresenter;
import org.waveprotocol.box.webclient.search.SimpleSearch;
import org.waveprotocol.box.webclient.stat.SingleThreadedRequestScope;
import org.waveprotocol.box.webclient.stat.gwtevent.GwtStatisticsEventSystem;
import org.waveprotocol.box.webclient.stat.gwtevent.GwtStatisticsHandler;
import org.waveprotocol.box.webclient.widget.error.ErrorIndicatorPresenter;
import org.waveprotocol.box.webclient.widget.loading.LoadingIndicator;
import org.waveprotocol.wave.client.account.ProfileManager;
import org.waveprotocol.wave.client.doodad.attachment.AttachmentManagerImpl;
import org.waveprotocol.wave.client.doodad.attachment.AttachmentManagerProvider;
import org.waveprotocol.wave.client.events.ClientEvents;
import org.waveprotocol.wave.client.events.Log;
import org.waveprotocol.wave.client.events.NetworkStatusEvent;
import org.waveprotocol.wave.client.events.NetworkStatusEventHandler;
import org.waveprotocol.wave.client.events.WaveCreationEvent;
import org.waveprotocol.wave.client.events.WaveCreationEventHandler;
import org.waveprotocol.wave.client.events.WaveSelectionEvent;
import org.waveprotocol.wave.client.events.WaveSelectionEventHandler;
import org.waveprotocol.wave.client.wavepanel.event.FocusManager;
import org.waveprotocol.wave.model.id.IdGenerator;
import org.waveprotocol.wave.model.id.WaveId;
import org.waveprotocol.wave.model.wave.ParticipantId;
import org.waveprotocol.wave.model.waveref.WaveRef;
import org.waveprotocol.wave.util.escapers.GwtWaverefEncoder;

import java.util.Set;
import java.util.logging.Logger;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class WaveConnector implements IWaveConnector {

	private static final WebClientMessages messages = GWT
			.create(WebClientMessages.class);

	static Log LOG = Log.get(WaveConnector.class);
	// Use of GWT logging is only intended for sending exception reports to the
	// server, nothing else in the client should use java.util.logging.
	// Please also see WebClientDemo.gwt.xml.
	private static final Logger REMOTE_LOG = Logger.getLogger("REMOTE_LOG");

	private static final String DEFAULT_LOCALE = "default";

	private final ProfileManager profiles = new RemoteProfileManagerImpl();

	private final Element loading = new LoadingIndicator().getElement();

	/**
	 * Create a remote websocket to talk to the server-side FedOne service.
	 */
	private WaveWebSocketClient websocket;

	private ParticipantId loggedInUser;

	private IdGenerator idGenerator;

	private RemoteViewServiceMultiplexer channel;

	private SelectElement select;

	private IClientFactory clientFactory;

	@Inject
	public WaveConnector(IClientFactory clientFactory) {
		this.clientFactory = clientFactory;
	}

	private void setupCreateHandler() {

		ClientEvents.get().addWaveCreationEventHandler(
				new WaveCreationEventHandler() {

					@Override
					public void onCreateRequest(WaveCreationEvent event,
							Set<ParticipantId> participantSet) {
						LOG.info("WaveCreationEvent received");
						if (getChannel() == null) {
							throw new RuntimeException(
									"Spaghetti attack.  Create occured before login");
						}
						WaveId id = getIdGenerator().newWaveId();

						openWaveAndGotoActivity(clientFactory, participantSet,
								true, id);
					}
				});
	}

	private void setupMessagePane() {

		// Handles opening waves.
		ClientEvents.get().addWaveSelectionEventHandler(
				new WaveSelectionEventHandler() {
					@Override
					public void onSelection(WaveRef waveRef) {
						openWaveAndGotoActivity(clientFactory, null, false,
								waveRef.getWaveId());
					}
				});
	}

	private void openWaveAndGotoActivity(final IClientFactory clientFactory,
			Set<ParticipantId> participantSet, boolean isNewWave, WaveId id) {
		MessagePlace place = clientFactory
				.getAppPlaceFactory()
				.getMessagePlaceTokenizer()
				.getPlace(
						GwtWaverefEncoder.encodeToUriPathSegment(WaveRef.of(id)));

		clientFactory.getPlaceController().goTo(place);

		place.openWave(id, isNewWave, participantSet);
	}

	public void setupSearchPane() {

		SearchPresenter.WaveActionHandler actionHandler = new SearchPresenter.WaveActionHandler() {
			@Override
			public void onCreateWave() {
				ClientEvents.get().fireEvent(new WaveCreationEvent());
			}

			@Override
			public void onWaveSelected(WaveId id) {
				ClientEvents.get().fireEvent(
						new WaveSelectionEvent(WaveRef.of(id)));
			}
		};
		Search search = SimpleSearch.create(RemoteSearchService.create(),
				clientFactory.getWaveStore());
		SearchPresenter.create(search, clientFactory.getMsgSearchView()
				.getSearchPanel(), actionHandler, clientFactory
				.getProfileManager());
	}

	/**
	 * This is the entry point method.
	 */
	@Override
	public void start() {

		ErrorHandler.install();

		setupCreateHandler();

		setupLocaleSelect();
		setupConnectionIndicator();

		HistorySupport.init(new HistoryProviderDefault());
		DateOneHistoryChangeListener.init();

		websocket = new WaveWebSocketClient(websocketNotAvailable(),
				getWebSocketBaseUrl());
		websocket.connect();

		if (Session.get().isLoggedIn()) {
			loggedInUser = new ParticipantId(Session.get().getAddress());
			idGenerator = ClientIdGenerator.create();
			loginToServer();
		}

		setupSearchPane();
		setupMessagePane();

		setupStatistics();

		AttachmentManagerProvider.init(AttachmentManagerImpl.getInstance());
		FocusManager.init();

		// History.fireCurrentHistoryState();
		LOG.info("SimpleWebClient.onModuleLoad() done");
	}

	private void setupLocaleSelect() {

		String currentLocale = LocaleInfo.getCurrentLocale().getLocaleName();
		String[] localeNames = LocaleInfo.getAvailableLocaleNames();
		for (String locale : localeNames) {
			if (!DEFAULT_LOCALE.equals(locale)) {
				String displayName = LocaleInfo
						.getLocaleNativeDisplayName(locale);
				OptionElement option = Document.get().createOptionElement();
				option.setValue(locale);
				option.setText(displayName);
				select.add(option, null);
				if (locale.equals(currentLocale)) {
					select.setSelectedIndex(select.getLength() - 1);
				}
			}
		}
	}

	@Override
	public void setLocaleElement(Element element) {
		select = (SelectElement) element;
	}

	private void setupConnectionIndicator() {
		ClientEvents.get().addNetworkStatusEventHandler(
				new NetworkStatusEventHandler() {

					boolean isTurbulenceDetected = false;

					@Override
					public void onNetworkStatus(NetworkStatusEvent event) {
						Element element = Document.get().getElementById(
								"netstatus");
						if (element != null) {
							switch (event.getStatus()) {
							case CONNECTED:
							case RECONNECTED:
								element.setInnerText(messages.online());
								element.setClassName("online");
								isTurbulenceDetected = false;
								break;
							case DISCONNECTED:
								element.setInnerText(messages.offline());
								element.setClassName("offline");
								if (!isTurbulenceDetected) {
									isTurbulenceDetected = true;
								}
								break;
							case RECONNECTING:
								element.setInnerText(messages.connecting());
								element.setClassName("connecting");
								break;
							}
						}
					}
				});
	}

	private void setupStatistics() {
		Timing.setScope(new SingleThreadedRequestScope());
		Timing.setEnabled(true);
		GwtStatisticsEventSystem eventSystem = new GwtStatisticsEventSystem();
		eventSystem.addListener(new GwtStatisticsHandler(), true);
		eventSystem.enable(true);
	}

	/**
	 * Returns <code>ws(s)://yourhost[:port]/</code>.
	 */
	// XXX check formatting wrt GPE
	private native String getWebSocketBaseUrl() /*-{return ((window.location.protocol == "https:") ? "wss" : "ws") + "://" +  $wnd.__websocket_address + "/";}-*/;

	private native boolean websocketNotAvailable() /*-{ return !window.WebSocket }-*/;

	/**
   */
	private void loginToServer() {
		assert loggedInUser != null;
		channel = new RemoteViewServiceMultiplexer(websocket,
				loggedInUser.getAddress());
	}

	/**
	 * An exception handler that reports exceptions using a
	 * <em>shiny banner</em> (an alert placed on the top of the screen). Once
	 * the stack trace is prepared, it is revealed in the banner via a link.
	 */
	static class ErrorHandler implements UncaughtExceptionHandler {
		/** Next handler in the handler chain. */
		private final UncaughtExceptionHandler next;

		/**
		 * Indicates whether an error has already been reported (at most one
		 * error is ever reported by this handler).
		 */
		private boolean hasFired;

		private ErrorHandler(UncaughtExceptionHandler next) {
			this.next = next;
		}

		public static void install() {
			GWT.setUncaughtExceptionHandler(new ErrorHandler(GWT
					.getUncaughtExceptionHandler()));
		}

		@Override
		public void onUncaughtException(Throwable e) {
			if (!hasFired) {
				hasFired = true;
				final ErrorIndicatorPresenter error = ErrorIndicatorPresenter
						.create(RootPanel.get("banner"));
			}

			if (next != null) {
				next.onUncaughtException(e);
			}
		}
	}

	@Override
	public IdGenerator getIdGenerator() {
		return idGenerator;
	}

	@Override
	public RemoteViewServiceMultiplexer getChannel() {
		return channel;
	}
}
