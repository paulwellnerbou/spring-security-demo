package de.wellnerbou.springsecuritydemo.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@EnableAutoConfiguration(exclude = {
		SecurityAutoConfiguration.class
})
@ActiveProfiles("no-security")
class ExampleControllerNoSecurityHttpTest {

	@LocalServerPort
	int port;
	@Autowired
	private TestRestTemplate testRestTemplate;

	@Test
	void addAndRetrieveData() {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		final String newEntry = "{\"key\":\"key\",\"value\":\"value\"}";
		HttpEntity<String> entity = new HttpEntity<>(newEntry, headers);

		final ResponseEntity<String> exchange = testRestTemplate.exchange(
				"http://localhost:" + this.port + "/api/save",
				HttpMethod.POST,
				entity, String.class
		);

		assertThat(exchange.getStatusCode()).isEqualTo(HttpStatus.CREATED);
		assertThat(exchange.getBody()).isEqualTo(newEntry);

		assertThat(testRestTemplate.getForEntity("/api/list", String.class).getBody()).isEqualTo("[" + newEntry + "]");
	}
}
