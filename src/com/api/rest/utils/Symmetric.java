package com.api.rest.utils;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;

public class Symmetric {

	private SecretKeySpec secretKey;
	private Cipher cipher;

	public Symmetric(String secret) throws NoSuchAlgorithmException, NoSuchPaddingException, UnsupportedEncodingException {
		secretKey = generateKey(secret,16);
		cipher = Cipher.getInstance("AES");
	}

	public SecretKeySpec generateKey(String s, int length) throws UnsupportedEncodingException {
		if (s.length()<length) {
			int missingLength = length-s.length();
			for (int i=0;i<missingLength;i++) {
				s += " ";
			}
		}
		byte[] k = s.substring(0, length).getBytes("UTF-8");
		return new SecretKeySpec(k, "AES");
	}
	
	public byte[] encryptData(String data) throws InvalidKeyException, IllegalBlockSizeException, BadPaddingException, UnsupportedEncodingException {
		cipher.init(Cipher.ENCRYPT_MODE, secretKey);
		byte[] enc = cipher.doFinal(data.getBytes("UTF-8"));
		return enc;
	}
	
	public byte[] decryptData(byte[] data) throws InvalidKeyException, IllegalBlockSizeException, BadPaddingException, UnsupportedEncodingException {
		cipher.init(Cipher.DECRYPT_MODE, secretKey);
		byte[] dec = cipher.doFinal(data);
		return dec;
	}

}
