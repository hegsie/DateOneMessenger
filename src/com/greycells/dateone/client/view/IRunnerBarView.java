package com.greycells.dateone.client.view;

import com.google.gwt.dom.client.Element;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;

public interface IRunnerBarView extends IsWidget {

	public interface Presenter {
		void onSignOut();
		void onBeginSearching();
		void onShowProfile();
	}

	void showAllOptions();
	void setPresenter(IMainView.Presenter mainPres, Presenter presenter);
	Widget asWidget();
	Element getSavedStateIndicator();
	void logout();
}