package com.greycells.dateone.client.view.impl;

import com.google.code.gwt.crop.client.GWTCropper;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

class CropPopup extends PopupPanel {

    public CropPopup(String filename) {
      // PopupPanel's constructor takes 'auto-hide' as its boolean parameter.
      // If this is set, the panel closes itself automatically when the user
      // clicks outside of it.
      super(true);	
      
      // PopupPanel is a SimplePanel, so you have to set it's widget property to
      // whatever you want its contents to be.
      VerticalPanel panel = new VerticalPanel();
      final GWTCropper cropper = new GWTCropper(filename);
      cropper.setAspectRatio(1);
      
      Button ok = new Button("OK");
      ok.addClickHandler(new ClickHandler(){

		@Override
		public void onClick(ClickEvent event) {
			// test 
			
		}
    	  
      });
      Button cancel = new Button("Cancel");
      
      panel.add(cropper);
      panel.add(ok);
      panel.add(cancel);
      
      setWidget(panel);
    }
}