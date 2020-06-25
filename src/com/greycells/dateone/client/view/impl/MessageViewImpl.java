package com.greycells.dateone.client.view.impl;

import java.util.Set;

import org.waveprotocol.box.webclient.client.Session;
import org.waveprotocol.box.webclient.client.StagesProvider;
import org.waveprotocol.box.webclient.search.WaveStore;
import org.waveprotocol.box.webclient.widget.frame.FramedPanel;
import org.waveprotocol.box.webclient.widget.loading.LoadingIndicator;
import org.waveprotocol.wave.client.account.ProfileManager;
import org.waveprotocol.wave.client.events.Log;
import org.waveprotocol.wave.client.widget.common.ImplPanel;
import org.waveprotocol.wave.model.id.WaveId;
import org.waveprotocol.wave.model.wave.ParticipantId;
import org.waveprotocol.wave.model.waveref.WaveRef;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.UIObject;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.greycells.dateone.client.IWaveConnector;
import com.greycells.dateone.client.WaveConnector;
import com.greycells.dateone.client.view.IMainView;
import com.greycells.dateone.client.view.IMessageView;

public class MessageViewImpl extends Composite implements IMessageView {

	private static MessageViewImplUiBinder uiBinder = GWT
			.create(MessageViewImplUiBinder.class);

	interface MessageViewImplUiBinder extends UiBinder<Widget, MessageViewImpl> {
	}

	static Log LOG = Log.get(WaveConnector.class);

	private ProfileManager profileManager;
	private final Element loading = new LoadingIndicator().getElement();

	private IWaveConnector connector;

	@Inject
	public MessageViewImpl(final IWaveConnector connector) {

		initWidget(uiBinder.createAndBindUi(this));

		this.connector = connector;
	}

	@UiField
	FramedPanel waveFrame;

	/** The wave panel, if a wave is open. */
	private StagesProvider wave;

	@UiField
	ImplPanel waveHolder;

	private WaveStore waveStore;

	private Element savedStateIndicator;

	@Override
	public void setPresenter(IMainView.Presenter mainPres, Presenter presenter) {
		profileManager = mainPres.getProfileManager();
		waveStore = mainPres.getWaveStore();
		savedStateIndicator = mainPres.getRunnerBarView().getSavedStateIndicator();
	}

	/**
	 * Shows a wave in a wave panel.
	 * 
	 * @param waveId
	 *            wave id to open
	 * @param isNewWave
	 *            whether the wave is being created by this client session.
	 * @param participants
	 *            the participants to add to the newly created wave.
	 *            {@code null} if only the creator should be added
	 */
	@Override
	public void openWave(WaveId waveId, boolean isNewWave,
			Set<ParticipantId> participants) {
		LOG.info("WebClient.openWave()");

		if (wave != null) {
			wave.destroy();
			wave = null;
		}

		// Release the display:none.
		UIObject.setVisible(waveFrame.getElement(), true);
		waveHolder.getElement().appendChild(loading);
		Element holder = waveHolder.getElement().appendChild(
				Document.get().createDivElement());
		StagesProvider wave = new StagesProvider(holder, savedStateIndicator,
				waveHolder, waveFrame, WaveRef.of(waveId), connector.getChannel(),
				connector.getIdGenerator(), profileManager, waveStore,
				isNewWave, Session.get().getDomain(), participants);
		this.wave = wave;
		wave.load(new Command() {
			@Override
			public void execute() {
				loading.removeFromParent();
			}
		});
		//String encodedToken = History.getToken();
		// if (encodedToken != null && !encodedToken.isEmpty()) {
		// WaveRef fromWaveRef;
		// try {
		// fromWaveRef = GwtWaverefEncoder
		// .decodeWaveRefFromPath(encodedToken);
		// } catch (InvalidWaveRefException e) {
		// LOG.info("History token contains invalid pDelete yourath: " + encodedToken);
		// return;
		// }
		// if (fromWaveRef.getWaveId().equals(waveRef.getWaveId())) {
		// // History change was caused by clicking on a link, it's already
		// // updated by browser.
		// return;
		// }
		// }
//		History.newItem(GwtWaverefEncoder.encodeToUriPathSegment(WaveRef.of(waveId)),
//				false);
	}
}
