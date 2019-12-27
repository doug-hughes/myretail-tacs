package tacs.myretail.model;

/** 
 * Provides mash up of external REST source with local pricing
 * 
 *
 */
public class Product {
	private long id;
	private String name;
	private PriceIF current_price;
	
	public Product(long id, String name, PriceIF price) {
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
	public PriceIF getCurrent_price() {
		return current_price;
	}

}
