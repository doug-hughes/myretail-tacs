package tacs.myretail.model;

import java.util.Optional;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonUnwrapped;

import tacs.myretail.model.rest.ItemResponse.Item;

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
	
	public Product(Item item, Price price) {
		this.item = item;
		this.current_price = Optional.ofNullable(price);
	}
	public Item getItem() {
		return item;
	}
	public Optional<Price> getCurrent_price() {
		return current_price;
	}

}
