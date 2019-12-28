package tacs.myretail.model;

/** 
 * Provides mash up of external REST source with local pricing
 * 
 *
 */
public class Product {
	private int id;
	private String name;
	private Price current_price;
	
	public Product(int id, String name, Price price) {
		this.id = id;
		this.name = name;
		this.current_price = price;
	}
	public long getId() {
		return id;
	}
	public String getName() {
		return name;
	}
	public Price getCurrent_price() {
		return current_price;
	}

}
