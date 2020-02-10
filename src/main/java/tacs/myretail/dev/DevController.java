package tacs.myretail.dev;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.DefaultUriBuilderFactory;

import reactor.core.publisher.Flux;
import tacs.myretail.model.Price;
import tacs.myretail.model.PriceRepository;

/**
 * Controller for development endpoints, will not be included in jar
 */
@RestController
public class DevController {
	private static final String REDSKY_TCIN = "https://redsky.target.com/v2/plp/search/?channel=web&count=24&default_purchasability_filter=true&facet_recovery=false&isDLP=false&offset=0&pageId=%2Fs%2Ffootball&pricing_store_id=663&scheduled_delivery_store_id=663&store_ids=663%2C1068%2C1211%2C2449%2C1272&visitorId=016F4998B7530201A7E92CCFB1872770&include_sponsored_search_v2=true&ppatok=AOxT33a&platform=desktop&useragent=Mozilla&key=eb2551e4accc14f38cc42d32fbc2b2ea&excludes=taxonomy,promotion,bulk_ship,rating_and_review_reviews,rating_and_review_statistics,question_answer_statistics";
	@Autowired
	private PriceRepository priceRepository;

	/**
	 * This will populate the MongoDb prices repository with data from the endpoint
	 * It will not overwrite existing entries
	 * @param query
	 * @return
	 */
	@PostMapping(path = "dev/prices/populate")
	public Flux<Integer> populatePricesFromTargetQuery(
			@RequestParam(value = "query", defaultValue = "football") String query) {
		WebClient itemWebClient = WebClient.builder().uriBuilderFactory(new DefaultUriBuilderFactory(REDSKY_TCIN))
				.build();
		return itemWebClient.get().uri(builder -> builder.queryParam("keyword", query).build())
			.retrieve()
			.bodyToMono(SearchResult.class)
			.flatMapMany(sr -> Flux.fromIterable(sr.getSearchResponse().getItems().getItem()))
			.filter(item -> item.getTcin() > 0 && item.getCurrentRetail() != null)
			.map(item -> priceRepository.findByTcin(item.getTcin())
				.orElseGet(() -> {
					Price newPrice = new Price(item.getTcin(), item.getCurrentRetail(), "USD");
					Price saved = priceRepository.save(newPrice);
					return saved;
			}))
			.map(price -> price.getTcin());
	}

}
