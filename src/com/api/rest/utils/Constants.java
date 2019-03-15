package com.api.rest.utils;

public class Constants {

	public static String DB_ENABLE = "database.enable";
	public static String JDBC_URL = "database.jdbc.url";
	public static String JDBC_DRIVER= "database.jdbc.driver";
	
	public static String SERVER_KEYSTORE_FILE = "server.keystore.file";
	public static String SERVER_KEYSTORE_PASS = "server.keystore.pass";
	public static String SERVER_PORT_HTTP = "server.port.http";
	public static String SERVER_PORT_HTTPS = "server.port.https";
	
	public static String AUTH_SECRET = "auth.encryption.secret";
	public static String AUTH_SESSION_DURATION = "auth.session.duration";
	public static String REGISTER_ENABLED = "auth.register.enabled";

	public static String CAPTCHA_REQUIRED_FOR_REGISTER = "antispam.recaptcha.register.required";
	public static String CAPTCHA_KEY_FOR_REGISTER = "antispam.recaptcha.register.api.secret";
	public static String CAPTCHA_VALIDATION_ENDPOINT = "antispam.recaptcha.endpoint";
	
}
