package com.hua.demo;

import com.hua.demo.movie.MovieRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.Map;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;  // ΣΩΣΤΗ import
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;  // ΣΩΣΤΗ import
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)   // <-- no security filters
@ActiveProfiles("test")                     // <-- H2
class DemoApplicationTests {

	@Autowired
	MockMvc mockMvc;

	@Autowired
	MovieRepository repo;

	@Test
	void contextLoads() {}

	@Test
	void createMovie_works_without_auth() throws Exception {
		mockMvc.perform(post("/api/movies/save")
						.contentType(MediaType.APPLICATION_JSON)
						.content("""
          {"title":"testmovie","duration":120,"director":"X","genre":"Y","poster":"/p.png","description":"d"}
        """))
				.andExpect(status().isCreated());

		assertThat(repo.findById("testmovie")).isPresent();
	}

	@Test
	void getAllMovies_asUser_returns200() throws Exception {
		mockMvc.perform(get("/api/movies")
						.with(jwt().jwt(j -> j.claim("resource_access",
								Map.of("cm", Map.of("roles", List.of("USER")))))) // -> ROLE_USER
						.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
	}
}
