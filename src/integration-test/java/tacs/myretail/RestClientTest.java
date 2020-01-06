package tacs.myretail;

import java.math.BigDecimal;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.util.ClassUtils;

import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import tacs.myretail.RestClientTest.Config;
import tacs.myretail.model.Price;
import tacs.myretail.model.PriceRepository;

//@RunWith(SpringRunner.class)
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ContextConfiguration(classes = { /*Config.class, */AppConfig.class, MongoConfig.class, Application.class })
public class RestClientTest {
	private static int mockPort;
	@LocalServerPort
	private int port;
	private WebTestClient webTestClient;
	@Autowired
	PriceRepository priceRepository;

	public WebTestClient getWebTestClient() {
		return this.webTestClient;
	}

	public PriceRepository getPriceRepository() {
		return this.priceRepository;
	}

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		// do this to get a port
		try (MockWebServer server = new MockWebServer()) {
			RestClientTest.mockPort = server.getPort();
			server.shutdown();
		}
	}

	@Before
	public void setUp() throws Exception {
		this.webTestClient = WebTestClient.bindToServer().baseUrl("http://localhost:" + this.port).build();

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

	// ============================ NOT OUR CONTROLLER
	@Ignore
	@Test(timeout = 20000)
	public void givenItemIdNull_ThenReturn404() throws Exception {
		// Given
		String id = null;

		// Then
		getWebTestClient().get().uri("/products/{id}", id).exchange().expectStatus().isNotFound().expectHeader()
				.valueEquals("Content-Type", "application/json").expectBody()
				.json("{\"status\":404,\"error\":\"Not Found\",\"message\":\"No message available\",\"path\":\"/products/\"}");
	}

	@Ignore
	@Test(timeout = 20000)
	public void givenItemIdEmpty_ThenReturn404() throws Exception {
		// Given
		String id = "";

		// Then
		getWebTestClient().get().uri("/products/{id}", id).exchange().expectStatus().isNotFound().expectHeader()
				.valueEquals("Content-Type", "application/json").expectBody()
				.json("{\"status\":404,\"error\":\"Not Found\",\"message\":\"No message available\",\"path\":\"/products/\"}");
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
		
		Path path = Paths.get(url.toURI());
		String jsonBody = Files.readString(path);
		/* return response in item lookup */
		@SuppressWarnings("resource")
		MockWebServer server = new MockWebServer();
		server.enqueue(jsonResponse(200).setBody(jsonBody));
		server.start(RestClientTest.mockPort);

		// Then
		getWebTestClient().get().uri("/products/{id}", id).exchange().expectStatus().isOk().expectHeader()
				.valueEquals("Content-Type", "application/json").expectBody()
				.json("{\"id\":13860428,\"name\":\"The Big Lebowski (Blu-ray)\"}");

		// cleanup
		server.shutdown();
	}

	private static MockResponse jsonResponse(int code) {
		return new MockResponse().setResponseCode(code).setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON);
	}

	private Price savePrice(Price price) {
		Price savedPrice = null;
		try {
			savedPrice = getPriceRepository().save(price);
		} catch (DuplicateKeyException dke) {
			/* ignore dups */}
		return savedPrice;
	}

	/**
	 * 
	 * @return <ClassName>-<MethodName> for finding test resources
	 */
	private String getClassAndMethodName() {
		StringBuilder sb = new StringBuilder(ClassUtils.getShortName(getClass()));
		sb.append('-').append(new Throwable().getStackTrace()[1].getMethodName());
		return sb.toString();
	}

	@Configuration
	static class Config {
		@Bean
		public String mockBaseUri() {
			return String.format("http://localhost:%d/", RestClientTest.mockPort);
		}
	}
}
