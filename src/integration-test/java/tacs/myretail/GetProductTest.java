package tacs.myretail;

import java.math.BigDecimal;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import tacs.myretail.model.Price;

/**
 * Test class that uses the MongoDb for the pricing information and
 * uses the actual external web service for validating successful integration
 * If either of these services are down or have changed, the test may fail.
 */
//@RunWith(SpringRunner.class)
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ContextConfiguration(classes = { /*Config.class, */AppConfig.class, MongoConfig.class, Application.class })
public class GetProductTest extends RestClientTest {

	/**************************** INPUT VALIDATION *******************************/
	@Test(timeout = 20000)
	public void givenItemIdLargerThanInteger_ThenReturn404() throws Exception {
		// Given
		String id = String.valueOf(Long.MAX_VALUE);

		// Then
		getWebTestClient().get().uri("/products/{id}", id).exchange().expectStatus().isNotFound().expectBody().isEmpty();
	}

	@Test(timeout = 20000)
	public void givenItemIdSpace_ThenReturn404() throws Exception {
		// Given
		String id = " ";

		// Then
		getWebTestClient().get().uri("/products/{id}", id).exchange().expectStatus().isNotFound().expectBody().isEmpty();
	}

	@Test(timeout = 20000)
	public void givenItemIdNotInteger_ThenReturn404() throws Exception {
		// Given
		String id = "notinteger";

		// Then
		getWebTestClient().get().uri("/products/{id}", id).exchange().expectStatus().isNotFound().expectBody().isEmpty();
	}

	@Test(timeout = 20000)
	public void givenItemIdZero_ThenReturn404() throws Exception {
		// Given
		String id = "0";

		// Then
		getWebTestClient().get().uri("/products/{id}", id).exchange().expectStatus().isNotFound().expectBody().isEmpty();
	}

	/************************ END INPUT VALIDATION *******************************/

	@Test(timeout = 20000)
	public void givenItemIdNotExistAndPriceNotExist_ThenReturn404() throws Exception {
		// Given
		String id = "15117729";

		// Then
		getWebTestClient().get().uri("/products/{id}", id).exchange().expectStatus().isNotFound().expectBody().isEmpty();
	}

	@Test(timeout = 20000)
	public void givenItemIdNotExistAndPriceExists_ThenReturn404() throws Exception {
		Price currentPrice = null;
		try {

			// Given
			String id = "15117729";

			// When
			BigDecimal value = BigDecimal.valueOf(12.99);
			currentPrice = savePrice(new Price(Integer.valueOf(id), value, "USD"));

			// Then
			getWebTestClient().get().uri("/products/{id}", id).exchange().expectStatus().isNotFound().expectBody()
					.isEmpty();

		} finally {
			// Cleanup
			if (currentPrice != null) {
				getPriceRepository().delete(currentPrice);
			}
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
			currentPrice = savePrice(new Price(Integer.valueOf(id), value, "USD"));

			// Then
			getWebTestClient().get().uri("/products/{id}", id).exchange().expectStatus().isOk().expectHeader()
					.valueEquals("Content-Type", "application/json").expectBody()
					.json("{\"id\":26396662,\"name\":\"Exploding Kittens Game\",\"current_price\":{\"value\":15.99,\"currency_code\":\"USD\"}}");

		} finally {
			// Cleanup
			if (currentPrice != null) {
				getPriceRepository().delete(currentPrice);
			}
		}
	}

	@Test()
	public void givenItemIdAndPriceNotExist_ThenReturnSingleProduct() throws Exception {
		// Given
		String id = "13860428";

		// When
		/* Get the response for the item lookup from file */
		String resourceFileName = getClassAndMethodName() + ".json";
		URL url = getClass().getClassLoader().getResource(resourceFileName);
		assert(url != null) : "unable to find file " + resourceFileName + " on the classpath";
		
		Path path = Paths.get(url.toURI());
		String jsonBody = Files.readString(path);
		/* return response in item lookup */
		@SuppressWarnings("resource")
		MockWebServer server = new MockWebServer();
		server.enqueue(jsonResponse(200).setBody(jsonBody));
		server.start(super.getMockPort());

		// Then
		getWebTestClient().get().uri("/products/{id}", id).exchange().expectStatus().isOk().expectHeader()
				.valueEquals("Content-Type", "application/json").expectBody()
				.json("{\"id\":13860428,\"name\":\"The Big Lebowski (Blu-ray)\"}");

		// cleanup
		server.shutdown();
	}
///////////////////////////////////////////////////////////////// START UTILITY METHODS
	private static MockResponse jsonResponse(int code) {
		return new MockResponse().setResponseCode(code).setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON);
	}

}
