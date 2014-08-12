package com.krakhoul.model;

import org.vertx.java.core.json.JsonObject;

public class Place {

	private final JsonObject json;
	
	public Place(JsonObject json) {
		this.json = new JsonObject();
		this.json.putString("id", json.getString("id"));
		this.json.putString("name", json.getString("name"));
		this.json.putObject("location", json.getObject("location"));
		this.json.putNumber("hereNow", json.getObject("hereNow").getNumber("count"));
		this.json.putNumber("photosCount", json.getObject("photos").getNumber("count"));
		this.json.putString("hotness", hotness(json));
	}
	
	private String hotness(JsonObject json) {
		int hereNow = json.getObject("hereNow").getNumber("count").intValue();
		if(hereNow >= 5){
			return "hot";
		}
		if(hereNow >= 2 && hereNow <= 4){
			return "semi";
		}
		return "not";
	}

	public String id() {
		return json.getString("id");
	}
	
	public JsonObject asJson() {
		return json;
	}

	public void addPhoto(final Photo photo) {
		json.putObject("photo", photo.asJson());
	}

	public boolean hasPhoto() {
		return json.getObject("photo") != null;
	}
}
