package com.greycells.dateone.client.activity.page;

import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.greycells.dateone.client.IClientFactory;
import com.greycells.dateone.client.activity.MainActivity;
import com.greycells.dateone.client.view.IMessagesSearchView;
import com.greycells.dateone.client.view.IProfileView;
import com.greycells.dateone.client.view.IRunnerBarView;

public class ProfileActivity extends MainActivity 
	implements IProfileView.Presenter, IMessagesSearchView.Presenter {

	// Used to obtain views, eventBus, placeController
	// Alternatively, could be injected via GIN
	private IClientFactory clientFactory;
	private IMessagesSearchView msgSearchView;
	private IRunnerBarView runnerBarView;
	private IProfileView profileView;
	@Inject
	public ProfileActivity(IClientFactory clientFactory) {
		super(clientFactory);
		
		this.clientFactory = clientFactory;
	}

	@Override
	public void start(AcceptsOneWidget containerWidget, EventBus eventBus) {
		msgSearchView = clientFactory.getMsgSearchView();
		profileView = clientFactory.getProfileView();
		runnerBarView = clientFactory.getRunnerBarView();
		
		profileView.setPresenter(this, this);
		runnerBarView.setPresenter(this, this);
		msgSearchView.setPresenter(this, this);
		
		mainView.setPageElements(
				runnerBarView.asWidget(),
				profileView.asWidget(),
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
}
