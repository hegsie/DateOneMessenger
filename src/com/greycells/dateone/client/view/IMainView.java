package com.greycells.dateone.client.view;

import org.waveprotocol.box.webclient.search.WaveStore;
import org.waveprotocol.wave.client.account.ProfileManager;

import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;

public interface IMainView extends IsWidget {

	public interface Presenter {
		void onLogoutUser();
		ProfileManager getProfileManager();
		WaveStore getWaveStore();
		IMainView getMainView();
		IMessageView getMessageView();
		IRunnerBarView getRunnerBarView();
	}

	void setPresenter(Presenter presenter);

	Widget asWidget();

	void setPageElements(Widget runner, Widget main, Widget right);
}
