package com.greycells.dateone.client.view;

import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import com.greycells.dateone.shared.User;

public interface IRegistrationView extends IsWidget {

	public interface Presenter {
		void onRegisterUser(User user, String password);
	}

	void setPresenter(IMainView.Presenter mainPres, Presenter presenter);
	Widget asWidget();
}
