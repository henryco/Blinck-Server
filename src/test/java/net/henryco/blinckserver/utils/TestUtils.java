package net.henryco.blinckserver.utils;

import org.springframework.http.HttpHeaders;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Random;

/**
 * @author Henry on 25/08/17.
 */
public interface TestUtils {

	String DEFAULT_URI = "http://localhost";

	static URI newURI(String host, String endpoint, int port) {
		try {
			return new URI(host + ":" + port + endpoint);
		} catch (URISyntaxException e) {
			e.printStackTrace();
			throw new RuntimeException(e.getMessage());
		}
	}

	static URI newURI(String endpoint, int port) {
		return newURI(DEFAULT_URI, endpoint, port);
	}


	static HttpHeaders newJsonTypeHeader() {
		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.add("Content-Type","application/json");
		return httpHeaders;
	}

	static String randomGaussNumberString(long bound) {
		return Long.toString(Math.abs((long) (new Random().nextGaussian() * bound)));
	}

	static String randomGaussNumberString() {
		return randomGaussNumberString(1_000_000_000L);
	}

	static String randomNumberString(int bound) {
		return Integer.toString(new Random().nextInt(bound));
	}

	static String randomNumberString(int boundMin, int boundMax) {
		return Integer.toString(Math.max(boundMin, new Random().nextInt(boundMax)));
	}
}