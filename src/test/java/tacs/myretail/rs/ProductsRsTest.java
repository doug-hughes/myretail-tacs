package tacs.myretail.rs;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.NoSuchElementException;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import tacs.myretail.model.ProductService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { ProductsRs.class })
//@WebMvcTest(ProductsRs.class)
public class ProductsRsTest {
	
	private MockMvc mockMvc;
	
	@MockBean
	private ProductService productService;
	@Autowired
	private ProductsRs productsRs;
	
	
	private MockMvc getMockMvc() {
		return this.mockMvc;
	}

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
		this.mockMvc = MockMvcBuilders.standaloneSetup(productsRs).build();
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testGetProduct_NoProductFound() throws Exception {
		// Given Mocked Config at end of file annotated with @TestConfiguration
		// When
		when(productService.findByTcin("15117729")).thenThrow(NoSuchElementException.class);
		
		// Then
		getMockMvc().perform(get("/products/15117729")).andExpect(status().isNotFound())
				.andExpect(jsonPath("$").doesNotExist());
	}

	@Test
	public void testGetProduct_InvalidIdentifier() throws Exception {
		// Given Mocked Config at end of file annotated with @TestConfiguration
		// When
		when(productService.findByTcin("c3po")).thenThrow(NumberFormatException.class);
		
		// Then
		getMockMvc().perform(get("/products/c3po")).andExpect(status().isNotFound())
				.andExpect(jsonPath("$").doesNotExist());
	}

}
