package tacs.myretail.model;

import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import tacs.myretail.model.rest.ItemResponse;

@Service
public class ProductService {
	private static Logger log = LogManager.getLogger();
	@Autowired
	private WebClient productWebClient;
	@Autowired
	private PriceRepository priceRepository;
	
	private WebClient getWebClient() {
		return this.productWebClient;
	}
	private PriceRepository getRepository() {
		return this.priceRepository;
	}

	private Optional<Price> findPriceByTCIN(String tcin) {
		Optional<Price> price = getRepository().findByTcin(Integer.valueOf(tcin));
		return price;
		
		
	}
	public Product findByTcin(String tcin) {
		Optional<Price> currentPrice = findPriceByTCIN(tcin);
		
		
		Optional<ItemResponse> ir = getWebClient().get().uri(builder -> builder.build(tcin))
				.exchange().filter(cr -> cr.statusCode().is2xxSuccessful())
				.flatMap(response -> response.bodyToMono(ItemResponse.class))
				.blockOptional();
		ItemResponse item = ir.orElseThrow();
		Product product = new Product(item.getTcin(), item.getTitle(), currentPrice.orElse(null));
		return product;
	}
	@ExceptionHandler(WebClientResponseException.class)
	public ResponseEntity<String> handleWebClientResponseException(WebClientResponseException ex) {
	    log.error("Error from WebClient - Status {}, Body {}", ex.getRawStatusCode(), ex.getResponseBodyAsString(), ex);
	    return ResponseEntity.status(ex.getRawStatusCode()).body(ex.getResponseBodyAsString());
	}	
}
