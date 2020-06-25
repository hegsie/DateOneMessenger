package com.greycells.dateone.client.view.impl;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.*;
import com.greycells.dateone.client.DateUtil;
import com.greycells.dateone.client.view.IMainView;
import com.greycells.dateone.client.view.IRegistrationView;
import com.greycells.dateone.shared.Address;
import com.greycells.dateone.shared.Gender;
import com.greycells.dateone.shared.InterestedIn;
import com.greycells.dateone.shared.User;

import java.util.Date;

public class RegistrationViewImpl extends Composite implements IRegistrationView {

	private static RegistrationViewUiBinder uiBinder = GWT.create(RegistrationViewUiBinder.class);
	
	@UiField Button register;
	@UiField Button postcodeLookup;
	
	@UiField TextBox tbxFirstName;
	@UiField TextBox tbxSurname;
	@UiField TextBox tbxEmail;
	
	@UiField ListBox lbxDay;
	@UiField ListBox lbxMonth;
	@UiField ListBox lbxYear;
	@UiField ListBox lbxGender;
	@UiField ListBox lbxInterestedIn;
	
	@UiField TextBox tbxPostcode;
	@UiField ListBox lbxAddress;
	@UiField PasswordTextBox tbxPassword;
	@UiField PasswordTextBox tbxCfmPassword;
	
	private Presenter presenter;

	interface RegistrationViewUiBinder extends UiBinder<Widget, RegistrationViewImpl> {
	}

	public RegistrationViewImpl() {
		initWidget(uiBinder.createAndBindUi(this));
		
		populateListBoxes();
	}

	private void populateListBoxes() {
		for (Integer day = 1; day <= 31; day++ ){
			lbxDay.addItem(day.toString());
		}
		lbxDay.setVisibleItemCount(1);
		
		lbxMonth.addItem("January");
		lbxMonth.addItem("February");
		lbxMonth.addItem("March");
		lbxMonth.addItem("April");
		lbxMonth.addItem("May");
		lbxMonth.addItem("June");
		lbxMonth.addItem("July");
		lbxMonth.addItem("August");
		lbxMonth.addItem("September");
		lbxMonth.addItem("October");
		lbxMonth.addItem("November");
		lbxMonth.addItem("December");
		lbxMonth.setVisibleItemCount(1);

		int currentYear = DateUtil.getYearAsInt(new Date());
		for (Integer year = 1900; year<=currentYear; year++){
			lbxYear.addItem(year.toString());
		}
		lbxYear.setVisibleItemCount(1);
		
		lbxGender.addItem(Gender.Man.toString());
		lbxGender.addItem(Gender.Woman.toString());
		
		lbxInterestedIn.addItem(InterestedIn.Men.toString());
		lbxInterestedIn.addItem(InterestedIn.Women.toString());
	}

	@UiHandler("register")
	void onRegisterClick(ClickEvent event) {
		if (!tbxPassword.getText().equals(tbxCfmPassword.getText())) {
			Window.alert("Passwords do not match.");
		}
		
		User user = new User(tbxEmail.getText());
		user.setFirstName(tbxFirstName.getText());
		user.setLastName(tbxSurname.getText());       
		
		DateTimeFormat fmt = DateTimeFormat.getFormat("yyyy.MM.dd");
		
		String day = lbxDay.getItemText(lbxDay.getSelectedIndex());     
		String month = lbxMonth.getItemText(lbxMonth.getSelectedIndex());  
		String year = lbxYear.getItemText(lbxYear.getSelectedIndex());  
		user.setDob(fmt.parse(year + "." + month + "." + day));
		
		user.setGender(Gender.valueOf(lbxGender.getItemText(lbxGender.getSelectedIndex())));    
		user.setInterestedIn(InterestedIn.valueOf(lbxInterestedIn.getItemText(lbxInterestedIn.getSelectedIndex())));     
		user.setAddress(new Address("","","","",""));
		user.getAddress().postcode = tbxPostcode.getText().trim();      
		if (lbxAddress.getSelectedIndex() > -1)
			user.getAddress().houseNumber = lbxAddress.getItemText(lbxAddress.getSelectedIndex());    
		
		presenter.onRegisterUser(user, tbxPassword.getText());
		
		Window.alert("Finished registering user [" + user.getEmail() + "]");
	}
	
	@UiHandler("postcodeLookup")
	void onPostcodeLookupClick(ClickEvent event) {
	}

	@Override
	public void setPresenter(IMainView.Presenter mainPres, Presenter presenter) {
		this.presenter = presenter;
		
	}
}
