package tacs.myretail.model;

import java.util.NoSuchElementException;
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
	
	/*package-private*/ Optional<Price> findPriceByTCIN(String tcin) throws NumberFormatException{
		Optional<Price> price = getRepository().findByTcin(Integer.valueOf(tcin));
		return price;
	}
	/*package-private*/ ItemResponse findItemByTCIN(String tcin) throws NoSuchElementException {
		Optional<ItemResponse> ir = getWebClient().get().uri(builder -> builder.build(tcin))
				.retrieve()
				.bodyToMono(ItemResponse.class)
				.blockOptional();
		return ir.get();
	}
	public Product findByTcin(String tcin) {
		try {
		Optional<Price> currentPrice = findPriceByTCIN(tcin);
		ItemResponse item = findItemByTCIN(tcin);

		Product product = new Product(item.getItem(), currentPrice.orElse(null));
		return product;
		} catch (WebClientResponseException wce) {
			throw new NoSuchElementException();
		}
	}
//	@ExceptionHandler(WebClientResponseException.class)
//	public ResponseEntity<String> handleWebClientResponseException(WebClientResponseException ex) {
//	    log.error("Error from WebClient - Status {}, Body {}", ex.getRawStatusCode(), ex.getResponseBodyAsString(), ex);
//	    return ResponseEntity.status(ex.getRawStatusCode()).body(ex.getResponseBodyAsString());
//	}	
}
