package tacs.myretail.rs;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import reactor.core.publisher.Mono;
import tacs.myretail.model.Price;
import tacs.myretail.model.PriceRepository;
import tacs.myretail.model.Product;
import tacs.myretail.model.ProductService;
import tacs.myretail.model.rest.Item;

@RunWith(SpringRunner.class)
@WebFluxTest()
@ContextConfiguration(classes = {ProductController.class})
public class ProductControllerTest {
	@Autowired
//	private MockMvc mockMvc;
	private WebTestClient webTestClient;
	@MockBean
	private ProductService service;

	private ProductService getService() {
		return this.service;
	}

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

//	@Test
//	public void givenProductWithPrice_WhenGetProduct_ReturnProductWithPriceJson() throws Exception {
//		// Given
//		int id = 13860428;
//		BigDecimal value = BigDecimal.valueOf(15.99);
//		String currency = "JPY";
//		String name = "The Big Lebowski (Blu-ray)";
//		Product product = new Product(new Item(id, name), new Price(id, value, currency));
//		when(getService().findByTcin(Mockito.anyString())).thenReturn(product);
//		
//		// When
//		mockMvc.perform(get("/products/13860428")).andDo(print()).andExpect(status().isOk())
//        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
//        .andExpect(content()
//        		.string(containsString("{\"id\":13860428,\"name\":\"The Big Lebowski (Blu-ray)\",\"product_description\":{\"title\":\"The Big Lebowski (Blu-ray)\"},\"current_price\":{\"value\":15.99,\"currency_code\":\"JPY\"}}")));
//	}

	@Test
	public void givenProductWithPrice_WhenGetProduct_ReturnProductWithPriceJson() throws Exception {
		// Given
		int id = 13860428;
		BigDecimal value = BigDecimal.valueOf(15.99);
		String currency = "JPY";
		String name = "The Big Lebowski (Blu-ray)";
		Product product = new Product(new Item(id, name), Optional.of(new Price(id, value, currency)));
		
		// When
		when(getService().findProductByTcin(Mockito.anyString())).thenReturn(Mono.just(product));

		// Then
		webTestClient.get()
		.uri("/products/{id}", id)
		.accept(MediaType.APPLICATION_JSON)
		.exchange()
		.expectStatus().isOk()
		.expectHeader().valueEquals("Content-Type", "application/json")
//		.expectBody(Product.class)
//		.value(p -> p.getItem().getId(), equalTo(id))
//		.value(p -> p.getItem().getName(), equalTo(name))
//		.value(p -> p.getCurrent_price().get().getValue(), equalTo(value))
//		.value(p -> p.getCurrent_price().get().getCurrency_code(), equalTo(currency));
		.expectBody()
		.json("{\"id\":13860428,\"name\":\"The Big Lebowski (Blu-ray)\",\"product_description\":{\"title\":\"The Big Lebowski (Blu-ray)\"},\"current_price\":{\"value\":15.99,\"currency_code\":\"JPY\"}}");
	}
	@Test
	public void givenProductWithoutPrice_WhenGetProduct_ReturnProductWithoutPriceJson() throws Exception {
		// Given
		int id = 13860428;
		String name = "The Big Lebowski (Blu-ray)";
		Product product = new Product(new Item(id, name), Optional.empty());
		
		// When
		when(getService().findProductByTcin(Mockito.anyString())).thenReturn(Mono.just(product));

		// Then
		webTestClient.get()
		.uri("/products/{id}", id)
		.accept(MediaType.APPLICATION_JSON)
		.exchange()
		.expectStatus().isOk()
		.expectHeader().valueEquals("Content-Type", "application/json")
		.expectBody()
		.json("{\"id\":13860428,\"name\":\"The Big Lebowski (Blu-ray)\",\"product_description\":{\"title\":\"The Big Lebowski (Blu-ray)\"}}");
	}

//	@Test
//	public void givenProductNotFound_WhenGetProduct_ReturnNotFound() throws Exception {
//		// Given
//		int id = 13860428;
////		String name = "The Big Lebowski (Blu-ray)";
////		Product product = new Product(new Item(id, name), Optional.empty());
//		
//		// When
//		when(getService().findProductByTcin(Mockito.anyString())).thenThrow(new WebClientResponseException(HttpStatus.NOT_FOUND.value(), HttpStatus.NOT_FOUND.getReasonPhrase(), null, null, null));
//
//		// Then
//		webTestClient.get()
//		.uri("/products/{id}", id)
//		.accept(MediaType.APPLICATION_JSON)
//		.exchange()
//		.expectStatus().isNotFound()
//		.expectHeader().valueEquals("Content-Type", "application/json")
//		.expectBody()
//		.json("{\"id\":13860428,\"name\":\"The Big Lebowski (Blu-ray)\",\"product_description\":{\"title\":\"The Big Lebowski (Blu-ray)\"}}");
//	}
}
