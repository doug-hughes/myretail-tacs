package tacs.myretail;

import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class RestClientTest {

	@LocalServerPort
	private int port;

	private WebTestClient webClient;

	private WebTestClient getWebClient() {
		return this.webClient;
	}

	@Before
	public void setUp() throws Exception {
		this.webClient = WebTestClient.bindToServer().baseUrl("http://localhost:" + this.port).build();
		// add some prices from remote
//		getWebClient().post().uri("/dev/actions/populate?query={keyword}", "kittens").exchange().expectStatus().isOk();
	}

	@Test
	public void givenItemIdAndPriceExists_ThenReturnSingleProduct() {
		// Given
		String id = "26396662";
		getWebClient().get().uri("/products/{id}", id).exchange().expectStatus().isOk().expectHeader()
				.valueEquals("Content-Type", "application/json").expectBody()
				.json("{\"id\":26396662,\"name\":\"Exploding Kittens Game\",\"current_price\":{\"value\":15.99,\"currency_code\":\"USD\"}}");
	}

	@Test
	public void givenItemIdAndPriceDoesntExist_ThenReturnSingleProduct() {
		// Given
		// When
		String id = "13860428";

		// Then
		getWebClient().get().uri("/products/{id}", id).exchange().expectStatus().isOk().expectHeader()
				.valueEquals("Content-Type", "application/json").expectBody()
				.json("{\"id\":13860428,\"name\":\"The Big Lebowski (Blu-ray)\"}");
	}
}
