package de.wellnerbou.springsecuritydemo;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import java.util.Collections;

import static org.assertj.core.api.BDDAssertions.then;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class SpringSecurityHttpTest {

	@LocalServerPort
	int port;
	@Autowired
	private TestRestTemplate rest;

	private static HttpHeaders acceptHeaders(final MediaType applicationJson) {
		HttpHeaders headers = new HttpHeaders();
		headers.setAccept(Collections.singletonList(applicationJson));
		return headers;
	}

	private static HttpEntity<String> requestEntityWithAcceptHeader(final MediaType mediaType) {
		return new HttpEntity<>(null, acceptHeaders(mediaType));
	}

	@Test
	void faviconShouldBeReachableWithoutAuthentication() {
		HttpEntity<String> entity = requestEntityWithAcceptHeader(MediaType.APPLICATION_OCTET_STREAM);
		ResponseEntity<byte[]> response = rest.exchange(
				"http://localhost:" + this.port + "/favicon.ico",
				HttpMethod.GET,
				entity, byte[].class
		);

		then(response.getStatusCode()).isEqualTo(HttpStatus.OK);
	}

	@Test
	void requestToIndex_withoutAuthentication_notAcceptingHTML_shouldReturnUnauthorized() {
		HttpEntity<String> entity = requestEntityWithAcceptHeader(MediaType.APPLICATION_JSON);

		ResponseEntity<String> response = rest.exchange(
				"http://localhost:" + this.port + "/",
				HttpMethod.GET,
				entity, String.class
		);

		then(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
	}

	/**
	 * Checks that a request accepting HTML to a secured URL is redirecting to the built-in default
	 * login page Spring is providing.
	 * Actually the applications sends a 302 Found, redirecting to /login, which is followed automatically by
	 * TestRestTemplate.
	 */
	@Test
	void requestToIndex_withoutAuthentication_acceptingHTML_shouldRedirectToLoginForm() {
		HttpEntity<String> entity = requestEntityWithAcceptHeader(MediaType.TEXT_HTML);

		ResponseEntity<String> response = rest.exchange(
				"http://localhost:" + this.port + "/",
				HttpMethod.GET,
				entity, String.class
		);

		then(response.getStatusCode()).isEqualTo(HttpStatus.OK);
		then(response.getBody()).startsWith("<!DOCTYPE html>").contains("action=\"/login\"");
	}

	@Test
	void requestToIndex_withAuthentication_acceptingHTML_shouldRedirectToLoginForm() {
		HttpEntity<String> entity = requestEntityWithAcceptHeader(MediaType.TEXT_HTML);

		ResponseEntity<String> response = rest.withBasicAuth("user", "password").exchange(
				"http://localhost:" + this.port + "/",
				HttpMethod.GET,
				entity, String.class
		);

		then(response.getStatusCode()).isEqualTo(HttpStatus.OK);
		then(response.getBody()).startsWith("<!DOCTYPE html>").contains("Hello World");
	}

	@Test
	void requestToLogout_withoutAuthentication_expectsLogoutPageToBeThere() {
		HttpEntity<String> entity = requestEntityWithAcceptHeader(MediaType.TEXT_HTML);

		ResponseEntity<String> response = rest.exchange(
				"http://localhost:" + this.port + "/logout",
				HttpMethod.GET,
				entity, String.class
		);

		then(response.getStatusCode()).isEqualTo(HttpStatus.OK);
		then(response.getBody()).startsWith("<!DOCTYPE html>").contains("You have been signed out");
	}

	@Test
	void postToAPI_withoutAuthentication_shouldReturnUnauthorized() {
		HttpHeaders headers = acceptHeaders(MediaType.APPLICATION_JSON);
		HttpEntity<String> entity = new HttpEntity<>("{\"key\": 0}", headers);

		ResponseEntity<String> response = this.rest.exchange(
				"http://localhost:" + this.port + "/api/list",
				HttpMethod.POST,
				entity, String.class
		);

		then(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
	}

	@Test
	void getFromAPI_withAuthentication_shouldBeAllowedAndReturnOk() {
		HttpHeaders headers = acceptHeaders(MediaType.APPLICATION_JSON);
		HttpEntity<String> entity = new HttpEntity<>(null, headers);

		ResponseEntity<String> response = rest.withBasicAuth("user", "password").exchange(
				"http://localhost:" + this.port + "/api/list",
				HttpMethod.GET,
				entity, String.class
		);
		then(response.getStatusCode()).isEqualTo(HttpStatus.OK);
	}

	@Test
	void postToAPI__withAuthentication_shouldBeAllowedAndReturnCreated() {
		HttpHeaders headers = acceptHeaders(MediaType.APPLICATION_JSON);
		headers.setContentType(MediaType.APPLICATION_JSON);
		HttpEntity<String> entity = new HttpEntity<>("{\"key\":\"key\",\"value\":\"value\"}", headers);

		ResponseEntity<String> response = rest.withBasicAuth("user", "password").exchange(
				"http://localhost:" + this.port + "/api/save",
				HttpMethod.POST,
				entity, String.class
		);
		then(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
	}
}
