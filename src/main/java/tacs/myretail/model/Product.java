package tacs.myretail.model;

import java.util.Optional;

import com.fasterxml.jackson.annotation.JsonInclude;

/** 
 * Provides mash up of external REST source with local pricing
 * 
 *
 */
@JsonInclude(JsonInclude.Include.NON_ABSENT)
public class Product {
	private int id;
	private String name;
	private Optional<Price> current_price;
	
	public Product(int id, String name, Price price) {
		this.id = id;
		this.name = name;
		this.current_price = Optional.ofNullable(price);
	}
	public long getId() {
		return id;
	}
	public String getName() {
		return name;
	}
	public Optional<Price> getCurrent_price() {
		return current_price;
	}

}
