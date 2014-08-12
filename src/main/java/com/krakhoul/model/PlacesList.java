package com.krakhoul.model;

import static java.util.stream.Collectors.toList;

import java.util.ArrayList;
import java.util.List;

import org.vertx.java.core.json.JsonArray;

public class PlacesList {
	
	public final List<Place> placesList = new ArrayList<Place>();
	
	public PlacesList(JsonArray jsonArray) {
		for(int i = 0; i < jsonArray.size(); i++){
			placesList.add(new Place(jsonArray.get(i)));
		}
	}
	
	public PlacesList(List<Place> places) {
		this.placesList.addAll(places);
	}
	
	public PlacesList withPhoto() {
		return new PlacesList(
				placesList.stream().filter(place -> place.hasPhoto()).collect(toList()));
	}
	
	public JsonArray asJson() {
		JsonArray json = new JsonArray();
		placesList.forEach(place -> {
			json.addObject(place.asJson());
		});
		return json;
	}
	
	public JsonArray placeIdsAsJson() {
		JsonArray json = new JsonArray();
		placesList.forEach(place -> json.addString(place.id()));
		return json;
	}

	public PlacesList addPhotos(final PhotosList photosList) {
		placesList.forEach(place -> {
			if(photosList.photoOfPlace(place).isPresent()){
				place.addPhoto(photosList.photoOfPlace(place).get());
			}
		});
		return this;
	}
	
}
