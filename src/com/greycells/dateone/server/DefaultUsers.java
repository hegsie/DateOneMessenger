package com.greycells.dateone.server;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.regex.Pattern;

import javax.servlet.ServletContext;

import com.greycells.dateone.shared.Address;
import com.greycells.dateone.shared.User;
import org.bson.Document;
import org.waveprotocol.box.server.CoreSettings;
import org.waveprotocol.box.server.authentication.PasswordDigest;
import org.waveprotocol.box.server.persistence.AccountStore;
import org.waveprotocol.box.server.robots.agent.welcome.WelcomeRobot;
import org.waveprotocol.box.server.util.RegistrationUtil;
import org.waveprotocol.wave.model.wave.ParticipantId;

import com.greycells.dateone.shared.Gender;
import com.greycells.dateone.shared.InterestedIn;
import com.google.inject.Inject;
import com.google.inject.name.Named;
import com.greycells.dateone.server.helpers.DBHelper;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoDatabase;

public class DefaultUsers {

	private static final String HELLO_PWD = "hello";

	private static final String HASHPW = BCrypt.hashpw(HELLO_PWD,
			BCrypt.gensalt());

	private static final ArrayList<User> users = new ArrayList<User>();

	private final AccountStore accountStore;
	private final WelcomeRobot welcomeBot;

	private DBHelper dbHelper;

	private String domain;

	@Inject
	public DefaultUsers(AccountStore accountStore, WelcomeRobot welcomeBot,
			DBHelper dbHelper,@Named(CoreSettings.WAVE_SERVER_DOMAIN) String domain) {
		this.accountStore = accountStore;
		this.welcomeBot = welcomeBot;
		this.dbHelper = dbHelper;
		this.domain = domain;
		
		Address blankAddress = new Address("33","Mayflower Rd","London","England","SW9 9JY");
		
		Calendar calendar = GregorianCalendar.getInstance();

		calendar.set(1981, Calendar.NOVEMBER, 28);
		users.add(new User("Ben", "Hegarty", "hegsie@greycells.cloudapp.net",
				blankAddress, calendar.getTime(), Gender.Man,
				InterestedIn.Men, HASHPW, 0, "/WEB-INF/default.images/Ben.jpg",
				"hegsie@" + domain));

		calendar.set(1983, Calendar.SEPTEMBER, 05);
		users.add(new User("Devin", "Baghusrt",
				"devin.hegarty@greycells.cloudapp.net", blankAddress, calendar
						.getTime(), Gender.Man, InterestedIn.Men, HASHPW, 0,
				"/WEB-INF/default.images/devin.jpg",
				"devin.hegarty@" + domain));

		calendar.set(1985, Calendar.JULY, 15);
		users.add(new User(
				"Hollie",
				"Voss",
				"hollie@greycells.cloudapp.net",
				blankAddress,
				calendar.getTime(),
				Gender.Woman,
				InterestedIn.Men,
				HASHPW,
				0,
				"/WEB-INF/default.images/girls_a_pretty_girl_on_an_orange_background_011825_.jpg",
				"hollie@" + domain));

		calendar.set(1970, Calendar.FEBRUARY, 31);
		users.add(new User("Emerson", "Milton",
				"emerson@greycells.cloudapp.net", blankAddress, calendar
						.getTime(), Gender.Man, InterestedIn.Men, HASHPW, 0,
				"/WEB-INF/default.images/DFYltJGWdoqvd5jhLqZENIyQo1_500.jpg",
				"emerson@" + domain));

		calendar.set(1975, Calendar.JULY, 17);
		users.add(new User("Healy", "Colette", "healy@greycells.cloudapp.net",
				blankAddress, calendar.getTime(), Gender.Woman,
				InterestedIn.Men, HASHPW, 0,
				"/WEB-INF/default.images/images_001.jpg", "healy@" + domain));

		calendar.set(1972, Calendar.OCTOBER, 17);
		users.add(new User("Brigitte", "Cobb",
				"brigitte@greycells.cloudapp.net", blankAddress, calendar
						.getTime(), Gender.Woman, InterestedIn.Men, HASHPW, 0,
				"/WEB-INF/default.images/Brigitte.jpg", "brigitte@" + domain));

		calendar.set(1978, Calendar.JANUARY, 14);
		users.add(new User("Elba", "Lockhart", "elba@greycells.cloudapp.net",
				blankAddress, calendar.getTime(), Gender.Woman,
				InterestedIn.Men, HASHPW, 0,
				"/WEB-INF/default.images/th_002.jpg", "elba@" + domain));

		calendar.set(1975, Calendar.SEPTEMBER, 01);
		users.add(new User("Claudio", "Engle",
				"claudio@greycells.cloudapp.net", blankAddress, calendar
						.getTime(), Gender.Woman, InterestedIn.Men, HASHPW, 0,
				"/WEB-INF/default.images/th_005.jpg", "claudio@" + domain));

		calendar.set(1980, Calendar.MARCH, 10);
		users.add(new User(
				"Dena",
				"Pacheco",
				"dena@greycells.cloudapp.net",
				blankAddress,
				calendar.getTime(),
				Gender.Woman,
				InterestedIn.Men,
				HASHPW,
				0,
				"/WEB-INF/default.images/tumblr_m3n9dyEntC1r76x8zo1_500_large.jpg",
				"dena@" + domain));

		calendar.set(1987, Calendar.APRIL, 16);
		users.add(new User("Christina", "Blake",
				"cblake@greycells.cloudapp.net", blankAddress, calendar
						.getTime(), Gender.Woman, InterestedIn.Women, HASHPW,
				0, "/WEB-INF/default.images/th_004.jpg", "cblake@" + domain));

		calendar.set(1987, Calendar.APRIL, 16);
		users.add(new User(
				"Gail",
				"Horton",
				"gailh@greycells.cloudapp.net",
				blankAddress,
				calendar.getTime(),
				Gender.Woman,
				InterestedIn.Men,
				HASHPW,
				0,
				"/WEB-INF/default.images/Beautiful_Portraits_of_Random_Strangers_8-620x434.jpg",
				"gailh@" + domain));

		calendar.set(1989, Calendar.MARCH, 28);
		users.add(new User(
				"Orville",
				"Daniel",
				"orville@greycells.cloudapp.net",
				blankAddress,
				calendar.getTime(),
				Gender.Woman,
				InterestedIn.Men,
				HASHPW,
				0,
				"/WEB-INF/default.images/258042253619716592_R0d8ToBc_c_large.jpg",
				"orville@" + domain));

		calendar.set(1985, Calendar.JUNE, 25);
		users.add(new User(
				"Rae",
				"Childers",
				"rchilders@greycells.cloudapp.net",
				blankAddress,
				calendar.getTime(),
				Gender.Man,
				InterestedIn.Women,
				HASHPW,
				0,
				"/WEB-INF/default.images/Channing-Tatum-channing-tatum-20369900-1000-1483.jpg",
				"rchilders@" + domain));

		calendar.set(1984, Calendar.AUGUST, 14);
		users.add(new User("Mildred", "Starnes",
				"Mildred@greycells.cloudapp.net", blankAddress, calendar
						.getTime(), Gender.Woman, InterestedIn.Women, HASHPW,
				0, "/WEB-INF/default.images/july-24-2008-28.jpg",
				"Mildred@" + domain));

		calendar.set(1982, Calendar.JULY, 20);
		users.add(new User("Candice", "Carson",
				"Candice@greycells.cloudapp.net", blankAddress, calendar
						.getTime(), Gender.Woman, InterestedIn.Men, HASHPW, 0,
				"/WEB-INF/default.images/images copy.jpg",
				"Candice@" + domain));

		calendar.set(1978, Calendar.DECEMBER, 12);
		users.add(new User("Louise", "Kelchner",
				"Louise@greycells.cloudapp.net", blankAddress, calendar
						.getTime(), Gender.Woman, InterestedIn.Men, HASHPW, 0,
				"/WEB-INF/default.images/pretty-woman copy.jpg",
				"Louise@" + domain));

		calendar.set(1980, Calendar.DECEMBER, 15);
		users.add(new User("Emilio", "Hutchinson",
				"Emilio@greycells.cloudapp.net", blankAddress, calendar
						.getTime(), Gender.Man, InterestedIn.Women, HASHPW, 0,
				"/WEB-INF/default.images/PS_1076W_DADS_KILL.jpg",
				"Emilio@" + domain));

		calendar.set(1980, Calendar.DECEMBER, 15);
		users.add(new User("Gene", "Underwood", "Gene@greycells.cloudapp.net",
				blankAddress, calendar.getTime(), Gender.Man,
				InterestedIn.Women, HASHPW, 0,
				"/WEB-INF/default.images/aaa.jpg", "Gene@" + domain));
	}

