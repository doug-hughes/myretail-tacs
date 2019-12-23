package tacs.myretail.model;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProductService {
	private static Logger log = LogManager.getLogger();
	@Autowired
	private PriceIF price;

	public PriceIF getPrice() {
		return price;
	}

	public void setPrice(PriceIF price) {
		this.price = price;
	}

}
