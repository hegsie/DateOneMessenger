package com.greycells.dateone.client.activity.page;

import java.util.Set;

import org.waveprotocol.wave.model.id.WaveId;
import org.waveprotocol.wave.model.wave.ParticipantId;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.greycells.dateone.client.IClientFactory;
import com.greycells.dateone.client.activity.MainActivity;
import com.greycells.dateone.client.view.IMessageView;
import com.greycells.dateone.client.view.IMessagesSearchView;
import com.greycells.dateone.client.view.IRunnerBarView;

public class MessageActivity extends MainActivity 
	implements IMessagesSearchView.Presenter, IMessageView.Presenter {

	// Used to obtain views, eventBus, placeController
	// Alternatively, could be injected via GIN
	private IClientFactory clientFactory;
	private IMessagesSearchView msgSearchView;
	private IRunnerBarView runnerBarView;
	private IMessageView messageView;
	@Inject
	public MessageActivity(IClientFactory clientFactory) {
		super(clientFactory);
		
		this.clientFactory = clientFactory;
	}

	@Override
	public void start(AcceptsOneWidget containerWidget, EventBus eventBus) {
		msgSearchView = clientFactory.getMsgSearchView();
		runnerBarView = clientFactory.getRunnerBarView();
		messageView = clientFactory.getMessagesView();
		
		runnerBarView.setPresenter(this, this);
		msgSearchView.setPresenter(this, this);
		messageView.setPresenter(this, this);
		
		mainView.setPageElements(
				runnerBarView.asWidget(),
				messageView.asWidget(),
				msgSearchView.asWidget());

		mainView.setPresenter(this);
		containerWidget.setWidget(mainView.asWidget());	
		
		attemptSilentLogin();
	}

	@Override
	public void onDisplayWave() {
		runnerBarView.showAllOptions();		
	}

	@Override
	public void stop() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void openWave(WaveId id, boolean isNewWave,
			Set<ParticipantId> participants) {
		messageView.openWave(id, isNewWave, participants);	
		
	}
}
