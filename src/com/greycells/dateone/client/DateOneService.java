package com.greycells.dateone.client;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import com.greycells.dateone.shared.MessageStatus;
import com.greycells.dateone.shared.User;

@RemoteServiceRelativePath("dateOneService")
public interface DateOneService extends RemoteService {

	MessageStatus loginUser(String username, String password);
	MessageStatus registerUser(User user, String password);
	MessageStatus editProfile();
	
    User loginFromSessionServer();
    MessageStatus changePassword(String name, String newPassword);
    MessageStatus logout();
    
    MessageStatus beginSearching(int userToSearchAreaRatio);
    MessageStatus endSearching();
}
