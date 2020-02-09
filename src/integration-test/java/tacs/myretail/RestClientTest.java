package tacs.myretail;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
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
import tacs.myretail.model.Price;
import tacs.myretail.model.PriceRepository;

/**
 * Test class that uses the MongoDb for the pricing information and
 * uses the actual external web service for validating successful integration
 * If either of these services are down or have changed, the test may fail.
 */
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
	public int getMockPort() {
		return RestClientTest.mockPort;
	}
///////////////////////////////////////////////////////////////// START UTILITY METHODS
	private static MockResponse jsonResponse(int code) {
		return new MockResponse().setResponseCode(code).setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON);
	}

	protected Price savePrice(Price price) {
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
	protected String getClassAndMethodName() {
		final int CALLING_METHOD = 1;
		StringBuilder sb = new StringBuilder(ClassUtils.getShortName(getClass()));
		sb.append('-').append(new Throwable().getStackTrace()[CALLING_METHOD].getMethodName());
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
