package tacs.myretail.model;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import tacs.myretail.model.rest.ItemResponse;

@Service
public class ProductService {
	@Autowired
	private WebClient productWebClient;

	public Product findByTcin(String tcin) {
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

		ItemResponse ir = this.productWebClient.get().uri(builder -> builder.build(tcin))
				.exchange()
				.flatMap(response -> response.bodyToMono(ItemResponse.class))
				.block();
		Product product = new Product(ir.getTcin(), ir.getTitle(), staticPrice);
		return product;
	}
}
