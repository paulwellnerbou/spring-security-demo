package de.wellnerbou.springsecuritydemo.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = ExampleController.class, excludeAutoConfiguration = SecurityAutoConfiguration.class)
class ExampleControllerMockMvcTest {

	@Autowired
	private MockMvc mockMvc;

	@Test
	void getServiceByIdTest() throws Exception {
		final String payload = "{\"key\":\"key\",\"value\":\"value\"}";
		mockMvc.perform(post("/api/save")
						.contentType(MediaType.APPLICATION_JSON)
						.content(payload)
				)
				.andDo(result -> System.out.println(result.getResponse().getContentAsString()))
				.andExpect(status().isCreated())
				.andExpect(content().contentTypeCompatibleWith(MediaType.ALL))
				.andExpect(content().string(payload));

		mockMvc.perform(get("/api/list")).andExpect(status().isOk()).andExpect(content().string("[" + payload + "]"));
	}
}
