package tacs.myretail.model.rest;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
/**
 * TCIN is here to help consume the RESTResponse from the external product rest service providing our data
 *
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class TCIN {
	private Product product;

	public Product getProduct() {
		return product;
	}
}
