package tacs.myretail.model;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import reactor.core.publisher.Mono;
import tacs.myretail.model.rest.ItemResponse;

@Service
public class ProductService {
//	private static Logger log = LogManager.getLogger();
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
	
	Optional<Price> findPriceByTCIN(String tcin) throws NumberFormatException{
		Optional<Price> price = getRepository().findByTcin(Integer.valueOf(tcin));
		return price;
	}
	Mono<ItemResponse> findItemByTCIN(String tcin) {
		return getWebClient().get().uri(builder -> builder.build(tcin))
				.retrieve()
				.bodyToMono(ItemResponse.class);
	}
//	@ExceptionHandler(WebClientResponseException.class)
//	public ResponseEntity<String> handleWebClientResponseException(WebClientResponseException ex) {
//	    log.error("Error from WebClient - Status {}, Body {}", ex.getRawStatusCode(), ex.getResponseBodyAsString(), ex);
//	    return ResponseEntity.status(ex.getRawStatusCode()).body(ex.getResponseBodyAsString());
//	}	
	public Mono<Product> findProductByTcin(String tcin) {
		Mono<ItemResponse> mono = findItemByTCIN(tcin); 
		Optional<Price> currentPrice = findPriceByTCIN(tcin);
		return mono.map(ir -> new Product(ir.getItem(), currentPrice));
	}
}
