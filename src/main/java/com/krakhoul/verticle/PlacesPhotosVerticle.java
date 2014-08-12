package com.krakhoul.verticle;

import java.util.ArrayList;
import java.util.List;

import org.vertx.java.core.Handler;
import org.vertx.java.core.eventbus.Message;
import org.vertx.java.core.json.JsonArray;
import org.vertx.java.platform.Verticle;

import com.krakhoul.fs.SearchPhotosApi;

public class PlacesPhotosVerticle extends Verticle {
	
	@Override
	public void start() {
		vertx.eventBus().registerHandler("krakhoul.PlacesPhotos.findAll", findAllHandler());
	}

	private Handler<Message<JsonArray>> findAllHandler() {
		return (message) -> {
			new SearchPhotosApi(vertx, getPlaceIds(message.body())).searchLatestPhotos(photosMessage -> {
				message.reply(photosMessage);
			});
		};
	}

	private List<String> getPlaceIds(final JsonArray json) {
		List<String> placeIds = new ArrayList<String>();
		for(int i = 0; i < json.size(); i++){
			placeIds.add(json.get(i));
		}
		return placeIds;
	}
	
}
