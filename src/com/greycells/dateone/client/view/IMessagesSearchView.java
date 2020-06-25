package com.greycells.dateone.client.view;

import org.waveprotocol.box.webclient.search.SearchPanelWidget;

import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;

public interface IMessagesSearchView extends IsWidget {
	public interface Presenter {
		void onDisplayWave();
	}
	
	SearchPanelWidget getSearchPanel();

	void setPresenter(IMainView.Presenter mainPres, Presenter presenter);

	Widget asWidget();
}
