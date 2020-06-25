package com.greycells.dateone.server;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.google.inject.Singleton;
import com.google.inject.name.Named;
import com.greycells.dateone.client.DateOneService;
import com.greycells.dateone.server.helpers.DBHelper;
import com.greycells.dateone.server.helpers.UserHelper;
import com.greycells.dateone.shared.MessageStatus;
import com.greycells.dateone.shared.User;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;

import java.util.regex.Pattern;

import javax.inject.Inject;
import javax.security.auth.Subject;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.login.Configuration;
import javax.security.auth.login.LoginContext;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpSession;

import org.bson.Document;
import org.eclipse.jetty.util.MultiMap;
import org.waveprotocol.box.server.CoreSettings;
import org.waveprotocol.box.server.authentication.HttpRequestBasedCallbackHandler;
import org.waveprotocol.box.server.authentication.PasswordDigest;
import org.waveprotocol.box.server.authentication.SessionManager;
import org.waveprotocol.box.server.persistence.AccountStore;
import org.waveprotocol.box.server.robots.agent.welcome.WelcomeRobot;
import org.waveprotocol.box.server.util.RegistrationUtil;
import org.waveprotocol.wave.model.wave.ParticipantId;

@Singleton
public class DateOneServiceImpl extends RemoteServiceServlet implements
		DateOneService {

	/**
	 */
	private static final long serialVersionUID = 1L;
	private DefaultUsers defaultUsers;
	private final SessionManager sessionManager;
	private Configuration configuration;
	private UserHelper userHelper;
	private DBHelper dbHelper;
	private AccountStore accountStore;
	private WelcomeRobot welcomeBot;
	private String waveDomain;
	private DocumentUserConverter converter;

	@Inject
	public DateOneServiceImpl(DefaultUsers defaultUsers,
			SessionManager sessionManager, Configuration configuration,
			UserHelper userHelper, DBHelper dbHelper, AccountStore accountStore, WelcomeRobot welcomeBot,
			@Named(CoreSettings.WAVE_SERVER_DOMAIN) String waveDomain, DocumentUserConverter converter) {
		this.defaultUsers = defaultUsers;
		this.sessionManager = sessionManager;
		this.configuration = configuration;
		this.userHelper = userHelper;
		this.dbHelper = dbHelper;
		this.accountStore = accountStore;
		this.welcomeBot = welcomeBot;
		this.waveDomain = waveDomain;
		this.converter = converter;
	}

	@Override
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
	}

	@Override
	public MessageStatus loginUser(String username, String password) {
	
		MongoCollection<Document> users = dbHelper.getMongoDatabase().getCollection(Consts.UsersCollectionName);
		
		FindIterable<Document> doc = users.find(new Document("Email", Pattern.compile("hegsie@greycells.cloudapp.net", Pattern.CASE_INSENSITIVE)));
		
		if (doc.first() == null){
			// Generate all the default records if they don't exist

			ServletContext servletContext = getServletContext();
			defaultUsers.initContacts(servletContext, converter);
		}

		MessageStatus status = new MessageStatus();
		status.Status = MessageStatus.Statuses.Error;
		status.Message = "Username and Password do not match our records!";
		
		FindIterable<Document> attemptLogin = users.find(new Document("Email", Pattern.compile(username, Pattern.CASE_INSENSITIVE)));
		if (attemptLogin.first() != null){
			
			User user = converter.ToUser(attemptLogin.first());
			
			if (user != null) {
				HttpSession session = this.getThreadLocalRequest().getSession(true);

				try {
					// Login to the wave system
					// {address=hegsie, signIn=Sign+in, password=hello}
					Subject subject = new Subject();
					MultiMap<String> parameters = new MultiMap<>();
					parameters.add("address", user.getUsername().split("@")[0]);
					parameters.add("signIn", "Sign+in");
					parameters.add("password", password);

					CallbackHandler callbackHandler = new HttpRequestBasedCallbackHandler(
							parameters);

					LoginContext context = new LoginContext("Wave", subject,
							callbackHandler, configuration);

					// If authentication fails, login() will throw a LoginException.
					context.login();

					sessionManager.setLoggedInUser(session,
							userHelper.getLoggedInUser(subject));

				} catch (Exception ex) {
					return status;
				}
				
				boolean valid = BCrypt.checkpw(password, user.getPwdHash());
				if (valid) {
					user.loggedIn();
					status.Message = "";
					status.Status = MessageStatus.Statuses.Success;

					userHelper.storeUserInSession(this.getThreadLocalRequest(),
							user);
				}
			}
		}

		return status;
	}

	@Override
	public MessageStatus registerUser(User user, String password) {
		String userShortName = user.getUsername().split("@")[0];
		
		MessageStatus status = new MessageStatus();
		status.Status = MessageStatus.Statuses.Error;
		try {
			user.setWaveId(userShortName + "@" + waveDomain);
			user.setPwdHash(BCrypt.hashpw(password, BCrypt.gensalt()));
						
			ParticipantId id = RegistrationUtil.checkNewUsername(
					waveDomain, userShortName);

			RegistrationUtil.createAccountIfMissing(accountStore,
					id,
					new PasswordDigest(password.toCharArray()),
					welcomeBot);
			
			dbHelper.persist(user);
						
			status.Status = MessageStatus.Statuses.Success;
		} catch (Exception ex) {
			status.Message = ex.getMessage();
		}

		return status;
	}

	@Override
	public MessageStatus editProfile() {
		MessageStatus status = new MessageStatus();
		status.Status = MessageStatus.Statuses.Success;
		return status;
	}

	@Override
	public User loginFromSessionServer() {
		ParticipantId id = sessionManager.getLoggedInUser(this
				.getThreadLocalRequest().getSession(false));

		if (id != null)
			return userHelper.getUserAlreadyFromSession(this
					.getThreadLocalRequest());
		return null;
	}

	@Override
	public MessageStatus changePassword(String name, String newPassword) {
		return new MessageStatus();
	}

	@Override
	public MessageStatus logout() {
		userHelper.deleteUserFromSession(this.getThreadLocalRequest());

		return new MessageStatus();
	}

	@Override
	public MessageStatus beginSearching(int userToSearchAreaRatio) {
		return new MessageStatus();
	}

	@Override
	public MessageStatus endSearching() {
		return new MessageStatus();
	}
}
