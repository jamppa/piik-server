package com.krakhoul.fs;

import static java.util.stream.Collectors.toList;

import java.util.ArrayList;
import java.util.List;

import org.vertx.java.core.Handler;
import org.vertx.java.core.Vertx;
import org.vertx.java.core.buffer.Buffer;
import org.vertx.java.core.http.HttpClient;
import org.vertx.java.core.http.HttpClientRequest;
import org.vertx.java.core.json.JsonArray;
import org.vertx.java.core.json.JsonObject;

public class SearchPhotosApi implements FourSquareApi {

	private final HttpClient httpClient;
	private final List<String> placeIds = new ArrayList<String>();
	private final JsonArray photos = new JsonArray();
	
	private static final String END_POINT_TEMPLATE = "/v2/venues/{venueId}/photos?group=venue&limit=1";
	
	public SearchPhotosApi(Vertx vertx, List<String> placeIds) {
		this.httpClient = client(vertx);
		this.placeIds.addAll(placeIds);
	}
	
	public void searchLatestPhotos(Handler<JsonArray> callback) {
		placeIds.stream().map(id -> {
			return buildRequestWithHandler(id, callback);
		}).collect(toList()).forEach(req -> req.end());
	}

	private HttpClientRequest buildRequestWithHandler(final String id, final Handler<JsonArray> callback) {
		return httpClient.get(endPointUrl(id), resp -> {
			final Buffer body = new Buffer(0);
			resp.dataHandler(data -> {
				body.appendBuffer(data);
			});
			resp.endHandler((Void) -> {
				JsonObject response = new JsonObject(new String(body.getBytes()));
				if(hasPhotos(response)){
					photos.addObject(photoFromResponse(response, id));
				}
				placeIds.remove(id);
				if(placeIds.isEmpty()){
					callback.handle(photos);
				}
			});
		});
	}

	private JsonObject photoFromResponse(final JsonObject response, final String id) {
		JsonObject photoJson = response.getObject("response").getObject("photos").getArray("items").get(0);
		photoJson.putString("placeId", id);
		return photoJson;
	}

	private boolean hasPhotos(final JsonObject response) {
		JsonObject photosObject = response.getObject("response").getObject("photos");
		return photosObject != null && photosObject.getNumber("count").intValue() > 0;
	}

	private String endPointUrl(final String placeId) {
		return appendClientDetailsToUrl(END_POINT_TEMPLATE.replace("{venueId}", placeId));
	}
	
}
