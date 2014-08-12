package com.krakhoul.fs;

import org.vertx.java.core.Handler;
import org.vertx.java.core.Vertx;
import org.vertx.java.core.buffer.Buffer;
import org.vertx.java.core.http.HttpClient;
import org.vertx.java.core.http.HttpClientRequest;
import org.vertx.java.core.json.JsonArray;
import org.vertx.java.core.json.JsonObject;

public class SearchVenuesApi implements FourSquareApi {

	private static final String END_POINT_TEMPLATE = 
			"/v2/venues/explore?ll={lat},{lon}&limit=25&section=nightlife&sortByDistance=1&client_id={clientId}&client_secret={clientSecret}&v=20140603";
	
	private final HttpClient httpClient;
	
	public SearchVenuesApi(Vertx vertx) {
		this.httpClient = client(vertx);
	}
	
	public void searchVenuesNear(final JsonObject latlon, Handler<JsonArray> callback) {
		HttpClientRequest request = httpClient.get(endPoint(latlon), (resp) -> {
			final Buffer body = new Buffer(0);
			resp.dataHandler((data) -> {
				body.appendBuffer(data);
			});
			resp.endHandler((Void) -> {
				JsonObject response = new JsonObject(new String(body.getBytes()));
				callback.handle(placesFromResponse(response));
			});
		});
		request.end();
	}

	private JsonArray placesFromResponse(final JsonObject response) {
		JsonArray venues = new JsonArray();
		JsonArray groups = response.getObject("response").getArray("groups");
		if(groups != null){
			JsonObject recommendation = groups.get(0);
			JsonArray items = recommendation.getArray("items");
			for(int i = 0; i < items.size(); i++){
				JsonObject eachItem = items.get(i);
				venues.addObject(eachItem.getObject("venue"));
			}
		}
		return venues;
	}

	private String endPoint(final JsonObject latlon) {
		return END_POINT_TEMPLATE
				.replace("{lat}", latlon.getString("lat"))
				.replace("{lon}", latlon.getString("lon"))
				.replace("{clientId}", clientId())
				.replace("{clientSecret}", clientSecret());
	}
	
}
