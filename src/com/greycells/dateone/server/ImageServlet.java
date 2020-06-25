package com.greycells.dateone.server;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.inject.Inject;
import com.greycells.dateone.server.helpers.DBHelper;
import com.mongodb.gridfs.GridFS;
import com.mongodb.gridfs.GridFSDBFile;

@SuppressWarnings("serial")
public class ImageServlet extends HttpServlet {
	
	private DBHelper dbHelper;

	@Inject
	public ImageServlet(DBHelper dbHelper){
		this.dbHelper = dbHelper;
		
	}

	public void doGet(HttpServletRequest req, HttpServletResponse resp) 
      throws IOException {

		GridFS gridfs = dbHelper.getGridFS();
		
		//User user = getUserAlreadyFromSession(req);
		String imageId =  req.getParameter("imageId");
		GridFSDBFile file = gridfs.findOne(imageId);
		resp.setContentType(file.getContentType());

        // Set content size
        resp.setContentLength((int)file.getLength());

        // Open the file and output streams
        InputStream in = file.getInputStream();
        OutputStream out = resp.getOutputStream();

        // Copy the contents of the file to the output stream
        byte[] buf = new byte[1024];
        int count = 0;
        while ((count = in.read(buf)) >= 0) {
            out.write(buf, 0, count);
        }
        in.close();
        out.close();
    }
	

}