package tacs.myretail.model;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;
import tacs.myretail.AppConfig;
import tacs.myretail.model.ProductServiceTest.Config;
import tacs.myretail.model.rest.ItemResponse;

@RunWith(SpringRunner.class)
@SpringBootTest()
@ContextConfiguration(classes = { Config.class, AppConfig.class, ProductService.class })
public class ProductServiceTest {
	private static Logger log = LogManager.getLogger();
	private static MockWebServer server;
	private static int mockPort;
	@MockBean
	private PriceRepository priceRepository;
	@Autowired() // use @InjectMocks to include mocking productWebClient
	private ProductService service;
	private ObjectMapper objectMapper;

	private ProductService getService() {
		return this.service;
	}

	private void printJson(Object o) throws JsonProcessingException {
		log.debug(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(o),
				new Exception("json for " + o.getClass().toString()));
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
		// Without this method mockbean priceRepository is not available in
		// ProductService
		MockitoAnnotations.initMocks(this);
		objectMapper = new ObjectMapper();
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
		enqueueProductNotFound(getServer());

		// When
		Mono<ItemResponse> itemResponse = getService().findItemByTCIN(id);

		// Then
		StepVerifier.create(itemResponse).expectErrorSatisfies(throwable -> {
			assertThat(throwable instanceof WebClientResponseException).isTrue();
			WebClientResponseException ex = (WebClientResponseException) throwable;
			assertThat(ex.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
			assertThat(ex.getRawStatusCode()).isEqualTo(HttpStatus.NOT_FOUND.value());
			assertThat(ex.getStatusText()).isEqualTo(HttpStatus.NOT_FOUND.getReasonPhrase());
			assertThat(ex.getHeaders().getContentType()).isEqualTo(MediaType.APPLICATION_JSON);
			assertThat(ex.getResponseBodyAsString()).isEqualTo("{\"product\":{\"item\":{}}}");
		}).verify();
	}

	@Test(timeout = 20000)
	public void givenItemIdSpace_WhenFindPriceByTCIN_ThenEmpty() throws Exception {
		// Given
		String id = " ";

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
	public void givenItemIdSpace_WhenFindItemByTCIN_ThenEmpty() throws Exception {
		// Given
		String id = " ";
		getServer().enqueue(jsonResponse(404).setBody("{\"product\":{\"item\":{}}}"));

		// When
		Mono<ItemResponse> itemResponse = getService().findItemByTCIN(id);

		// Then
		StepVerifier.create(itemResponse)
		.expectErrorSatisfies(throwable -> {
			assertThat(throwable instanceof WebClientResponseException).isTrue();
			WebClientResponseException ex = (WebClientResponseException) throwable;
			assertThat(ex.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
			assertThat(ex.getRawStatusCode()).isEqualTo(HttpStatus.NOT_FOUND.value());
			assertThat(ex.getStatusText()).isEqualTo(HttpStatus.NOT_FOUND.getReasonPhrase());
			assertThat(ex.getHeaders().getContentType()).isEqualTo(MediaType.APPLICATION_JSON);
			assertThat(ex.getResponseBodyAsString()).isEqualTo("{\"product\":{\"item\":{}}}");
			})
		.verify();
	}

	@Test(timeout = 20000)
	public void givenItemIdNotInteger_WhenFindPriceByTCIN_ThenEmpty() throws Exception {
		// Given
		String id = "notinteger";

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
	public void givenItemIdNotInteger_WhenFindItemByTCIN_ThenEmpty() throws Exception {
		// Given
		String id = "notinteger";
		getServer().enqueue(jsonResponse(404).setBody("{\"product\":{\"item\":{}}}"));

		// When
		Mono<ItemResponse> itemResponse = getService().findItemByTCIN(id);

		// Then
		StepVerifier.create(itemResponse)
		.expectErrorSatisfies(throwable -> {
			assertThat(throwable instanceof WebClientResponseException).isTrue();
			WebClientResponseException ex = (WebClientResponseException) throwable;
			assertThat(ex.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
			assertThat(ex.getRawStatusCode()).isEqualTo(HttpStatus.NOT_FOUND.value());
			assertThat(ex.getStatusText()).isEqualTo(HttpStatus.NOT_FOUND.getReasonPhrase());
			assertThat(ex.getHeaders().getContentType()).isEqualTo(MediaType.APPLICATION_JSON);
			assertThat(ex.getResponseBodyAsString()).isEqualTo("{\"product\":{\"item\":{}}}");
			})
		.verify();
	}

	@Test(timeout = 20000)
	public void givenItemIdZero_WhenFindPriceByTCIN_ThenEmpty() throws Exception {
		// Given
		when(getRepository().findByTcin(Mockito.anyInt())).thenReturn(Optional.empty());
		String id = "0";

		// When
		Optional<Price> price = getService().findPriceByTCIN(id);

		// Then
		assertTrue(price.isEmpty());
	}

	@Test(timeout = 20000)
	public void givenItemIdZero_WhenFindItemByTCIN_ThenEmpty() throws Exception {
		// Given
		String id = "0";
		getServer().enqueue(jsonResponse(404).setBody("{\"product\":{\"item\":{}}}"));

		// When
		Mono<ItemResponse> itemResponse = getService().findItemByTCIN(id);

		// Then
		StepVerifier.create(itemResponse)
		.expectErrorSatisfies(throwable -> {
			assertThat(throwable instanceof WebClientResponseException).isTrue();
			WebClientResponseException ex = (WebClientResponseException) throwable;
			assertThat(ex.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
			assertThat(ex.getRawStatusCode()).isEqualTo(HttpStatus.NOT_FOUND.value());
			assertThat(ex.getStatusText()).isEqualTo(HttpStatus.NOT_FOUND.getReasonPhrase());
			assertThat(ex.getHeaders().getContentType()).isEqualTo(MediaType.APPLICATION_JSON);
			assertThat(ex.getResponseBodyAsString()).isEqualTo("{\"product\":{\"item\":{}}}");
			})
		.verify();
	}

	// ============================ NOT OUR CONTROLLER
	@Test(timeout = 20000)
	public void givenItemIdNull_WhenFindPriceByTCIN_ThenEmpty() throws Exception {
		// Given
		String id = null;

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
	public void givenItemIdNull_WhenFindItemByTCIN_ThenEmpty() throws Exception {
		// Given
		String id = null;
		getServer().enqueue(jsonResponse(404).setBody("{\"product\":{\"item\":{}}}"));

		// When
		Mono<ItemResponse> itemResponse = getService().findItemByTCIN(id);

		// Then
		StepVerifier.create(itemResponse)
		.expectErrorSatisfies(throwable -> {
			assertThat(throwable instanceof WebClientResponseException).isTrue();
			WebClientResponseException ex = (WebClientResponseException) throwable;
			assertThat(ex.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
			assertThat(ex.getRawStatusCode()).isEqualTo(HttpStatus.NOT_FOUND.value());
			assertThat(ex.getStatusText()).isEqualTo(HttpStatus.NOT_FOUND.getReasonPhrase());
			assertThat(ex.getHeaders().getContentType()).isEqualTo(MediaType.APPLICATION_JSON);
			assertThat(ex.getResponseBodyAsString()).isEqualTo("{\"product\":{\"item\":{}}}");
			})
		.verify();
	}

	@Test(timeout = 20000)
	public void givenItemIdEmpty_WhenFindPriceByTCIN_ThenEmpty() throws Exception {
		// Given
		String id = "";

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
	public void givenItemIdEmpty_WhenFindItemByTCIN_ThenEmpty() throws Exception {
		// Given
		String id = "";
		getServer().enqueue(jsonResponse(404).setBody("{\"product\":{\"item\":{}}}"));

		// When
		Mono<ItemResponse> itemResponse = getService().findItemByTCIN(id);

		// Then
		StepVerifier.create(itemResponse).expectErrorSatisfies(throwable -> {
			assertThat(throwable instanceof WebClientResponseException).isTrue();
			WebClientResponseException ex = (WebClientResponseException) throwable;
			assertThat(ex.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
			assertThat(ex.getRawStatusCode()).isEqualTo(HttpStatus.NOT_FOUND.value());
			assertThat(ex.getStatusText()).isEqualTo(HttpStatus.NOT_FOUND.getReasonPhrase());
			assertThat(ex.getHeaders().getContentType()).isEqualTo(MediaType.APPLICATION_JSON);
			assertThat(ex.getResponseBodyAsString()).isEqualTo("{\"product\":{\"item\":{}}}");
		}).verify();
	}

	/************************ END INPUT VALIDATION *******************************/

	@Test(timeout = 20000)
	public void givenItemIdNotExistAndPriceNotExist_WhenFindByTcin_ThenNotFound() throws Exception {
		// Given
		String id = "13860428";
		when(getRepository().findByTcin(Mockito.anyInt())).thenReturn(Optional.empty());
		enqueueProductNotFound(server);

		// When
				Mono<Product> product = getService().findProductByTcin(id);

		// Then
		StepVerifier.create(product)
		.expectErrorSatisfies(throwable -> {
			assertThat(throwable instanceof WebClientResponseException).isTrue();
			WebClientResponseException ex = (WebClientResponseException) throwable;
			assertThat(ex.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
			assertThat(ex.getRawStatusCode()).isEqualTo(HttpStatus.NOT_FOUND.value());
			assertThat(ex.getStatusText()).isEqualTo(HttpStatus.NOT_FOUND.getReasonPhrase());
			assertThat(ex.getHeaders().getContentType()).isEqualTo(MediaType.APPLICATION_JSON);
			assertThat(ex.getResponseBodyAsString()).isEqualTo("{\"product\":{\"item\":{}}}");
			})
		.verify();
	}

	@Test(timeout = 20000)
	public void givenItemIdNotExistAndPriceExists_WhenFindByTcin_ThenEmpty() throws Exception {
		// Given
		String id = "13860428";
		BigDecimal value = BigDecimal.valueOf(15.99);
		String currency = "JPY";
		when(getRepository().findByTcin(Mockito.anyInt()))
				.thenReturn(Optional.of(new Price(Integer.valueOf(id), value, currency)));
		enqueueProductNotFound(server);

		// When
		Mono<Product> product = getService().findProductByTcin(id);

		// Then
		StepVerifier.create(product)
		.expectErrorSatisfies(throwable -> {
			assertThat(throwable instanceof WebClientResponseException).isTrue();
			WebClientResponseException ex = (WebClientResponseException) throwable;
			assertThat(ex.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
			assertThat(ex.getRawStatusCode()).isEqualTo(HttpStatus.NOT_FOUND.value());
			assertThat(ex.getStatusText()).isEqualTo(HttpStatus.NOT_FOUND.getReasonPhrase());
			assertThat(ex.getHeaders().getContentType()).isEqualTo(MediaType.APPLICATION_JSON);
			assertThat(ex.getResponseBodyAsString()).isEqualTo("{\"product\":{\"item\":{}}}");
			})
		.verify();
	}

	@Test(timeout = 20000)
	public void givenItemIdAndPriceExists_WhenFindByTCIN_ThenReturnSingleProductWithPrice() throws Exception {
		// Given
		int id = 13860428;
		BigDecimal value = BigDecimal.valueOf(15.99);
		String currency = "JPY";
		when(getRepository().findByTcin(Mockito.anyInt())).thenReturn(Optional.of(new Price(id, value, currency)));
		enqueueValidProduct(server);

		// When
		Mono<Product> product = getService().findProductByTcin(String.valueOf(id));
		printJson(product);

		// Then
		StepVerifier.create(product).assertNext(p -> {
			assertEquals(id, p.getItem().getId());
			assertEquals("The Big Lebowski (Blu-ray)", p.getItem().getName());
			assertTrue(p.getCurrent_price().isPresent());
			assertEquals(value, p.getCurrent_price().get().getValue());
			assertEquals(currency, p.getCurrent_price().get().getCurrency_code());
		}).expectComplete().verify();
	}

	@Test(timeout = 20000)
	public void givenItemPriceExists_WhenFindPriceByTCIN_ThenReturnPrice() throws Exception {
		// Given
		String id = "13860428";
		BigDecimal value = BigDecimal.valueOf(15.99);
		String currency = "JPY";
		when(getRepository().findByTcin(Mockito.anyInt()))
				.thenReturn(Optional.of(new Price(Integer.valueOf(id), value, currency)));

		// When
		Optional<Price> price = getService().findPriceByTCIN(id);

		// Then
		assertTrue(price.isPresent());
		assertEquals((int) Integer.valueOf(id), price.get().getTcin());
		assertEquals(value, price.get().getValue());
		assertEquals(currency, price.get().getCurrency_code());
	}

	@Test(timeout = 20000)
	public void givenItemIdExists_WhenFindItemByTCIN_ThenReturnItem() throws Exception {
		// Given
		int id = 13860428;
		enqueueValidProduct(getServer());

		// When
		Mono<Product> product = getService().findProductByTcin(String.valueOf(id));

		// Then
		StepVerifier.create(product).assertNext(p -> {
			assertEquals(id, p.getItem().getId());
			assertEquals("The Big Lebowski (Blu-ray)", p.getItem().getName());
			assertTrue(p.getCurrent_price().isEmpty());
		}).expectComplete().verify();
	}

	@Test(timeout = 20000)
	public void givenItemPriceMissing_WhenFindPriceByTCIN_ThenReturnEmpty() throws Exception {
		// Given
		String id = "13860428";
		when(getRepository().findByTcin(Mockito.anyInt())).thenReturn(Optional.empty());

		// When
		Optional<Price> price = getService().findPriceByTCIN(id);

		// Then
		assertTrue(price.isEmpty());
	}

	@Test(timeout = 20000)
	public void givenItemIdNotFound_WhenFindItemByTCIN_ThenNotFound() throws Exception {
		// Given
		String id = "13860428";
		enqueueProductNotFound(server);

		// When
		Mono<Product> product = getService().findProductByTcin(id);

		// Then
		StepVerifier.create(product).expectErrorSatisfies(throwable -> {
			assertThat(throwable instanceof WebClientResponseException).isTrue();
			WebClientResponseException ex = (WebClientResponseException) throwable;
			assertThat(ex.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
			assertThat(ex.getRawStatusCode()).isEqualTo(HttpStatus.NOT_FOUND.value());
			assertThat(ex.getStatusText()).isEqualTo(HttpStatus.NOT_FOUND.getReasonPhrase());
			assertThat(ex.getHeaders().getContentType()).isEqualTo(MediaType.APPLICATION_JSON);
			assertThat(ex.getResponseBodyAsString()).isEqualTo("{\"product\":{\"item\":{}}}");
		}).verify();
	}

	@Test()
	public void givenItemIdAndPriceNotExist_WhenFindByTCIN_ThenReturnSingleProductWithoutPrice() throws Exception {
		// Given
		when(getRepository().findByTcin(Mockito.anyInt())).thenReturn(Optional.empty());
		int id = 13860428;
		enqueueValidProduct(getServer());

		// When
		Mono<Product> product = getService().findProductByTcin(String.valueOf(id));

		// Then
		StepVerifier.create(product).assertNext(p -> {
			assertEquals(id, p.getItem().getId());
			assertEquals("The Big Lebowski (Blu-ray)", p.getItem().getName());
			assertTrue(p.getCurrent_price().isEmpty());
		}).expectComplete().verify();
	}

	private static MockResponse jsonResponse(int code) {
		return new MockResponse().setResponseCode(code).setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON);
	}

	/** mocked lookup returns single valid item with tcin:"13860428" **/
	private void enqueueValidProduct(MockWebServer server) throws URISyntaxException, IOException {
		/* Get the response for the item lookup from file */
		String resourceFileName = "RestClientTest-givenItemIdAndPriceNotExist_ThenReturnSingleProduct.json";
		URL url = getClass().getClassLoader().getResource(resourceFileName);
		Path path = Paths.get(url.toURI());
		String jsonBody = Files.readString(path);
		/* return response in item lookup */
		server.enqueue(jsonResponse(200).setBody(jsonBody));
	}

	/** mocked lookup returns 404 with skeletal body **/
	private void enqueueProductNotFound(MockWebServer server) {
		server.enqueue(jsonResponse(404).setBody("{\"product\":{\"item\":{}}}"));
	}

	@Configuration
	static class Config {
		@Bean
		public String mockBaseUri() {
			return String.format("http://localhost:%d/", ProductServiceTest.mockPort);
		}
	}

}
