package com.greycells.dateone.client.view;

import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;

public interface ILoginView extends IsWidget {
	
	  public interface Presenter {
		    void onLoginUser(String username, String password);
		  }
		  
		  void setPresenter(IMainView.Presenter mainPres, Presenter presenter);
		  Widget asWidget();
}
