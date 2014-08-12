package com.krakhoul.api;

import org.vertx.java.core.Handler;
import org.vertx.java.core.Vertx;
import org.vertx.java.core.eventbus.EventBus;
import org.vertx.java.core.eventbus.Message;
import org.vertx.java.core.http.HttpServerRequest;
import org.vertx.java.core.http.RouteMatcher;
import org.vertx.java.core.json.JsonArray;
import org.vertx.java.core.json.JsonObject;

public class PlacesApi implements PiikApi {
	
	private final RouteMatcher routeMatcher;
	private final Vertx vertx;
	private final EventBus eventBus;
	
	public PlacesApi(RouteMatcher routeMatcher, Vertx vertx) {
		this.routeMatcher = routeMatcher;
		this.vertx = vertx;
		this.eventBus = this.vertx.eventBus();
	}
	
	public void initRoutes() {
		routeMatcher.get("/api/places", req -> {
			eventBus.send("krakhoul.Places.findNearBy", latLonFromRequest(req), sendPlacesResponse(req));
		});
	}
	
	private Handler<Message<JsonArray>> sendPlacesResponse(final HttpServerRequest req) {
		return (Message<JsonArray> message) -> {
			String json = message.body().encodePrettily();
			response(req.response(), json);
		};
	}

	private JsonObject latLonFromRequest(HttpServerRequest req) {
		JsonObject latlon = new JsonObject();
		latlon.putString("lat", req.params().get("lat"));
		latlon.putString("lon", req.params().get("lon"));
		return latlon;
	}
	
}
