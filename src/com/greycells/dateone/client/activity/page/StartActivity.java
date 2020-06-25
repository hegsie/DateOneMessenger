package com.greycells.dateone.client.activity.page;

import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.greycells.dateone.client.IClientFactory;
import com.greycells.dateone.client.activity.MainActivity;
import com.greycells.dateone.client.event.EditProfileEvent;
import com.greycells.dateone.client.view.ILoginView;
import com.greycells.dateone.client.view.IRegistrationView;
import com.greycells.dateone.shared.MessageStatus;
import com.greycells.dateone.shared.User;

public class StartActivity extends MainActivity implements
		IRegistrationView.Presenter, ILoginView.Presenter {

	// Used to obtain views, eventBus, placeController
	// Alternatively, could be injected via GIN
	private IClientFactory clientFactory;

	@Inject
	public StartActivity(IClientFactory clientFactory) {
		super(clientFactory);

		this.clientFactory = clientFactory;
	}

	@Override
	public void start(AcceptsOneWidget containerWidget, EventBus eventBus) {
		clientFactory.getRegisterView().setPresenter(this, this);
		clientFactory.getLoginView().setPresenter(this, this);
		clientFactory.getRunnerBarView().setPresenter(this, this);		

		mainView.setPageElements(
				clientFactory.getRunnerBarView().asWidget(),
				clientFactory.getLoginView().asWidget(),
				clientFactory.getRegisterView().asWidget());

		mainView.setPresenter(this);
		containerWidget.setWidget(mainView.asWidget());
		
		attemptSilentLogin();
	}

	@Override
	public void onRegisterUser(User user, String password) {
		service.registerUser(user, password,
				new AsyncCallback<MessageStatus>() {

					@Override
					public void onFailure(Throwable caught) {
						Window.alert("Error registering user!");

					}

					@Override
					public void onSuccess(MessageStatus result) {
						if (result.Status == MessageStatus.Statuses.Error) {
							Window.alert(result.Message);
						} else {
							clientFactory.getEventBus().fireEvent(
									new EditProfileEvent());
						}
					}
				});
	}

	@Override
	public void onLoginUser(final String username, final String password) {

		service.loginUser(username, password,
				new AsyncCallback<MessageStatus>() {

					@Override
					public void onFailure(Throwable caught) {
						Window.alert("Error Logging in.");
					}

					@Override
					public void onSuccess(MessageStatus result) {
						if (result.Status == MessageStatus.Statuses.Error) {
							Window.alert(result.Message);
						} else {
							clientFactory.getPlaceController().goTo(placeFactory.getStartPlace());
						}
					}
				});

	}

	@Override
	public void stop() {
		// TODO Auto-generated method stub
		
	}
}
