package com.greycells.dateone.client.view;

import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;

public interface IProfileView extends IsWidget {
	public interface Presenter {
	}

	void setPresenter(IMainView.Presenter mainPres, Presenter presenter);
	Widget asWidget();
}
