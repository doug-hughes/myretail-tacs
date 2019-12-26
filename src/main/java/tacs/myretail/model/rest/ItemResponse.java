package tacs.myretail.model.rest;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ItemResponse {
	private Product product;

	public Product getProduct() {
		return product;
	}

	@Override
	public String toString() {
		return "TCIN [product=" + product + "]";
	}

	public Item getItem() {
		return this.getProduct().getItem();
	}
	
	@JsonIgnoreProperties(ignoreUnknown = true)
	public static class Product {
		private Item item;

		public Item getItem() {
			return item;
		}

		@Override
		public String toString() {
			return "Product [item=" + item + "]";
		}
	}
	
	
	
}
