package tacs.myretail;

import java.math.BigDecimal;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.reactive.server.WebTestClient.ResponseSpec;

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
public class PutProductTest extends RestClientTest {

	@Test
	public void givenItemIdAndPriceBody_WhenPriceExists_ThenReturnProduct() throws Exception {
		Price currentPrice = null;
		try {
			// Given
			String id = "26396662";

			// When
			BigDecimal value = BigDecimal.valueOf(15.99);
			currentPrice = savePrice(new Price(Integer.valueOf(id), BigDecimal.valueOf(1.00), "USD"));
			ResponseSpec response = getWebTestClient().put()
				.uri("/products/{id}", id)
				.accept(MediaType.APPLICATION_JSON)
				.bodyValue(new Price(Integer.valueOf(id), value, "USD"))
				.exchange();
			// Then
			response.expectStatus().isCreated()
				.expectHeader().valueEquals("Content-Type", "application/json")
				.expectHeader().contentType(MediaType.APPLICATION_JSON)
				.expectBody()
				.json("{\"id\":26396662,\"name\":\"Exploding Kittens Game\",\"current_price\":{\"value\":15.99,\"currency_code\":\"USD\"}}");

		} finally {
			// Cleanup
			if (currentPrice != null) {
				getPriceRepository().delete(currentPrice);
			}
		}
	}
}
