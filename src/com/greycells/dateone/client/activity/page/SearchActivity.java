package com.greycells.dateone.client.activity.page;

import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.greycells.dateone.client.IClientFactory;
import com.greycells.dateone.client.activity.MainActivity;
import com.greycells.dateone.client.event.UserPingEvent;
import com.greycells.dateone.client.view.IMessagesSearchView;
import com.greycells.dateone.client.view.IRunnerBarView;
import com.greycells.dateone.client.view.ISearchView;
import com.greycells.dateone.shared.IUser;
import com.greycells.dateone.shared.UserFromSearch;

public class SearchActivity extends MainActivity implements
		IMessagesSearchView.Presenter, ISearchView.Presenter {
	// Used to obtain views, eventBus, placeController
	// Alternatively, could be injected via GIN
	private IClientFactory clientFactory;
	private ISearchView searchView;
	private IMessagesSearchView msgSearchView;
	private IRunnerBarView runnerBarView;

	@Inject
	public SearchActivity(IClientFactory clientFactory) {
		super(clientFactory);
		
		this.clientFactory = clientFactory;
	}

	@Override
	public void start(AcceptsOneWidget containerWidget, EventBus eventBus) {
		msgSearchView = clientFactory.getMsgSearchView();
		searchView = clientFactory.getSearchView();
		runnerBarView = clientFactory.getRunnerBarView();
		
		runnerBarView.setPresenter(this, this);	
		msgSearchView.setPresenter(this, this);
		searchView.setPresenter(this, this);

		mainView.setPageElements(	runnerBarView.asWidget(),
									searchView.asWidget(),
									msgSearchView.asWidget());

		mainView.setPresenter(this);
		containerWidget.setWidget(mainView.asWidget());
		
		attemptSilentLogin();
		
		eventBus.addHandler(UserPingEvent.TYPE, new UserPingEvent.Handler() {
			
			@Override
			public void displayUser(UserFromSearch user) {
				searchView.setNewUser(user);
			}
		});		
	}
	
	@Override
	public void stop(){
		
	}

	@Override
	public void onDisplayWave() {
		runnerBarView.showAllOptions();
	}
}
