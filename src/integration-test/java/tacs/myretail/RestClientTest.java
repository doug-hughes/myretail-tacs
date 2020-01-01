package tacs.myretail;

import java.math.BigDecimal;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.reactive.server.WebTestClient;

import tacs.myretail.model.Price;
import tacs.myretail.model.PriceRepository;

//@RunWith(SpringRunner.class)
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class RestClientTest {

	@LocalServerPort
	private int port;
	private WebTestClient webClient;
	@Autowired
	PriceRepository priceRepository;

	public WebTestClient getWebClient() {
		return this.webClient;
	}

	public PriceRepository getPriceRepository() {
		return this.priceRepository;
	}

	@Before
	public void setUp() throws Exception {
		this.webClient = WebTestClient.bindToServer().baseUrl("http://localhost:" + this.port).build();

	}

	@After
	public void tearDown() throws Exception {
	}

	/**************************** INPUT VALIDATION *******************************/
	@Test(timeout = 20000)
	public void givenItemIdLargerThanInteger_ThenReturn404() throws Exception {
		// Given
		String id = String.valueOf(Long.MAX_VALUE);

		// Then
		getWebClient().get().uri("/products/{id}", id).exchange().expectStatus().isNotFound().expectBody().isEmpty();
	}

	@Test(timeout = 20000)
	public void givenItemIdSpace_ThenReturn404() throws Exception {
		// Given
		String id = " ";

		// Then
		getWebClient().get().uri("/products/{id}", id).exchange().expectStatus().isNotFound().expectBody().isEmpty();
	}

	@Test(timeout = 20000)
	public void givenItemIdNotInteger_ThenReturn404() throws Exception {
		// Given
		String id = "notinteger";

		// Then
		getWebClient().get().uri("/products/{id}", id).exchange().expectStatus().isNotFound().expectBody().isEmpty();
	}

	@Test(timeout = 20000)
	public void givenItemIdZero_ThenReturn404() throws Exception {
		// Given
		String id = "0";

		// Then
		getWebClient().get().uri("/products/{id}", id).exchange().expectStatus().isNotFound().expectBody().isEmpty();
	}

	// ============================ NOT OUR CONTROLLER
	@Ignore
	@Test(timeout = 20000)
	public void givenItemIdNull_ThenReturn404() throws Exception {
		// Given
		String id = null;

		// Then
		getWebClient().get().uri("/products/{id}", id).exchange().expectStatus().isNotFound().expectHeader()
				.valueEquals("Content-Type", "application/json").expectBody()
				.json("{\"status\":404,\"error\":\"Not Found\",\"message\":\"No message available\",\"path\":\"/products/\"}");
	}

	@Ignore
	@Test(timeout = 20000)
	public void givenItemIdEmpty_ThenReturn404() throws Exception {
		// Given
		String id = "";

		// Then
		getWebClient().get().uri("/products/{id}", id).exchange().expectStatus().isNotFound().expectHeader()
				.valueEquals("Content-Type", "application/json").expectBody()
				.json("{\"status\":404,\"error\":\"Not Found\",\"message\":\"No message available\",\"path\":\"/products/\"}");
	}

	/************************ END INPUT VALIDATION *******************************/

	@Test(timeout = 20000)
	public void givenItemIdNotExistAndPriceNotExist_ThenReturn404() throws Exception {
		// Given
		String id = "15117729";

		// Then
		getWebClient().get().uri("/products/{id}", id).exchange().expectStatus().isNotFound().expectBody().isEmpty();
	}

	@Test(timeout = 20000)
	public void givenItemIdNotExistAndPriceExists_ThenReturn404() throws Exception {
		Price currentPrice = null;
		try {

			// Given
			String id = "15117729";

			// When
			BigDecimal value = BigDecimal.valueOf(12.99);
			currentPrice = getPriceRepository().save(new Price(Integer.valueOf(id), value, "USD"));

			// Then
			getWebClient().get().uri("/products/{id}", id).exchange().expectStatus().isNotFound().expectBody()
					.isEmpty();
			
		} finally {
			// Cleanup
			getPriceRepository().delete(currentPrice);
		}
	}

	@Test(timeout = 20000)
	public void givenItemIdAndPriceExists_ThenReturnSingleProduct() throws Exception {
		Price currentPrice = null;
		try {
		// Given
		String id = "26396662";

		// When
		BigDecimal value = BigDecimal.valueOf(15.99);
		currentPrice = getPriceRepository().save(new Price(Integer.valueOf(id), value, "USD"));

		// Then
		getWebClient().get().uri("/products/{id}", id).exchange().expectStatus().isOk().expectHeader()
				.valueEquals("Content-Type", "application/json").expectBody()
				.json("{\"id\":26396662,\"name\":\"Exploding Kittens Game\",\"current_price\":{\"value\":15.99,\"currency_code\":\"USD\"}}");

		} finally {
			// Cleanup
			getPriceRepository().delete(currentPrice);
		}
	}

	@Test(timeout = 20000)
	public void givenItemIdAndPriceNotExist_ThenReturnSingleProduct() throws Exception {
		// Given
		String id = "13860428";

		// When
		// Then
		getWebClient().get().uri("/products/{id}", id).exchange().expectStatus().isOk().expectHeader()
				.valueEquals("Content-Type", "application/json").expectBody()
				.json("{\"id\":13860428,\"name\":\"The Big Lebowski (Blu-ray)\"}");
	}
}
