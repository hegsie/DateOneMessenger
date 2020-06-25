package com.greycells.dateone.server;

import java.util.Date;

import com.greycells.dateone.shared.Address;
import com.greycells.dateone.shared.User;
import com.greycells.dateone.shared.Gender;
import com.greycells.dateone.shared.InterestedIn;
import org.bson.Document;

public class DocumentUserConverter {

	private static final String WAVE_ID = "WaveId";
	private static final String PHOTO_FIELD = "PhotoField";
	private static final String LAST_LOGIN = "LastLogin";
	private static final String PWD_HASH = "PwdHash";
	private static final String INTERESTED_IN = "InterestedIn";
	private static final String GENDER = "Gender";
	private static final String DOB = "DOB";
	private static final String POSTCODE = "Postcode";
	private static final String COUNTY = "County";
	private static final String CITY = "City";
	private static final String STREET = "Street";
	private static final String HOUSE_NUMBER = "HouseNumber";
	private static final String ADDRESS = "Address";
	private static final String EMAIL = "Email";
	private static final String LAST_NAME = "LastName";
	private static final String FIRST_NAME = "FirstName";

	public User ToUser(Document doc){
		
		String firstName = doc.getString(FIRST_NAME);
		String lastName = doc.getString(LAST_NAME);
		String email = doc.getString(EMAIL);
		Date dob = doc.getDate(DOB);
		Gender gender = Gender.valueOf(doc.getString(GENDER));
		InterestedIn interestedIn = InterestedIn.valueOf(doc.getString(INTERESTED_IN));
		String pwdHash = doc.getString(PWD_HASH);
		Long lastLogin = doc.getLong(LAST_LOGIN);
		String photoField = doc.getString(PHOTO_FIELD);
		String waveId = doc.getString(WAVE_ID);

		Document addressDoc = doc.get(ADDRESS, Document.class);
		Address address = new Address(addressDoc.getString(HOUSE_NUMBER),
				addressDoc.getString(STREET),
				addressDoc.getString(CITY),
				addressDoc.getString(COUNTY),
				addressDoc.getString(POSTCODE));
		
		return new User(
				firstName,
				lastName,
				email, 
				address,
				dob,
				gender,
				interestedIn,
				pwdHash,
				lastLogin,
				photoField,
				waveId);		
	}
	
	public Document ToDocument(User user){
		return new Document()
				.append(FIRST_NAME, user.getFirstName())
				.append(LAST_NAME, user.getLastName())
				.append(EMAIL, user.getEmail())
				.append(ADDRESS, new Document()
						.append(HOUSE_NUMBER, user.getAddress().houseNumber.toString())
						.append(STREET, user.getAddress().street)
						.append(CITY, user.getAddress().city)
						.append(COUNTY, user.getAddress().county)
						.append(POSTCODE, user.getAddress().postcode))
				.append(DOB, user.getDob())
				.append(GENDER, user.getGender().toString())
				.append(INTERESTED_IN, user.getInterestedIn().toString())
				.append(PWD_HASH, user.getPwdHash())
				.append(LAST_LOGIN, user.getLastLogin())
				.append(PHOTO_FIELD, user.getPhotoFileId())
				.append(WAVE_ID, user.getWaveId());
	}
}
