package tacs.myretail;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.DefaultUriBuilderFactory;

import reactor.core.publisher.Mono;

@Configuration
public class AppConfig {
	private final String REDSKY_TCIN = "https://redsky.target.com/v2/pdp/tcin/{tcin}?excludes=taxonomy,price,promotion,bulk_ship,rating_and_review_reviews,rating_and_review_statistics,question_answer_statistics";

	@Bean
	public WebClient productWebClient() {
		return WebClient.builder().uriBuilderFactory(new DefaultUriBuilderFactory(REDSKY_TCIN)).filter(logRequest())
				.build();
	}

	private ExchangeFilterFunction logRequest() {
		return ExchangeFilterFunction.ofRequestProcessor(clientRequest -> {
			System.out.printf("Request: {%s} {%s}", clientRequest.method(), clientRequest.url());
			clientRequest.headers()
					.forEach((name, values) -> values.forEach(value -> System.out.printf("{%s}={%s}", name, value)));
			System.out.println();
			return Mono.just(clientRequest);
		});
	}

}
