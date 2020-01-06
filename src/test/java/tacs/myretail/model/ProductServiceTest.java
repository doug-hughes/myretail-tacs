package tacs.myretail.model;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import tacs.myretail.AppConfig;
import tacs.myretail.model.ProductServiceTest.Config;

@RunWith(SpringRunner.class)
@SpringBootTest()
@ContextConfiguration(classes = { Config.class, AppConfig.class, ProductService.class })
public class ProductServiceTest {
	private static MockWebServer server;
	private static int mockPort;
	@MockBean
	private PriceRepository priceRepository;
	@Autowired
	private ProductService service;

	private ProductService getService() {
		return this.service;
	}

	private PriceRepository getRepository() {
		return this.priceRepository;
	}

	private static MockWebServer getServer() {
		return ProductServiceTest.server;
	}

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		// do this to get a port
		ProductServiceTest.server = new MockWebServer();
		ProductServiceTest.mockPort = server.getPort();
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		getServer().shutdown();
	}

	@Before
	public void setUp() throws Exception {
		this.priceRepository = mock(PriceRepository.class);
	}

	@After
	public void tearDown() throws Exception {
	}

	/**************************** INPUT VALIDATION *******************************/
	@Test(timeout = 20000)
	public void givenItemIdLargerThanInteger_WhenFindPriceByTCIN_ThenEmpty() throws Exception {
		// Given
		String id = String.valueOf(Long.MAX_VALUE);

		// When
		ThrowingCallable tc = new ThrowingCallable() {
			@Override
			public void call() throws Throwable {
				getService().findPriceByTCIN(id);
			}
		};

		// Then
		assertThatExceptionOfType(NumberFormatException.class).isThrownBy(tc);
	}

	@Test(timeout = 20000)
	public void givenItemIdLargerThanInteger_WhenFindItemByTCIN_ThenEmpty() throws Exception {
		// Given
		String id = String.valueOf(Long.MAX_VALUE);
		getServer().enqueue(jsonResponse(404).setBody("{\"product\":{\"item\":{}}}"));

		// When
		ThrowingCallable tc = new ThrowingCallable() {
			@Override
			public void call() throws Throwable {
				getService().findItemByTCIN(id);
			}
		};

		// Then
		assertThatExceptionOfType(NoSuchElementException.class).isThrownBy(tc);
	}

	@Ignore
	@Test(timeout = 20000)
	public void givenItemIdSpace_ThenReturn404() throws Exception {
		// Given
		String id = " ";

		// Then
//		getWebTestClient().get().uri("/products/{id}", id).exchange().expectStatus().isNotFound().expectBody().isEmpty();
	}

	@Ignore
	@Test(timeout = 20000)
	public void givenItemIdNotInteger_ThenReturn404() throws Exception {
		// Given
		String id = "notinteger";

		// Then
//		getWebTestClient().get().uri("/products/{id}", id).exchange().expectStatus().isNotFound().expectBody().isEmpty();
	}

	@Ignore
	@Test(timeout = 20000)
	public void givenItemIdZero_ThenReturn404() throws Exception {
		// Given
		String id = "0";

		// Then
//		getWebTestClient().get().uri("/products/{id}", id).exchange().expectStatus().isNotFound().expectBody().isEmpty();
	}

	// ============================ NOT OUR CONTROLLER
	@Ignore
	@Test(timeout = 20000)
	public void givenItemIdNull_ThenReturn404() throws Exception {
		// Given
		String id = null;

		// Then
//		getWebTestClient().get().uri("/products/{id}", id).exchange().expectStatus().isNotFound().expectHeader()
//				.valueEquals("Content-Type", "application/json").expectBody()
//				.json("{\"status\":404,\"error\":\"Not Found\",\"message\":\"No message available\",\"path\":\"/products/\"}");
	}

	@Ignore
	@Test(timeout = 20000)
	public void givenItemIdEmpty_ThenReturn404() throws Exception {
		// Given
		String id = "";

		// Then
//		getWebTestClient().get().uri("/products/{id}", id).exchange().expectStatus().isNotFound().expectHeader()
//				.valueEquals("Content-Type", "application/json").expectBody()
//				.json("{\"status\":404,\"error\":\"Not Found\",\"message\":\"No message available\",\"path\":\"/products/\"}");
	}

	/************************ END INPUT VALIDATION *******************************/

	@Ignore
	@Test(timeout = 20000)
	public void givenItemIdNotExistAndPriceNotExist_ThenReturn404() throws Exception {
		// Given
		String id = "15117729";

		// Then
//		getWebTestClient().get().uri("/products/{id}", id).exchange().expectStatus().isNotFound().expectBody().isEmpty();
	}

	@Ignore
	@Test(timeout = 20000)
	public void givenItemIdNotExistAndPriceExists_WhenFindByTCIN_ThenEmpty() throws Exception {
		Price currentPrice = null;
		try {

			// Given
			String id = "15117729";

			// When
			BigDecimal value = BigDecimal.valueOf(12.99);
//			currentPrice = savePrice(new Price(Integer.valueOf(id), value, "USD"));

			// Then
//			getWebTestClient().get().uri("/products/{id}", id).exchange().expectStatus().isNotFound().expectBody()
//					.isEmpty();

		} finally {
			// Cleanup
			if (currentPrice != null) {
//				getPriceRepository().delete(currentPrice);
			}
		}
	}

	@Ignore
	@Test(timeout = 20000)
	public void givenItemIdAndPriceExists_WhenFindByTCIN_ThenReturnSingleProductWithPrice() throws Exception {
		Price currentPrice = null;
		try {
			// Given
			String id = "26396662";

			// When
			BigDecimal value = BigDecimal.valueOf(15.99);
//			currentPrice = savePrice(new Price(Integer.valueOf(id), value, "USD"));

			// Then
//			getWebTestClient().get().uri("/products/{id}", id).exchange().expectStatus().isOk().expectHeader()
//					.valueEquals("Content-Type", "application/json").expectBody()
//					.json("{\"id\":26396662,\"name\":\"Exploding Kittens Game\",\"current_price\":{\"value\":15.99,\"currency_code\":\"USD\"}}");

		} finally {
			// Cleanup
			if (currentPrice != null) {
//				getPriceRepository().delete(currentPrice);
			}
		}
	}

	@Ignore
	@Test(timeout = 20000)
	public void givenItemPriceExists_WhenFindPriceByTCIN_ThenReturnPrice() throws Exception {
	}

	@Ignore
	@Test(timeout = 20000)
	public void givenItemIdExists_WhenFindItemByTCIN_ThenReturnItem() throws Exception {
	}

	@Ignore
	@Test(timeout = 20000)
	public void givenItemPriceMissing_WhenFindPriceByTCIN_ThenReturnEmpty() throws Exception {
	}

	@Ignore
	@Test(timeout = 20000)
	public void givenItemIdNotFound_WhenFindItemByTCIN_ThenEmpty() throws Exception {
	}

	@Test()
	public void givenItemIdAndPriceNotExist_WhenFindByTCIN_ThenReturnSingleProductWithoutPrice() throws Exception {
		// Given
		when(getRepository().findByTcin(Mockito.anyInt())).thenReturn(Optional.empty());

		String id = "13860428";

		// When
		/* Get the response for the item lookup from file */
		String resourceFileName = "RestClientTest-givenItemIdAndPriceNotExist_ThenReturnSingleProduct.json";
		URL url = getClass().getClassLoader().getResource(resourceFileName);
		Path path = Paths.get(url.toURI());
		String jsonBody = Files.readString(path);
		/* return response in item lookup */
		getServer().enqueue(jsonResponse(200).setBody(jsonBody));

		// Then
		Product product = getService().findByTcin(id);
		
		assertTrue(product.getCurrent_price().isEmpty());
		assertEquals("The Big Lebowski (Blu-ray)", product.getName());
		assertEquals(13860428L, product.getId());
		
//		getWebTestClient().get().uri("/products/{id}", id).exchange().expectStatus().isOk().expectHeader()
//				.valueEquals("Content-Type", "application/json").expectBody()
//				.json("{\"id\":13860428,\"name\":\"The Big Lebowski (Blu-ray)\"}");

	}

	private static MockResponse jsonResponse(int code) {
		return new MockResponse().setResponseCode(code).setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON);
	}

	@Configuration
	static class Config {
		@Bean
		public String mockBaseUri() {
			return String.format("http://localhost:%d/", ProductServiceTest.mockPort);
		}
	}

}
