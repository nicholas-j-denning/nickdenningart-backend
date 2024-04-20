package com.nickdenningart.fractal;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

import com.nickdenningart.fractal.model.Fractal;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@ActiveProfiles("dev")
class ApplicationTests {

	@LocalServerPort
	private int port;

	@Autowired
	private TestRestTemplate restTemplate;

	@Value("${x-api-key}")
	private String apiKey;

	private String url(){
		return "http://localhost:"+port;
	}

	@Test
	void contextLoads() {
	}

	@Test 
	void getFractalwithBadIdHasStatusNotFound(){
		String id = UUID.randomUUID().toString();
		ResponseEntity<Fractal> response = this.restTemplate.getForEntity(url()+"/fracta/"+id, Fractal.class);
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
	}

	@Test 
	void createUploadImageDeleteWorks(){
		// make fractal to upload
		String id = UUID.randomUUID().toString();
		String title = UUID.randomUUID().toString();
		Fractal fractal = Fractal.builder()
			.id(id)
			.title(title)
			.tags(List.of())
			.build();

		//set auth headers
		HttpHeaders headers = new HttpHeaders();
		headers.add("x-api-key", apiKey);

		// test post
		HttpEntity<Fractal> postEntity = new HttpEntity<Fractal>(fractal, headers);
		ResponseEntity<Fractal> postResponse = this.restTemplate.exchange(url()+"/fractal", HttpMethod.POST, postEntity, Fractal.class);
		assertThat(postResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
		String postId = postResponse.getBody().getId();
		assertThat(postId).isNotEqualTo(id);

		// test get
		ResponseEntity<Fractal> getResponse = this.restTemplate.getForEntity(url()+"/fractal/"+postId, Fractal.class);
		assertThat(getResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
		Fractal getFractal = getResponse.getBody();
		assertThat(getFractal.getTitle()).isEqualTo(title);
		assertThat(getFractal.getId()).isEqualTo(postId);

		// test delete
		HttpEntity<?> deleteEntity = new HttpEntity<>(headers);
		this.restTemplate.delete(url()+"/fractal/"+postId);
		ResponseEntity<Void> deleteResponse = this.restTemplate.exchange(url()+"/fractal/"+postId, HttpMethod.DELETE, deleteEntity, Void.class);
		assertThat(deleteResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
	}
}
