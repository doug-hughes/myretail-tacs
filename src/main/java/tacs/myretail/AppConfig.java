package tacs.myretail;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.index.IndexOperations;
import org.springframework.data.mongodb.core.index.IndexResolver;
import org.springframework.data.mongodb.core.index.MongoPersistentEntityIndexResolver;
import org.springframework.data.mongodb.core.mapping.MongoMappingContext;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.DefaultUriBuilderFactory;

import reactor.core.publisher.Mono;
import tacs.myretail.model.Price;

@Configuration
public class AppConfig {
	private static final Logger log = LogManager.getLogger();
	private final String REDSKY_TCIN = "https://redsky.target.com/v2/pdp/tcin/{tcin}?excludes=taxonomy,price,promotion,bulk_ship,rating_and_review_reviews,rating_and_review_statistics,question_answer_statistics";
	@Autowired
	private MongoTemplate mongoTemplate;
	@Autowired
	private MongoMappingContext mongoMappingContext;


	@Value("${application.title}")
	private String appTitle;
	
	@Bean String appTitle() {
		return this.appTitle;
	}
	
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
	@EventListener(ApplicationReadyEvent.class)
	public void initIndicesAfterStartup() {

		IndexOperations indexOps = this.mongoTemplate.indexOps(Price.class);

		IndexResolver resolver = new MongoPersistentEntityIndexResolver(mongoMappingContext);
		resolver.resolveIndexFor(Price.class).forEach(indexOps::ensureIndex);

	}

}
