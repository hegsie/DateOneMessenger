package com.greycells.dateone.client;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.greycells.dateone.shared.MessageStatus;
import com.greycells.dateone.shared.User;

public interface DateOneServiceAsync {

  public void loginUser(String username, String password, AsyncCallback<MessageStatus> callback);
  public void loginFromSessionServer(AsyncCallback<User> callback);
  
  public void registerUser(User user, String password, AsyncCallback<MessageStatus> callback);
  public void editProfile(AsyncCallback<MessageStatus> callback);

  public void changePassword(String name, String newPassword, AsyncCallback<MessageStatus> callback);
  public void logout(AsyncCallback<MessageStatus> callback);
  
  public void beginSearching(int userToSearchAreaRatio, AsyncCallback<MessageStatus> callback);
  public void endSearching(AsyncCallback<MessageStatus> callback);
}

