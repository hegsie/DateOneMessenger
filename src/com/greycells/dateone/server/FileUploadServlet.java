package com.greycells.dateone.server;

import com.greycells.dateone.shared.IUser;
import com.greycells.dateone.shared.User;
import gwtupload.server.UploadAction;
import gwtupload.server.exceptions.UploadActionException;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;

import com.google.inject.Inject;
import com.greycells.dateone.server.helpers.DBHelper;
import com.greycells.dateone.server.helpers.UserHelper;
import com.mongodb.gridfs.GridFS;
import com.mongodb.gridfs.GridFSDBFile;

/**
 * This class sends by email, all the fields and files received by GWTUpload
 * servlet.
 * 
 */
public class FileUploadServlet extends UploadAction {

	private UserHelper userHelper;
	private DBHelper dbHelper;

	@Inject
	public FileUploadServlet(UserHelper userHelper, DBHelper dbHelper){
		this.userHelper = userHelper;
		this.dbHelper = dbHelper;		
	}
	
	private static final long serialVersionUID = 1L;

	/**
	 * Override executeAction to save the received files in a custom place and
	 * delete this items from session.
	 */
	@Override
	public String executeAction(HttpServletRequest request,
			List<FileItem> sessionFiles) throws UploadActionException {
		String response = "";
		for (FileItem item : sessionFiles) {
			if (false == item.isFormField()) {
				try {
					User user = userHelper.getUserAlreadyFromSession(request);
					if (user != null) {

						String filename = dbHelper.saveProfilePicture(item, user);
						
						user.setPhotoFileId(filename);
						
						dbHelper.persist(user);
						
						// / Send a customized message to the client.
						response += "File saved as " + filename;
					}
				} catch (Exception e) {
					throw new UploadActionException(e.getMessage());
				}
			}
		}

		// / Remove files from session because we have a copy of them
		removeSessionFileItems(request);

		// / Send your customized message to the client.
		return response;
	}


	/**
	 * Get the content of an uploaded file.
	 */
	@Override
	public void getUploadedFile(HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		GridFS gridfs = dbHelper.getGridFS();
		
		IUser user = userHelper.getUserAlreadyFromSession(request);
		GridFSDBFile file = gridfs.findOne(user.getPhotoFileId());

		if (file != null) {
			response.setContentType(file.getContentType());
			copyFromInputStreamToOutputStream(file.getInputStream(), response.getOutputStream());
		} else {
			renderXmlResponse(request, response, XML_ERROR_ITEM_NOT_FOUND);
		}
	}

	/**
	 * Remove a file when the user sends a delete request.
	 */
	@Override
	public void removeItem(HttpServletRequest request, String fieldName)
			throws UploadActionException {
		GridFS gridfs = dbHelper.getGridFS();
		
		IUser user = userHelper.getUserAlreadyFromSession(request);
		GridFSDBFile file = gridfs.findOne(user.getPhotoFileId());
		gridfs.remove(file);
	}
}
