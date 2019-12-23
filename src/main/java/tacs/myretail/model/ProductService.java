package tacs.myretail.model;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class ProductService {
	private static Logger log = LogManager.getLogger();
	private final RestTemplate restTemplate;
	@Autowired
	private PriceIF price;

	ProductService(RestTemplate restTemplate) {
		this.restTemplate = restTemplate;
	}

	public PriceIF getPrice() {
		return price;
	}

	public void setPrice(PriceIF price) {
		this.price = price;
	}

}
