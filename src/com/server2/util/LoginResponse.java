package com.server2.util;

/**
 * 
 * @author Rene Roosen
 * 
 */
public class LoginResponse {

	public static final int LOGIN_RESPONSE_OK = 2;
	public static final int LOGIN_RESPONSE_INVALID_CREDENTIALS = 3;
	public static final int LOGIN_RESPONSE_ACCOUNT_DISABLED = 4;
	public static final int LOGIN_RESPONSE_ACCOUNT_ONLINE = 5;
	public static final int LOGIN_RESPONSE_UPDATED = 6;
	public static final int LOGIN_RESPONSE_WORLD_FULL = 7;
	public static final int LOGIN_RESPONSE_LOGIN_SERVER_OFFLINE = 8;
	public static final int LOGIN_RESPONSE_LOGIN_LIMIT_EXCEEDED = 9;
	public static final int LOGIN_RESPONSE_BAD_SESSION_ID = 10;
	public static final int LOGIN_RESPONSE_PLEASE_TRY_AGAIN = 11;
	public static final int LOGIN_RESPONSE_NEED_MEMBERS = 12;
	public static final int LOGIN_RESPONSE_COULD_NOT_COMPLETE_LOGIN = 13;
	public static final int LOGIN_RESPONSE_SERVER_BEING_UPDATED = 14;
	public static final int LOGIN_RESPONSE_LOGIN_ATTEMPTS_EXCEEDED = 16;
	public static final int LOGIN_RESPONSE_MEMBERS_ONLY_AREA = 17;
}
