package net.henryco.blinckserver.testutils;

import org.springframework.http.HttpHeaders;

import java.net.URI;
import java.net.URISyntaxException;

/**
 * @author Henry on 25/08/17.
 */
public interface HTTPTestUtils {

	String DEFAULT_URI = "http://localhost";

	static URI newURI(String http, String path, int port) {
		try {
			return new URI(http + ":" + port + path);
		} catch (URISyntaxException e) {
			e.printStackTrace();
			return null;
		}
	}

	static URI newURI(String path, int port) {
		return newURI(DEFAULT_URI, path, port);
	}


	static HttpHeaders jsonTypeHeader() {
		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.add("Content-Type","application/json");
		return httpHeaders;
	}

}