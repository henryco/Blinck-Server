package net.henryco.blinckserver.testutils;

import java.net.URI;
import java.net.URISyntaxException;

/**
 * @author Henry on 25/08/17.
 */
public interface HTTPTestUtils {

	String DEFAULT_URI = "http://localhost";

	static URI newURI(String http, String path, int port) throws URISyntaxException {
		return new URI(http + ":" + port + path);
	}
	static URI newURI(String path, int port) throws URISyntaxException {
		return newURI(DEFAULT_URI, path, port);
	}

}