package tacs.myretail.model;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import tacs.myretail.model.rest.ItemResponse;

@Service
public class ProductService {
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

	private Price findPriceByTCIN(String tcin) {
		Price price = getRepository().findByTcin(Long.valueOf(tcin));
		return price;
		
		
	}
	public Product findByTcin(String tcin) {
		
/*		
		PriceIF staticPrice = new PriceIF() {

			@Override
			public BigDecimal getValue() {
				// TODO Auto-generated method stub
				return BigDecimal.valueOf(10);
			}

			@Override
			public String getCurrencyCode() {
				// TODO Auto-generated method stub
				return "USD";
			}
			
		};
*/
		Price currentPrice = findPriceByTCIN(tcin);
		ItemResponse ir = getWebClient().get().uri(builder -> builder.build(tcin))
				.exchange()
				.flatMap(response -> response.bodyToMono(ItemResponse.class))
				.block();
		Product product = new Product(ir.getTcin(), ir.getTitle(), currentPrice);
		return product;
	}
	
}
