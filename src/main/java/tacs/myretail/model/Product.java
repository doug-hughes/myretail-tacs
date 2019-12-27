package tacs.myretail.model;

import tacs.myretail.model.rest.ItemResponse;

//{"id":13860428,"name":"The Big Lebowski (Blu-ray) (Widescreen)","current_price":{"value": 13.49,"currency_code":"USD"}}
public class Product {
	private long id;
	private String name;
	private PriceIF current_price;
	
	public Product(long id, String name, PriceIF price) {
		this.id = id;
		this.name = name;
		this.current_price = price;
	}
	public Product(ItemResponse item, PriceIF price) {
		new Product(item.getItem().getTcin(), item.getItem().getProduct_description().getTitle(), price);
	}
	public long getId() {
		return id;
	}
	public String getName() {
		return name;
	}
	public PriceIF getCurrent_price() {
		return current_price;
	}

}
