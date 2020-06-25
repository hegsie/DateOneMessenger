package com.greycells.dateone.client;

import org.waveprotocol.box.webclient.search.WaveStore;
import org.waveprotocol.wave.client.account.ProfileManager;

import com.google.gwt.activity.shared.ActivityMapper;
import com.google.gwt.inject.client.GinModules;
import com.google.gwt.inject.client.Ginjector;
import com.google.gwt.place.shared.PlaceController;
import com.google.web.bindery.event.shared.EventBus;
import com.greycells.dateone.client.mvp.AppPlaceFactory;
import com.greycells.dateone.client.view.ILoginView;
import com.greycells.dateone.client.view.IMainView;
import com.greycells.dateone.client.view.IMessageView;
import com.greycells.dateone.client.view.IProfileView;
import com.greycells.dateone.client.view.IRegistrationView;
import com.greycells.dateone.client.view.IRunnerBarView;
import com.greycells.dateone.client.view.IMessagesSearchView;
import com.greycells.dateone.client.view.ISearchView;

@GinModules(InjectorRegistry.class)
public interface IClientFactory extends Ginjector {
	
	EventBus getEventBus();

	PlaceController getPlaceController();

	IProfileView getProfileView();

	IMainView getMainView();

	IRegistrationView getRegisterView();

	ILoginView getLoginView();

	IMessagesSearchView getMsgSearchView();
	
	IMessageView getMessagesView();
	
	ISearchView getSearchView();

	IRunnerBarView getRunnerBarView();
	
	ActivityMapper getActivityMapper();

	AppPlaceFactory getAppPlaceFactory();
	
	ProfileManager getProfileManager();
	
	WaveStore getWaveStore();
	
	IWaveConnector getWaveConnector();
	
	IUserContainer getUserContainer();
}
