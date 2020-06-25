package com.greycells.dateone.client.view;

import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import com.greycells.dateone.shared.UserFromSearch;

public interface ISearchView extends IsWidget {
	public interface Presenter {
	}
	void setPresenter(IMainView.Presenter mainPres, Presenter presenter);

	Widget asWidget();

	void setNewUser(UserFromSearch user);
}
