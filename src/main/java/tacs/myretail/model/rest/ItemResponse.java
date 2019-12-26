package tacs.myretail.model.rest;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
/**
 * Parses the item from the WebClient rest response
 */
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
	
	@JsonIgnoreProperties(ignoreUnknown = true)
	public static class Item {
		private long tcin;
		private ProductDescription product_description;
		public long getTcin() {
			return tcin;
		}
		public ProductDescription getProduct_description() {
			return product_description;
		}
		@Override
		public String toString() {
			return String.format("Item [tcin=%d, product_description=%s]%n",tcin, product_description.getTitle());
		}
	}
	@JsonIgnoreProperties(ignoreUnknown = true)
	public static class ProductDescription {
		private String title;

		public String getTitle() {
			return title;
		}
	}
}
