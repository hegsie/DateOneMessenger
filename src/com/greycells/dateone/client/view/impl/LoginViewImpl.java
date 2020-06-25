package com.greycells.dateone.client.view.impl;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.greycells.dateone.client.view.ILoginView;
import com.greycells.dateone.client.view.IMainView;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyUpEvent;

public class LoginViewImpl extends Composite implements ILoginView {

	private Presenter presenter = null;
	
	private static LoginViewUiBinder uiBinder = GWT
			.create(LoginViewUiBinder.class);
	@UiField TextBox textBoxUsername;
	@UiField PasswordTextBox textBoxPassword;
	@UiField Button button;

	interface LoginViewUiBinder extends UiBinder<Widget, LoginViewImpl> {
	}

	public LoginViewImpl() {
		initWidget(uiBinder.createAndBindUi(this));
	}

	@UiHandler("button")
	void onButtonClick(ClickEvent event) {
		login();
	}

	private void login() {
		if (textBoxUsername.getText().length() == 0
				|| textBoxPassword.getText().length() == 0) {
				Window.alert("Username or password is empty."); 
			}
		presenter.onLoginUser(textBoxUsername.getText(), textBoxPassword.getText());
	}

	@Override
	public void setPresenter(IMainView.Presenter mainPres, Presenter presenter) {
		this.presenter = presenter;	
	}

	@UiHandler("textBoxPassword")
	void onTextBoxPasswordKeyUp(KeyUpEvent event) {
		if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
			login();
		}
	}
}
