package tacs.myretail.model;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProductService {
	@Autowired
	private PriceIF price;

	public PriceIF getPrice() {
		return price;
	}

	public void setPrice(PriceIF price) {
		this.price = price;
	}

	@Override
	public String toString() {
		return "ProductService [price=" + price + "]";
	}

}
