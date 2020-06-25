package com.greycells.dateone.client.view.impl;

import gwtupload.client.IUploader;
import gwtupload.client.MultiUploader;
import gwtupload.client.PreloadedImage;
import gwtupload.client.IUploadStatus.Status;
import gwtupload.client.IUploader.UploadedInfo;
import gwtupload.client.PreloadedImage.OnLoadPreloadedImageHandler;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.greycells.dateone.client.IClientFactory;
import com.greycells.dateone.client.view.IMainView;
import com.greycells.dateone.client.view.IProfileView;
import com.greycells.dateone.shared.IUser;

public class ProfileViewImpl extends Composite implements IProfileView {

	@UiField 
	Label firstName;
	
	@UiField 
	Label lastName;
	
	@UiField
	MultiUploader uploader;
	
	@UiField
	Image profilePic;
	
	//UploaderConstants c = GWT.create(UploaderConstants.class);

	private IClientFactory clientFactory;
	
	private static ProfileViewImplUiBinder uiBinder = GWT
			.create(ProfileViewImplUiBinder.class);
	
	interface ProfileViewImplUiBinder extends UiBinder<Widget, ProfileViewImpl> {
	}

	@Inject
	public ProfileViewImpl(IClientFactory clientFactory) {
		this.clientFactory = clientFactory;
		initWidget(uiBinder.createAndBindUi(this));
		
		uploader.addOnFinishUploadHandler(onFinishUploaderHandler);
	}

	@Override
	public void setPresenter(IMainView.Presenter mainPres, Presenter presenter) {
		IUser user = clientFactory.getUserContainer().getUser();
		
		if (user == null)
		{
			mainPres.getRunnerBarView().logout();
		}
		
		firstName.setText(user.getFirstName());
		lastName.setText(user.getLastName());
		String location = GWT.getHostPageBaseURL() + "images?imageId=" + user.getPhotoFileId();

		profilePic.setUrl(location);
		profilePic.setWidth("200px");
		profilePic.setVisible(true);
	}
	
	// Load the image in the document and in the case of success attach it to
	// the viewer
	private IUploader.OnFinishUploaderHandler onFinishUploaderHandler = new IUploader.OnFinishUploaderHandler() {
		public void onFinish(IUploader uploader) {
			if (uploader.getStatus() == Status.SUCCESS) {

				new PreloadedImage(uploader.fileUrl(), showImage);

				// The server sends useful information to the client by default
				UploadedInfo info = uploader.getServerInfo();
				System.out.println("File name " + info.name);
				System.out.println("File content-type " + info.ctype);
				System.out.println("File size " + info.size);

				// Also, you can send any customized message and parse it
				System.out.println("Server message " + info.message);
			}
		}
	};

	// Attach an image to the pictures viewer
	private OnLoadPreloadedImageHandler showImage = new OnLoadPreloadedImageHandler() {
		public void onLoad(PreloadedImage image) {
			image.setWidth("200px");
			profilePic = image;
		}
	};
}
