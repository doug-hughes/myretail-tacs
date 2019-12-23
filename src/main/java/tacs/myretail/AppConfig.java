package tacs.myretail;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class AppConfig {
// Switch to WebClient	
//	@Bean
//	public RestTemplate restTemplate(RestTemplateBuilder builder) {
//		return builder.rootUri("http://redsky.target.com/v2/pdp/tcin") //13860428?excludes=taxonomy,price,promotion,bulk_ship,rating_and_review_reviews,rating_and_review_statistics,question_answer_statistics)
//				.build();
//	}
}
