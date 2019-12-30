package tacs.myretail.model;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.reactive.function.client.WebClient;

@RunWith(SpringJUnit4ClassRunner.class)
//@ContextConfiguration(classes = { ProductService.class })
public class ProductServiceTest {

//	private MockMvc mockMvc;
	@MockBean
	private PriceRepository priceRepository;
	@MockBean
	private WebClient productWebClient;
	private final ProductService service = new ProductService(productWebClient, priceRepository);


//	private MockMvc getMockMvc() {
//		return this.mockMvc;
//	}
	private ProductService getService() {
		return this.service;
	}

	@Before
	public void setUp() throws Exception {
//		this.mockMvc = MockMvcBuilders.standaloneSetup(service).build();
	}

	@Test()
	public void testFindPriceByTCIN_InvalidIdentifier() throws Exception {
		// Given Mocked Config at end of file annotated with @TestConfiguration
		// When
		Product product = getService().findByTcin("c3po");
//		when(productService.findByTcin("c3po")).thenThrow(NumberFormatException.class);

		// Then
//		getMockMvc().perform(get("/products/c3po")).andExpect(status().isNotFound())
//				.andExpect(jsonPath("$").doesNotExist());
	}

	@Test
	public void testFindPriceByTCIN_GreaterThanMaxInteger() throws Exception {
		// Given Mocked Config at end of file annotated with @TestConfiguration
		// When
		Product product = getService().findByTcin(Long.toString(Long.MAX_VALUE));
//		when(productService.findByTcin("c3po")).thenThrow(NumberFormatException.class);

		// Then
//		getMockMvc().perform(get("/products/c3po")).andExpect(status().isNotFound())
//				.andExpect(jsonPath("$").doesNotExist());
	}

	@Test
	public void testFindPriceByTCIN_NegativeIdentifier() throws Exception {
		// Given Mocked Config at end of file annotated with @TestConfiguration
		// When
		Product product = getService().findByTcin("-1");
//			when(productService.findByTcin("c3po")).thenThrow(NumberFormatException.class);

		// Then
//			getMockMvc().perform(get("/products/c3po")).andExpect(status().isNotFound())
//					.andExpect(jsonPath("$").doesNotExist());
	}
}
