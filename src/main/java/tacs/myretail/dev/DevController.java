package tacs.myretail.dev;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.DefaultUriBuilderFactory;

import tacs.myretail.dev.SearchResponse.Item;
import tacs.myretail.dev.SearchResponse.Items;
import tacs.myretail.model.Price;
import tacs.myretail.model.PriceRepository;

@RestController
public class DevController {
	private static final String REDSKY_TCIN = "https://redsky.target.com/v2/plp/search/?channel=web&count=24&default_purchasability_filter=true&facet_recovery=false&isDLP=false&offset=0&pageId=%2Fs%2Ffootball&pricing_store_id=663&scheduled_delivery_store_id=663&store_ids=663%2C1068%2C1211%2C2449%2C1272&visitorId=016F4998B7530201A7E92CCFB1872770&include_sponsored_search_v2=true&ppatok=AOxT33a&platform=desktop&useragent=Mozilla&key=eb2551e4accc14f38cc42d32fbc2b2ea&excludes=taxonomy,promotion,bulk_ship,rating_and_review_reviews,rating_and_review_statistics,question_answer_statistics";
	@Autowired
	private PriceRepository priceRepository;

	@PostMapping(path = "dev/actions/populate")
	public ResponseEntity<List<Integer>> populatePricesFromTargetQuery(
			@RequestParam(value = "query", defaultValue = "football") String query) {
		WebClient itemWebClient = WebClient.builder().uriBuilderFactory(new DefaultUriBuilderFactory(REDSKY_TCIN))
				.build();
		SearchResponse sr = itemWebClient.get().uri(builder -> builder.queryParam("keyword", query).build()).exchange()
				.flatMap(r -> r.bodyToMono(SearchResponse.class)).block();
		Items qryResults = sr.getSearch_response().getItems();
		List<Integer> savedTcins = new ArrayList<>();
		for (Item item : qryResults.getItem()) {
			if (item.getTcin() > 0 && item.getCurrentRetail() != null) {
				Price saved = priceRepository.save(new Price(item.getTcin(), item.getCurrentRetail(), "USD"));
				savedTcins.add(saved.getTcin());
			}
		}
		ResponseEntity<List<Integer>> response = ResponseEntity.ok(savedTcins);
		return response;
	}

}
