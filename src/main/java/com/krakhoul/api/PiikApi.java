package com.krakhoul.api;

import org.vertx.java.core.http.HttpServerResponse;

public interface PiikApi {
	
	public void initRoutes();
	
	default void response(final HttpServerResponse response, final String payload) {
		response.putHeader("Content-Type", "application/json; charset=utf-8");
		response.end(payload);
	}
}
