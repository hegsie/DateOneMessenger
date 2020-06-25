package com.greycells.dateone.server.helpers;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.Principal;

import javax.inject.Singleton;
import javax.naming.InvalidNameException;
import javax.naming.ldap.LdapName;
import javax.naming.ldap.Rdn;
import javax.security.auth.Subject;
import javax.security.auth.x500.X500Principal;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.waveprotocol.box.server.CoreSettings;
import org.waveprotocol.box.server.authentication.ParticipantPrincipal;
import org.waveprotocol.box.server.persistence.AccountStore;
import org.waveprotocol.box.server.robots.agent.welcome.WelcomeRobot;
import org.waveprotocol.box.server.util.RegistrationUtil;
import org.waveprotocol.wave.model.id.WaveIdentifiers;
import org.waveprotocol.wave.model.wave.InvalidParticipantAddress;
import org.waveprotocol.wave.model.wave.ParticipantId;

import com.google.common.base.Preconditions;
import com.google.inject.Inject;
import com.google.inject.name.Named;
import com.greycells.dateone.shared.IUser;
import com.greycells.dateone.shared.User;

@Singleton
public class UserHelper {

	private String clientAuthCertDomain;
	private boolean isRegistrationDisabled;
	private AccountStore accountStore;
	private WelcomeRobot welcomeBot;

	@Inject
	public UserHelper(
			AccountStore accountStore,
			@Named(CoreSettings.CLIENTAUTH_CERT_DOMAIN) String clientAuthCertDomain,
			@Named(CoreSettings.DISABLE_REGISTRATION) boolean isRegistrationDisabled,
			WelcomeRobot welcomeBot) {
		this.accountStore = accountStore;
		this.clientAuthCertDomain = clientAuthCertDomain;
		this.isRegistrationDisabled = isRegistrationDisabled;
		this.welcomeBot = welcomeBot;
	}

	private static final String DATEONE_USER = "dateOneUser";
	// The Object ID of the PKCS #9 email address stored in the client
	// certificate.
	// Source:
	// http://www.rsa.com/products/bsafe/documentation/sslc251html/group__AD__COMMON__OIDS.html
	private static final String OID_EMAIL = "1.2.840.113549.1.9.1";

	public User getUserAlreadyFromSession(HttpServletRequest request) {
		User user = null;
		HttpSession session = request.getSession();
		Object userObj = session.getAttribute(DATEONE_USER);
		if (userObj != null && userObj instanceof User) {
			user = (User) userObj;
		}
		return user;
	}

	public void storeUserInSession(HttpServletRequest request, IUser user) {
		HttpSession session = request.getSession(true);
		user.setSession(session.getId());
		session.setAttribute(DATEONE_USER, user);
	}

	public void deleteUserFromSession(HttpServletRequest request) {
		HttpSession session = request.getSession();
		session.removeAttribute(DATEONE_USER);
	}

	public String createProfilePicFilename(User user) throws IOException {
		return File.createTempFile("profilePic",
				user.getUsername().replace("@", "-")).getName();
	}

	/**
	 * Get the participant id of the given subject.
	 * 
	 * The subject is searched for compatible principals. When other
	 * authentication types are added, this method will need to be updated to
	 * support their principal types.
	 * 
	 * @throws InvalidParticipantAddress
	 *             The subject's address is invalid
	 */
	public ParticipantId getLoggedInUser(Subject subject)
			throws InvalidParticipantAddress {
		String address = null;

		for (Principal p : subject.getPrincipals()) {
			// TODO(josephg): When we support other authentication types (LDAP,
			// etc),
			// this method will need to read the address portion out of the
			// other principal types.
			if (p instanceof ParticipantPrincipal) {
				address = ((ParticipantPrincipal) p).getName();
				break;
			} else if (p instanceof X500Principal) {
				return attemptClientCertificateLogin((X500Principal) p);
			}
		}

		return address == null ? null : ParticipantId.of(address);
	}

	/**
	 * Attempts to authenticate the user using their client certificate.
	 * 
	 * Retrieves the email from their certificate, using it as the wave
	 * username. If the user doesn't exist and registration is enabled, it will
	 * automatically create an account before continuing. Otherwise it will
	 * simply check if the account exists and authenticate based on that.
	 * 
	 * @throws RuntimeException
	 *             The encoding of the email is unsupported on this system
	 * @throws InvalidParticipantAddress
	 *             The email address doesn't correspond to an account
	 */
	private ParticipantId attemptClientCertificateLogin(X500Principal p)
			throws RuntimeException, InvalidParticipantAddress {
		String distinguishedName = p.getName();
		try {
			LdapName ldapName = new LdapName(distinguishedName);
			for (Rdn rdn : ldapName.getRdns()) {
				if (rdn.getType().equals(OID_EMAIL)) {
					String email = decodeEmailFromCertificate((byte[]) rdn
							.getValue());
					if (email.endsWith("@" + clientAuthCertDomain)) {
						// Check we decoded the string correctly.
						Preconditions
								.checkState(WaveIdentifiers
										.isValidIdentifier(email),
										"The decoded email is not a valid wave identifier");
						ParticipantId id = ParticipantId.of(email);
						if (!RegistrationUtil
								.doesAccountExist(accountStore, id)) {
							if (!isRegistrationDisabled) {
								if (!RegistrationUtil.createAccountIfMissing(
										accountStore, id, null, welcomeBot)) {
									return null;
								}
							} else {
								throw new InvalidNameException(
										"User doesn't already exist, and registration disabled by administrator");
							}
						}
						return id;
					}
				}
			}
		} catch (UnsupportedEncodingException ex) {
			throw new RuntimeException(ex);
		} catch (InvalidNameException ex) {
			throw new InvalidParticipantAddress(distinguishedName,
					"Certificate does not contain a valid distinguished name");
		}
		return null;
	}

	/**
	 * Decodes the user email from the X.509 certificate.
	 * 
	 * Email address is assumed to be valid in ASCII, and less than 128
	 * characters long
	 * 
	 * @param encoded
	 *            Output from rdn.getValue(). 1st byte is the tag, second is the
	 *            length.
	 * @return The decoded email in ASCII
	 * @throws UnsupportedEncodingException
	 *             The email address wasn't in ASCII
	 */
	private String decodeEmailFromCertificate(byte[] encoded)
			throws UnsupportedEncodingException {
		// Check for < 130, since first 2 bytes are taken up as stated above.
		Preconditions.checkState(encoded.length < 130,
				"The email address is longer than expected");
		return new String(encoded, 2, encoded.length - 2, "ascii");
	}
}