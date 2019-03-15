package com.api.rest.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import com.api.rest.arq.bean.ValidatedBean;
import org.apache.log4j.Logger;

import com.google.gson.Gson;

public class CaptchaUtils {
	private static Logger logger = Logger.getLogger(CaptchaUtils.class);
	
	public static boolean validateCaptcha(ValidatedBean validated, String captchaKey) {
		String captchaValue = validated.getValidationKey();
		String endpoint = PropLoader.get(Constants.CAPTCHA_VALIDATION_ENDPOINT);
		String params = "secret="+captchaKey+"&response="+captchaValue;
		
		try {
			URL url = new URL(endpoint);
			HttpURLConnection http = (HttpURLConnection)url.openConnection();
			http.setDoOutput(true);
			http.setRequestMethod("POST");
			http.setConnectTimeout(15000);
			http.setReadTimeout(15000);
			
			OutputStream os = http.getOutputStream();
			os.write(params.toString().getBytes("UTF-8"));
			os.flush();
			
			int responseCode = http.getResponseCode();
			
			if (responseCode!=200) {
				http.disconnect();
				return false;
			}
			
			String response = "";
			String output = "";
			BufferedReader br = new BufferedReader(new InputStreamReader((http.getInputStream())));
			while ((output = br.readLine()) != null) {
				response += output;
			}
			
			Gson gson = new Gson();
			Map<String,Object> responseMap = new HashMap<String,Object>();
			responseMap = gson.fromJson(response, responseMap.getClass());

			http.disconnect();
			
			return responseMap!=null && responseMap.get("success")!=null && responseMap.get("success").toString().equalsIgnoreCase("true");
		} catch (IOException e) {
			logger.error("Error validating CAPTCHA against GOOGLE RECAPTCHA endpoint ",e);
			return false;
		}
	}
	
}
