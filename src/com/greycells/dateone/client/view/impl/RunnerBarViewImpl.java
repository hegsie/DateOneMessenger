package com.greycells.dateone.client.view.impl;

import org.waveprotocol.box.webclient.client.LocaleService;
import org.waveprotocol.box.webclient.client.RemoteLocaleService;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.Window.Location;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.Widget;
import com.greycells.dateone.client.IClientFactory;
import com.greycells.dateone.client.IWaveConnector;
import com.greycells.dateone.client.view.IMainView;
import com.greycells.dateone.client.view.IRunnerBarView;
import com.greycells.dateone.shared.IUser;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.FocusEvent;
import com.google.gwt.http.client.UrlBuilder;
import com.google.inject.Inject;

public class RunnerBarViewImpl extends Composite implements IRunnerBarView {

	private Presenter presenter;
	
	private LocaleService localeService = new RemoteLocaleService();

	private static RunnerBarViewImplUiBinder uiBinder = GWT
			.create(RunnerBarViewImplUiBinder.class);
	@UiField
	Button searchBtn;
	@UiField
	Button profileBtn;
	@UiField
	Button signOutBtn;
	@UiField
	ListBox locale;
	@UiField
	HTMLPanel unsavedStateContainer;
	@UiField
	HTMLPanel banner;
	
	private IClientFactory clientFactory;

	interface RunnerBarViewImplUiBinder extends
			UiBinder<Widget, RunnerBarViewImpl> {
	}

	@Inject
	public RunnerBarViewImpl(final IWaveConnector connector, IClientFactory clientFactory) {
		this.clientFactory = clientFactory;
		initWidget(uiBinder.createAndBindUi(this));
		connector.setLocaleElement(locale.getElement());
		//banner.getElement().setId("banner");
	}

	private void loggedIn() {
		IUser user = clientFactory.getUserContainer().getUser();
		if (user != null) {
			searchBtn.setVisible(true);
			signOutBtn.setVisible(true);
			profileBtn.setVisible(true);
			locale.setVisible(true);
		} else {
			searchBtn.setVisible(false);
			signOutBtn.setVisible(false);
			profileBtn.setVisible(false);
			locale.setVisible(false);
		}
	}

	@Override
	public void setPresenter(IMainView.Presenter mainPres, Presenter presenter) {
		this.presenter = presenter;

		loggedIn();
	}

	@UiHandler("profileBtn")
	void onProfileBtnClick(ClickEvent event) {
		presenter.onShowProfile();
		profileBtn.setVisible(false);
	}

	@UiHandler("searchBtn")
	void onSearchBtnClick(ClickEvent event) {
		presenter.onBeginSearching();
		searchBtn.setVisible(false);
	}

	@UiHandler("signOutBtn")
	void onSignOutBtnClick(ClickEvent event) {
		presenter.onSignOut();
	}
	
	@UiHandler("locale")
	public void onListBoxFocus(FocusEvent event) {

	}

	@UiHandler("locale")
	public void onChange(ChangeEvent event) {
		UrlBuilder builder = Location.createUrlBuilder()
				.setParameter("locale", locale.getValue(locale.getSelectedIndex()));
		Window.Location.replace(builder.buildString());
		localeService.storeLocale(locale.getValue(locale.getSelectedIndex()));
	}

	@Override
	public void showAllOptions() {
		signOutBtn.setVisible(true);
		profileBtn.setVisible(true);
	}

	@Override
	public Element getSavedStateIndicator() {
		return unsavedStateContainer.getElement();
	}

	@Override
	public void logout() {
		presenter.onSignOut();
	}
}
