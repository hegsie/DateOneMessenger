package com.greycells.dateone.client.view.impl;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.Random;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;
import com.greycells.dateone.client.view.IMainView;
import com.greycells.dateone.client.view.ISearchView;
import com.google.gwt.uibinder.client.UiField;
import com.greycells.dateone.shared.UserFromSearch;

public class SearchViewImpl extends Composite implements ISearchView {
		
	private static SearchViewImplUiBinder uiBinder = GWT
			.create(SearchViewImplUiBinder.class);
	@UiField AbsolutePanel Panel;

	interface SearchViewImplUiBinder extends UiBinder<Widget, SearchViewImpl> {
	}

	public SearchViewImpl() {
		initWidget(uiBinder.createAndBindUi(this));
	}

	@Override
	public void setPresenter(IMainView.Presenter mainPres, Presenter presenter) {
		Panel.setSize("100%", "100%");
		Panel.addStyleName("absolutePanel");
	}

	@Override
	public void setNewUser(UserFromSearch user) {
		SearchElementImpl elem = new SearchElementImpl();
		elem.setUser(user);
		int width = elem.getOffsetWidth();
		int absPanel = getElement().getOffsetWidth();
		int xStartPos = Random.nextInt(absPanel-width);
				
		Panel.add(elem, xStartPos, 0);
		
        SearchViewAnimation animation = new SearchViewAnimation(elem.getElement());
        animation.scrollTo(xStartPos, 500, 5000);	
	}
}
