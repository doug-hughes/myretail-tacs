package tacs.myretail.model;

import java.util.Optional;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonUnwrapped;

import tacs.myretail.model.rest.Item;

/** 
 * Provides mash up of external REST source with local pricing
 * 
 *
 */
@JsonInclude(JsonInclude.Include.NON_ABSENT)
public class Product {
	@JsonUnwrapped
	private Item item;
	private Optional<Price> current_price;
	
	public Product(Item item, Optional<Price> price) {
		this.item = item;
		this.current_price = price;
	}
	public Product(Item item) {
		this.item = item;
		this.current_price = Optional.empty();
	}
	public Product() {
		super();
	}
	public Item getItem() {
		return item;
	}
	public void setItem(Item item) {
		this.item = item;
	}
	public Optional<Price> getCurrent_price() {
		return current_price;
	}
	public void setCurrentPrice(Optional<Price> current_price) {
		this.current_price = current_price;
	}
}
