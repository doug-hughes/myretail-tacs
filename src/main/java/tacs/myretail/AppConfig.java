package tacs.myretail;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.DefaultUriBuilderFactory;

import reactor.core.publisher.Mono;

@Configuration
public class AppConfig {
	private static final Logger log = LogManager.getLogger();
	private final String REDSKY_TCIN = "https://redsky.target.com/v2/pdp/tcin/{tcin}?excludes=taxonomy,price,promotion,bulk_ship,rating_and_review_reviews,rating_and_review_statistics,question_answer_statistics";

	@Bean
	public WebClient productWebClient() {
		return WebClient.builder().uriBuilderFactory(new DefaultUriBuilderFactory(REDSKY_TCIN)).filter(logRequest())
				.build();
	}

	private ExchangeFilterFunction logRequest() {
		return ExchangeFilterFunction.ofRequestProcessor(clientRequest -> {
			log.debug(String.format("Request: {%s} {%s}", clientRequest.method(), clientRequest.url()));
			clientRequest.headers()
					.forEach((name, values) -> values.forEach(value -> log.trace(String.format("{%s}={%s}", name, value))));
			return Mono.just(clientRequest);
		});
	}

}
