package tacs.myretail;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.DefaultUriBuilderFactory;
import org.springframework.web.util.UriBuilderFactory;

@Configuration
public class AppConfig {
// Switch to WebClient	
//	@Bean
//	public RestTemplate restTemplate(RestTemplateBuilder builder) {
//		return builder.rootUri("http://redsky.target.com/v2/pdp/tcin") //13860428?excludes=taxonomy,price,promotion,bulk_ship,rating_and_review_reviews,rating_and_review_statistics,question_answer_statistics)
//				.build();
//	}
	@Bean
	public WebClient webClient() {
		UriBuilderFactory ubf = new DefaultUriBuilderFactory("http://redsky.target.com/v2/pdp/tcin/{tcin}?excludes=taxonomy,price,promotion,bulk_ship,rating_and_review_reviews,rating_and_review_statistics,question_answer_statistics");
		return WebClient.builder().uriBuilderFactory(ubf).build();
	}
}
