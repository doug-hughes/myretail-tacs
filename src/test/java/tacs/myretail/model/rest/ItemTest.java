package tacs.myretail.model.rest;

import org.junit.Before;
import org.junit.Test;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.DefaultUriBuilderFactory;

import reactor.core.publisher.Mono;

public class ItemTest {
	private final String REDSKY_TCIN = "https://redsky.target.com/v2/pdp/tcin/{tcin}?excludes=taxonomy,price,promotion,bulk_ship,rating_and_review_reviews,rating_and_review_statistics,question_answer_statistics";
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
			return Mono.just(clientRequest);
		});
	}

	// Just example of sending request
	public void send() {
		TCIN tcin = webClient.get().uri(builder -> builder.build(13860428))
				.exchange()
				.flatMap(response -> response.bodyToMono(TCIN.class))
				.block();
//		System.out.printf("Response: {%s}", clientResponse.toEntity(String.class).block());
		System.out.println(tcin);
		Product p = tcin.getProduct();
		System.out.println(p);
		Item i = p.getItem();
		System.out.println(i);
	}
}
