package tacs.myretail;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.DefaultUriBuilderFactory;
import org.springframework.web.util.UriBuilderFactory;
import org.springframework.web.util.UriComponentsBuilder;

import reactor.core.publisher.Mono;

@Configuration
public class AppConfig {
	private static final Logger log = LogManager.getLogger();
	private static final String QUERY_FRAGMENT = "excludes=taxonomy,price,promotion,bulk_ship,rating_and_review_reviews,rating_and_review_statistics,question_answer_statistics";


//	@Value("${application.title}")
//	private String appTitle;
//	
//	@Bean String appTitle() {
//		return this.appTitle;
//	}
	@Value("${item.lookup.baseuri}")
	private String baseUri;

	@Autowired(required = false)
	private String mockBaseUri;
	
	private String getBaseUri() {
		log.debug("Lookup query mockUri {}", String.valueOf(this.mockBaseUri));
		return this.mockBaseUri != null ? this.mockBaseUri : this.baseUri;
	}
	
	private UriBuilderFactory getUriBuilderFactory() {
		log.debug("Lookup query baseUri {}", getBaseUri());
		UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromHttpUrl(getBaseUri()).path("/v2/pdp/tcin/{tcin}").replaceQuery(QUERY_FRAGMENT);
		log.debug("Lookup query template {}", uriBuilder.toUriString());
		return new DefaultUriBuilderFactory(uriBuilder);
	}
	@Bean
	public WebClient productWebClient() {
		return WebClient.builder().uriBuilderFactory(getUriBuilderFactory()).filter(logRequest())
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
