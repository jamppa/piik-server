package com.krakhoul.verticle;

import org.vertx.java.core.Handler;
import org.vertx.java.core.eventbus.Message;
import org.vertx.java.core.json.JsonArray;
import org.vertx.java.core.json.JsonObject;
import org.vertx.java.platform.Verticle;

import com.krakhoul.fs.SearchVenuesApi;
import com.krakhoul.model.PhotosList;
import com.krakhoul.model.PlacesList;

public class PlacesVerticle extends Verticle {
	
	public void start() {
		vertx.eventBus().registerHandler("krakhoul.Places.findNearBy", findNearByHandler());
	}

	private Handler<Message<JsonObject>> findNearByHandler() {
		return (Message<JsonObject> message) -> {
			new SearchVenuesApi(vertx).searchVenuesNear(message.body(), (venues) -> {		
				PlacesList places = new PlacesList(venues);
				vertx.eventBus().send("krakhoul.PlacesPhotos.findAll", places.placeIdsAsJson(), (Message<JsonArray> photos) -> {
					places.addPhotos(new PhotosList(photos.body()));
					message.reply(places.withPhoto().asJson());
				});
			});
		};
	}
	
}