	public void initContacts(final ServletContext servletContext, final DocumentUserConverter converter) {
		try {
			int numUsers = users.size();
			
			final MongoDatabase database = dbHelper.getMongoDatabase();
			// Insert default contacts if none exist.
			FindIterable<Document> iterable = database.getCollection(Consts.UsersCollectionName)
					.find(com.mongodb.client.model.Filters.eq("Email", Pattern.compile(users.get(0).getEmail(),	Pattern.CASE_INSENSITIVE)));
			
			if (iterable.first() == null) {
				for (int i = 0; i < numUsers && i < numUsers && i < numUsers; ++i) {
					try {
						User contact = users.get(i);

						InputStream inStream = servletContext
								.getResourceAsStream(contact.getPhotoFileId());

						String filename = dbHelper.saveProfilePicture(inStream,
								contact);

						contact.setPhotoFileId(filename);

						database.getCollection(Consts.UsersCollectionName).insertOne(converter.ToDocument(contact));

						ParticipantId id = RegistrationUtil.checkNewUsername(
								domain, contact.getUsername()
										.split("@")[0]);

						RegistrationUtil.createAccountIfMissing(accountStore,
								id,
								new PasswordDigest(HELLO_PWD.toCharArray()),
								welcomeBot);
					} catch (Exception ex) {
						System.out.print(ex);
					}
				}
			}
			
		} catch (Exception ex) {
			System.out.print(ex);
		}
	}
}
