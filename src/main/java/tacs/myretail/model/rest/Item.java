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
}
