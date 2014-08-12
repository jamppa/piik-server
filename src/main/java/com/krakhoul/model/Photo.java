package com.krakhoul.model;

import org.vertx.java.core.json.JsonObject;

public class Photo {

	private final JsonObject json;
	
	public Photo(JsonObject json) {
		this.json = json;
		this.json.putString("fullUrl", fullUrl(json));
	}
	
	private String fullUrl(JsonObject json) {
		return prefix() + size() + suffix();
	}

	private String suffix() {
		return this.json.getString("suffix");
	}

	private String size() {
		return this.json.getInteger("width") + "x" + this.json.getInteger("height");
	}

	private String prefix() {
		return this.json.getString("prefix");
	}

	public JsonObject asJson() {
		return json;
	}
	
	public String placeId() {
		return json.getString("placeId");
	}
}
