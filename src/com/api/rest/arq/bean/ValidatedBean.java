package com.api.rest.arq.bean;

public abstract class ValidatedBean {
	
	protected String validationKey;

	public String getValidationKey() {
		return validationKey;
	}

	public void setValidationKey(String validationKey) {
		this.validationKey = validationKey;
	}
	
}
