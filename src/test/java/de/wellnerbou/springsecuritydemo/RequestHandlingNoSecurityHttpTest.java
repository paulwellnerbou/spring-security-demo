package de.wellnerbou.springsecuritydemo;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("no-security")
class RequestHandlingNoSecurityHttpTest {

	@LocalServerPort
	int port;
	@Autowired
	private TestRestTemplate testRestTemplate;

	@Test
	void getRequestToExistingAPIShouldReturnOk() {
		assertThat(testRestTemplate.getForEntity("/api/list", String.class).getStatusCode()).isEqualTo(HttpStatus.OK);
	}

	@Test
	void getRequestToNotExistingAPIShouldReturnNotFound() {
		assertThat(testRestTemplate.getForEntity("/api/no-list", String.class).getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
	}
}
