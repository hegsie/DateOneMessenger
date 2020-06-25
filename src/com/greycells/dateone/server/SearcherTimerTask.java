package com.greycells.dateone.server;

import com.google.gson.Gson;
import com.google.inject.Inject;
import com.greycells.dateone.server.helpers.DBHelper;
import com.greycells.dateone.server.helpers.UserHelper;
import com.greycells.dateone.shared.User;
import com.greycells.dateone.shared.UserSearchingProto;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import org.atmosphere.cpr.AtmosphereResource;
import org.atmosphere.cpr.Broadcaster;
import org.bson.Document;
import org.waveprotocol.box.expimp.Console;
import org.waveprotocol.box.server.rpc.ProtoSerializer;

import java.util.TimerTask;

public class SearcherTimerTask extends TimerTask {

	private final ProtoSerializer serializer;
	private AtmosphereResource resource;
	private MongoCursor<String> iterator;
	private Broadcaster broadcaster;
	private Gson gson = new Gson();
	private User currentUser;
	private DBHelper dbHelper;
	private UserHelper userHelper;
	private DocumentUserConverter converter;
	
	@Inject
	public SearcherTimerTask(DBHelper dbHelper, UserHelper userHelper, DocumentUserConverter converter, ProtoSerializer serializer) {
		this.dbHelper = dbHelper;
		this.userHelper = userHelper;
		this.converter = converter;
		this.serializer = serializer;
	}
	
	@SuppressWarnings("unchecked")
	public void start(AtmosphereResource r){
		resource = r;
		
		MongoDatabase database = dbHelper.getMongoDatabase();
		
		iterator = database.getCollection(Consts.UsersCollectionName).distinct("Email", String.class).iterator();
		
		broadcaster = r.getBroadcaster();
		
		currentUser = userHelper.getUserAlreadyFromSession(r.getRequest());
		
		run();
	}

	@Override
	public void run() {

		if (resource != null)
		{
			MongoDatabase database = dbHelper.getMongoDatabase();
			
			if (!iterator.hasNext()){

				iterator = database.getCollection(Consts.UsersCollectionName).distinct("Email", String.class).iterator();
			}
			
			String user = iterator.next();
			if (user.equals(currentUser.getEmail())){
				user = iterator.next();
			}
			
			User userObj = converter.ToUser(database.getCollection(Consts.UsersCollectionName).find(new Document("Email", user)).first());

			UserSearchingProto.UserFromSearch.Address address = UserSearchingProto.UserFromSearch.Address.newBuilder()
					.setStreet(userObj.getAddress().street)
					.setHouseNumber(userObj.getAddress().houseNumber)
					.setCity(userObj.getAddress().city)
					.setCounty(userObj.getAddress().county)
					.setPostcode(userObj.getAddress().postcode).build();

			UserSearchingProto.UserFromSearch userFromSearch = UserSearchingProto.UserFromSearch.newBuilder()
					.setFirstName(userObj.getFirstName())
					.setLastName(userObj.getLastName())
					.setPhotoFileId(userObj.getPhotoFileId())
					.setDob(userObj.getDob().getTime())
					.setEmail(userObj.getEmail())
					.setGender(userObj.getGender().getValue())
					.setInterestedIn(userObj.getInterestedIn().getValue())
					.setWaveId(userObj.getWaveId())
					.setAddress(address).build();

			UserSearchingProto.UserSearchingResponse.Builder userPingEvent = UserSearchingProto.UserSearchingResponse.newBuilder()
					.setMessageType(UserSearchingProto.UserSearchingResponse.ResponseType.UserPingEvent)
					.setUser(userFromSearch);

			try {
				broadcaster.broadcast(serializer.toJson(userPingEvent.build()));
			} catch (ProtoSerializer.SerializationException ex)	{
				Console.error(ex.toString());
			}
		}
	}

}
