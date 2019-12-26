package tacs.myretail.model.rest;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Item {
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
		return "Item [tcin=" + tcin + ", product_description=" + product_description + "]";
	}
	
	@JsonIgnoreProperties(ignoreUnknown = true)
	public static class TCIN {
		private Product product;

		public Product getProduct() {
			return product;
		}

		@Override
		public String toString() {
			return "TCIN [product=" + product + "]";
		}
	}
	
}
