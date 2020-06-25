package com.greycells.dateone.client.view.impl;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.user.client.ui.FlowPanel;
import com.greycells.dateone.shared.UserFromSearch;

public class SearchElementImpl extends Composite {

	private static SearchElementImplUiBinder uiBinder = GWT
			.create(SearchElementImplUiBinder.class);
	@UiField Button _viewProfile;
	@UiField Label _displayName;
	@UiField FlowPanel DisplayPanel;
	
	String _username;
	
	interface SearchElementImplUiBinder extends
			UiBinder<Widget, SearchElementImpl> {
	}

	public SearchElementImpl() {
		initWidget(uiBinder.createAndBindUi(this));		
	}
	
	public void setUser(UserFromSearch user){
		
		_displayName.setText(user.getFirstName());
		_username = user.getEmail();	
		
		DisplayPanel.clear();
		
		String userPhoto = user.getPhotoFileId();
		
		if (userPhoto != null){
			String location = GWT.getHostPageBaseURL() + "images?imageId=" + userPhoto;
			
			Image img = new Image();
			img.setUrl(location);
			img.setWidth("200px");
			img.setVisible(true);
			
			DisplayPanel.add(img);
		}	
	}

	@UiHandler("_viewProfile")
	void onViewProfileClicked(ClickEvent event) {
	}
}
