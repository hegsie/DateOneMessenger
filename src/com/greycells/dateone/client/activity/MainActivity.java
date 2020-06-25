package com.greycells.dateone.client.activity;

import java.util.Date;

import com.greycells.dateone.shared.UserSearchingRequest;
import com.greycells.dateone.shared.jso.UserSearchingRequestJsoImpl;
import org.waveprotocol.box.webclient.client.Session;
import org.waveprotocol.box.webclient.search.WaveStore;
import org.waveprotocol.wave.client.account.ProfileManager;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.user.client.Cookies;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.greycells.dateone.client.DateOneService;
import com.greycells.dateone.client.IClientFactory;
import com.greycells.dateone.client.DateOneServiceAsync;
import com.greycells.dateone.client.mvp.AppPlaceFactory;
import com.greycells.dateone.client.search.MessageHandler;
import com.greycells.dateone.client.search.SearchConnectionImpl;
import com.greycells.dateone.client.view.IMainView;
import com.greycells.dateone.client.view.IMessageView;
import com.greycells.dateone.client.view.IRunnerBarView;
import com.greycells.dateone.shared.MessageStatus;
import com.greycells.dateone.shared.User;

public abstract class MainActivity extends ActivityBase implements
		IMainView.Presenter, IRunnerBarView.Presenter {

	protected final IMainView mainView;

	private SearchConnectionImpl searchConnection;
	private IClientFactory clientFactory;
	protected DateOneServiceAsync service = (DateOneServiceAsync) GWT
			.create(DateOneService.class);
	protected AppPlaceFactory placeFactory;

	public MainActivity(IClientFactory clientFactory) {
		this.clientFactory = clientFactory;

		placeFactory = clientFactory.getAppPlaceFactory();
		mainView = clientFactory.getMainView();
		
		String url = com.google.gwt.core.client.GWT.getModuleBaseURL() + "searcher";
		
		searchConnection = new SearchConnectionImpl(new MessageHandler(clientFactory), url );
	}

	protected Boolean attemptSilentLogin() {
		String sessionID = Cookies.getCookie("sid");
		if (sessionID == null || clientFactory.getUserContainer().getUser() == null) {

			service.loginFromSessionServer(new AsyncCallback<User>() {

				@Override
				public void onFailure(Throwable arg0) {
					// Do nothing default to login screen.
				}

				@Override
				public void onSuccess(User user) {
					if (user != null) {

						// set session cookie for 1 day expiry.
						String sessionID = user.getSession();
						final long DURATION = 1000 * 60 * 60 * 24 * 1;
						Date expires = new Date(System.currentTimeMillis()
								+ DURATION);
						Cookies.setCookie("sid", sessionID, expires, null, "/",
								false);

						clientFactory.getUserContainer().setUser(user);
						Session.get().setAddress(user.getWaveId());

						clientFactory.getWaveConnector().start();
						
						clientFactory.getPlaceController().goTo(
								placeFactory.getProfilePlace());
					}
				}

			});
		}
		if (clientFactory.getUserContainer().getUser() != null)
			return true;

		return false;
	}

	@Override
	public void onLogoutUser() {
		service.logout(new AsyncCallback<MessageStatus>() {

			@Override
			public void onSuccess(MessageStatus arg0) {
				clientFactory.getUserContainer().setUser(null);
			}

			@Override
			public void onFailure(Throwable arg0) {

			}
		});
	}

	@Override
	public void onSignOut() {
		service.logout(new AsyncCallback<MessageStatus>() {

			@Override
			public void onFailure(Throwable arg0) {
			}

			@Override
			public void onSuccess(MessageStatus arg0) {
				clientFactory.getPlaceController().goTo(
						placeFactory.getStartPlace());
				clientFactory.getUserContainer().setUser(null);
			}
		});
	}

	@Override
	public void onBeginSearching() {
		searchConnection.connect();

		UserSearchingRequestJsoImpl req = UserSearchingRequestJsoImpl.create();
		req.setMessageType(UserSearchingRequest.RequestType.OpenConnection);

		searchConnection.sendMessage(req.toJson());

		clientFactory.getPlaceController().goTo(placeFactory.getSearchPlace());
	}

	@Override
	public void onShowProfile() {


		UserSearchingRequestJsoImpl req = UserSearchingRequestJsoImpl.create();
		req.setMessageType(UserSearchingRequest.RequestType.CloseConnection);

		searchConnection.sendMessage(req.toJson());

		clientFactory.getPlaceController().goTo(placeFactory.getProfilePlace());
	}
	
	@Override 
	public IMainView getMainView(){
		return mainView;
	}
	
	@Override 
	public ProfileManager getProfileManager(){
		return clientFactory.getProfileManager();
	}
	
	@Override
	public WaveStore getWaveStore(){
		return clientFactory.getWaveStore();
	}
	
	@Override 
	public IMessageView getMessageView(){
		return clientFactory.getMessagesView();
	}
	
	@Override 
	public IRunnerBarView getRunnerBarView(){
		return clientFactory.getRunnerBarView();
	}
}
