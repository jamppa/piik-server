package com.krakhoul.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.vertx.java.core.json.JsonArray;

public class PhotosList {

	private final List<Photo> photosList = new ArrayList<Photo>();
	
	public PhotosList(JsonArray jsonArray) {
		for(int i = 0; i < jsonArray.size(); i++){
			photosList.add(new Photo(jsonArray.get(i)));
		}
	}
	
	public Optional<Photo> photoOfPlace(final Place place) {
		return photosList.stream()
				.filter(photo -> photo.placeId().equals(place.id()))
				.findFirst();
	}
}
