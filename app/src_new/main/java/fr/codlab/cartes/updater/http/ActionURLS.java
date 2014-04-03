package fr.codlab.cartes.updater.http;

public class ActionURLS {

	/* The server root page URL */
	private static final String SERVER_ROOT = "?";

	/* Type Level */
	private static final String TYPE_ATTR = "type";
	private static final String USER_TYPE_ATTR = "user";

	/* Action Level */
	private static final String ACTION_ATTR = "action";
	private static final String CREATE_ACTION_ATTR = "create";
	private static final String AUTHENTICATE_ACTION_ATTR = "auth";
	private static final String DOWNLOAD_ACTION_ATTR = "download";
	private static final String UPLOAD_ACTION_ATTR = "upload";

	/* Data level */
	private static final String LOGIN_DATA_ATTR = "login";
	private static final String PASSWORD_DATA_ATTR = "password";

	// Request hierarchy :
	// 1. Type {project, file, user}
	// 2. Action {auth, create, update, delete,explore, compile}
	// 3. Data {login, password, project, file}

	// ----------------------------
	// USER TYPE URLS
	// ----------------------------

	public static String getCreateUserURL(String login, String password) {
		return new StringBuilder()
				.append(SERVER_ROOT + TYPE_ATTR + "=" + USER_TYPE_ATTR)
				.append("&" + ACTION_ATTR + "=" + CREATE_ACTION_ATTR)
				.append("&" + LOGIN_DATA_ATTR + "=" + login)
				.append("&" + PASSWORD_DATA_ATTR + "=" + password).toString();
	}

	public static String getAuthenticationURL(String login, String password) {
		return new StringBuilder()
				.append(SERVER_ROOT + TYPE_ATTR + "=" + USER_TYPE_ATTR)
				.append("&" + ACTION_ATTR + "=" + AUTHENTICATE_ACTION_ATTR)
				.append("&" + LOGIN_DATA_ATTR + "=" + login)
				.append("&" + PASSWORD_DATA_ATTR + "=" + password).toString();
	}

	public static String getDownloadURL(String login, String password) {
		return new StringBuilder()
				.append(SERVER_ROOT + TYPE_ATTR + "=" + USER_TYPE_ATTR)
				.append("&" + ACTION_ATTR + "=" + DOWNLOAD_ACTION_ATTR)
				.append("&" + LOGIN_DATA_ATTR + "=" + login)
				.append("&" + PASSWORD_DATA_ATTR + "=" + password).toString();
	}

	public static String getUploadURL(String login, String password) {
		return new StringBuilder()
				.append(SERVER_ROOT + TYPE_ATTR + "=" + USER_TYPE_ATTR)
				.append("&" + ACTION_ATTR + "=" + UPLOAD_ACTION_ATTR)
				.append("&" + LOGIN_DATA_ATTR + "=" + login)
				.append("&" + PASSWORD_DATA_ATTR + "=" + password).toString();
	}

}
