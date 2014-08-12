package com.krakhoul.fs;

import org.vertx.java.core.Vertx;
import org.vertx.java.core.http.HttpClient;

public interface FourSquareApi {
	
	default String base() {
		return "api.foursquare.com";
	}
	
	default String clientId() {
		return "TL4BVSAGMYCK0CBJUSSPAU2T21Y1YA3ATWCJ24QSBASPOCL2";
	}
	
	default String clientSecret() {
		return "WLWWC4XEHT43BBADDWDSLBAMHZAIFCMP15XLXYV0ABHH1NQS";
	}
	
	default String version() {
		return "20140624";
	}
	
	default HttpClient client(final Vertx vertx) {
		return vertx.createHttpClient()
				.setSSL(true)
				.setTrustAll(true)
				.setPort(443)
				.setHost(base())
				.setMaxPoolSize(15);
	}
	
	default String appendClientDetailsToUrl(final String url) {
		return url.concat("&client_id=")
				.concat(clientId())
				.concat("&client_secret=")
				.concat(clientSecret())
				.concat("&v=").concat(version());
	}
	
}
