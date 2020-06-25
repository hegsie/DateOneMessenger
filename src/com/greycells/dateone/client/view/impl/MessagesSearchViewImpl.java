package com.greycells.dateone.client.view.impl;

import org.waveprotocol.box.webclient.search.SearchPanelRenderer;
import org.waveprotocol.box.webclient.search.SearchPanelWidget;
import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;
import com.greycells.dateone.client.IClientFactory;
import com.greycells.dateone.client.view.IMainView;
import com.greycells.dateone.client.view.IMessagesSearchView;
import com.google.gwt.uibinder.client.UiField;
import com.google.inject.Inject;

public class MessagesSearchViewImpl extends Composite implements
		IMessagesSearchView {

	private static SearchViewImplUiBinder uiBinder = GWT
			.create(SearchViewImplUiBinder.class);

	interface SearchViewImplUiBinder extends
			UiBinder<Widget, MessagesSearchViewImpl> {
	}

	@Inject
	public MessagesSearchViewImpl(IClientFactory clientFactory) {
		searchPanel = new SearchPanelWidget(
				new SearchPanelRenderer(clientFactory.getProfileManager()));
		
		initWidget(uiBinder.createAndBindUi(this));
	}

	@Override
	public void setPresenter(IMainView.Presenter mainPres, Presenter presenter) {
	}

	@UiField(provided = true) 
	SearchPanelWidget searchPanel;

	@Override
	public SearchPanelWidget getSearchPanel() {
		return searchPanel;
	}
}
