package tacs.myretail.model.rest;

import org.junit.Before;
import org.junit.Test;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.DefaultUriBuilderFactory;

import reactor.core.publisher.Mono;

public class ItemTest {
	private final String REDSKY_TCIN = "https://redsky.target.com/v2/plp/search/?channel=web&count=24&default_purchasability_filter=true&facet_recovery=false&isDLP=false&keyword=football&offset=0&pageId=%2Fs%2Ffootball&pricing_store_id=663&scheduled_delivery_store_id=663&store_ids=663%2C1068%2C1211%2C2449%2C1272&visitorId=016F4998B7530201A7E92CCFB1872770&include_sponsored_search_v2=true&ppatok=AOxT33a&platform=desktop&useragent=Mozilla&key=eb2551e4accc14f38cc42d32fbc2b2ea&excludes=taxonomy,price,promotion,bulk_ship,rating_and_review_reviews,rating_and_review_statistics,question_answer_statistics";
//	private final String REDSKY_TCIN = "https://redsky.target.com/v2/pdp/tcin/{tcin}?excludes=taxonomy,price,promotion,bulk_ship,rating_and_review_reviews,rating_and_review_statistics,question_answer_statistics";
	private WebTestClient webTestClient;
	private WebClient webClient;

	@Before
	public void before() {
//		UriBuilderFactory ubf = new DefaultUriBuilderFactory("http://redsky.target.com/v2/pdp/tcin/{tcin}?excludes=taxonomy,price,promotion,bulk_ship,rating_and_review_reviews,rating_and_review_statistics,question_answer_statistics");
		this.webClient = WebClient.builder().uriBuilderFactory(new DefaultUriBuilderFactory(REDSKY_TCIN))
				.filter(logRequest()).build();
//		Mono mono = wc.get().accept(MediaType.APPLICATION_JSON)
//				.exchange().;//attributes(System.out::println);
		this.webTestClient = WebTestClient.bindToServer().build();
	}

	@Test
	public void empty() {
		send();
	}

	private static ExchangeFilterFunction logRequest() {
		return ExchangeFilterFunction.ofRequestProcessor(clientRequest -> {
			System.out.printf("Request: {%s} {%s}", clientRequest.method(), clientRequest.url());
			clientRequest.headers()
					.forEach((name, values) -> values.forEach(value -> System.out.printf("{%s}={%s}", name, value)));
			System.out.println();
			return Mono.just(clientRequest);
		});
	}

	// Just example of sending request
	public void send() {
		SearchResponse tcin = webClient.get().uri(builder -> builder.build(13860428))
//		ClientResponse tcin 
				.exchange()
//				 .accept(MediaType.APPLICATION_JSON)
//			        .retrieve().bodyToMono(DougsClass.class)
				.flatMap(response -> response.bodyToMono(SearchResponse.class))
//				.bodyToFlux(Search_Recommendations.class).blockFirst();
				.block();
//		System.out.printf("Response: {%s}", tcin.toEntity(SearchResponse.class).block());
		System.out.println(tcin);
//		Product p = tcin.getProduct();
//		System.out.println(p);
//		List<Item> i = tcin.getItems();
//		System.out.println(i);
	}
}
