package com.api.rest.utils;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Date;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import com.api.rest.arq.exception.EncryptionException;
import org.apache.log4j.Logger;

public class AuthTools {
	private static Logger logger = Logger.getLogger(AuthTools.class);
	
	public static String generateToken(String username, String password, Date generated) throws EncryptionException {
		String token = username+":"+password+":"+generated.getTime();
		try {
			Symmetric symm = new Symmetric(PropLoader.get(Constants.AUTH_SECRET));
			byte[] encryptedToken = symm.encryptData(token);
			byte[] encodedToken = Base64.getEncoder().encode(encryptedToken);
			return new String(encodedToken,"UTF-8");
		} catch (NoSuchAlgorithmException | NoSuchPaddingException | UnsupportedEncodingException | InvalidKeyException | IllegalBlockSizeException | BadPaddingException e) {
			logger.error("Ecryption error ",e);
			throw new EncryptionException("Encryption error",e);
		}
	}
	
	public static String[] readToken(String encodedTokenData) throws EncryptionException {
		try {
			byte[] decodedtokenData = Base64.getDecoder().decode(encodedTokenData.getBytes("UTF-8"));
			Symmetric symm = new Symmetric(PropLoader.get(Constants.AUTH_SECRET));
			String decryptedTokenData = new String(symm.decryptData(decodedtokenData), "UTF-8");
			String[] token = decryptedTokenData.split(":");
			return token;
		} catch (NoSuchAlgorithmException | NoSuchPaddingException | UnsupportedEncodingException | InvalidKeyException | IllegalBlockSizeException | BadPaddingException e) {
			logger.error("Ecryption error ",e);
			throw new EncryptionException("Encryption error",e);
		}
	}
	
}
