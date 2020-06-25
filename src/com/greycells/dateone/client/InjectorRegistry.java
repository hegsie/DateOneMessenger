package com.greycells.dateone.client;

import org.waveprotocol.box.webclient.client.SimpleWaveStore;
import org.waveprotocol.box.webclient.profile.RemoteProfileManagerImpl;
import org.waveprotocol.box.webclient.search.WaveStore;
import org.waveprotocol.wave.client.account.ProfileManager;

import com.google.gwt.activity.shared.ActivityMapper;
import com.google.gwt.inject.client.AbstractGinModule;
import com.google.gwt.place.shared.PlaceController;
import com.google.inject.Singleton;
import com.google.web.bindery.event.shared.EventBus;
import com.google.web.bindery.event.shared.SimpleEventBus;
import com.greycells.dateone.client.mvp.AppActivityMapper;
import com.greycells.dateone.client.mvp.AppPlaceFactory;
import com.greycells.dateone.client.view.ILoginView;
import com.greycells.dateone.client.view.IMainView;
import com.greycells.dateone.client.view.IMessageView;
import com.greycells.dateone.client.view.IProfileView;
import com.greycells.dateone.client.view.IRegistrationView;
import com.greycells.dateone.client.view.IRunnerBarView;
import com.greycells.dateone.client.view.IMessagesSearchView;
import com.greycells.dateone.client.view.ISearchView;
import com.greycells.dateone.client.view.impl.LoginViewImpl;
import com.greycells.dateone.client.view.impl.MainViewImpl;
import com.greycells.dateone.client.view.impl.MessageViewImpl;
import com.greycells.dateone.client.view.impl.ProfileViewImpl;
import com.greycells.dateone.client.view.impl.RegistrationViewImpl;
import com.greycells.dateone.client.view.impl.RunnerBarViewImpl;
import com.greycells.dateone.client.view.impl.MessagesSearchViewImpl;
import com.greycells.dateone.client.view.impl.SearchViewImpl;

public class InjectorRegistry extends AbstractGinModule {
	protected void configure() {
		bind(EventBus.class).to(SimpleEventBus.class).in(Singleton.class);
		bind(PlaceController.class).toProvider(PlaceControllerProvider.class).in(Singleton.class);
		
		bind(AppPlaceFactory.class).in(Singleton.class);
		
		bind(IMainView.class).to(MainViewImpl.class).in(Singleton.class);
		
		bind(IProfileView.class).to(ProfileViewImpl.class).in(Singleton.class);
		bind(IRegistrationView.class).to(RegistrationViewImpl.class).in(Singleton.class);
		bind(ILoginView.class).to(LoginViewImpl.class).in(Singleton.class);
		bind(IMessagesSearchView.class).to(MessagesSearchViewImpl.class).in(Singleton.class);
		bind(IRunnerBarView.class).to(RunnerBarViewImpl.class).in(Singleton.class);
		bind(IMessageView.class).to(MessageViewImpl.class).in(Singleton.class);
		bind(ISearchView.class).to(SearchViewImpl.class).in(Singleton.class);
		
		bind(ActivityMapper.class).to(AppActivityMapper.class).in(Singleton.class);
		
		bind(IWaveConnector.class).to(WaveConnector.class).in(Singleton.class);
		bind(WaveStore.class).to(SimpleWaveStore.class).in(Singleton.class);
		bind(ProfileManager.class).to(RemoteProfileManagerImpl.class).in(Singleton.class);
		bind(IUserContainer.class).to(UserContainer.class).in(Singleton.class);
		}
}	