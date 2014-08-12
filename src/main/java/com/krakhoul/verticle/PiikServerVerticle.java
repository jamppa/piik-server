package com.krakhoul.verticle;

import org.vertx.java.core.http.HttpServer;
import org.vertx.java.core.http.RouteMatcher;
import org.vertx.java.platform.Verticle;

import com.krakhoul.api.PlacesApi;

public class PiikServerVerticle extends Verticle {
		
	private HttpServer httpServer;
	private RouteMatcher routeMatcher = new RouteMatcher();
	
	@Override
	public void start() {
		container.deployVerticle("com.krakhoul.verticle.PlacesVerticle");
		container.deployVerticle("com.krakhoul.verticle.PlacesPhotosVerticle");
		httpServer = vertx.createHttpServer();
		httpServer.requestHandler(initPiikApi(httpServer, routeMatcher)).listen(8080);
	}

	private RouteMatcher initPiikApi(HttpServer httpServer, RouteMatcher routeMatcher) {
		new PlacesApi(routeMatcher, vertx).initRoutes();
		return routeMatcher;
	}
	
}
