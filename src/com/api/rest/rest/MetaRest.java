package com.api.rest.rest;

import javax.ws.rs.POST;
import javax.ws.rs.Path;

@Path("/meta")
public class MetaRest {

	// You can use this API to verify you're talking the right API ;)
	@POST
	public String validateServer(String question) {
		if (question.equalsIgnoreCase("are you the rest server?")) {
			return "yes i am!";
		} else {
			return "wot u want??";
		}
	}
	
}
