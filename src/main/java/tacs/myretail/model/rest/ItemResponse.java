package tacs.myretail.model.rest;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
/**
 * Parses the item from the WebClient rest response
 * 
 * 	{
 * 		product: {
 * 			. . .
 * 			item: {
 * 				tcin: <String or Long>
 * 				product_description: {
 * 					title: <String>
 * 					. . .
 * 				}
 * 				. . .
 * 			}
 * 			. . . 
 * 		}
 * }
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
	
	public int getTcin() {
		return this.getItem().getTcin();
	}
	
	public String getTitle() {
		return this.getItem().getProduct_description().getTitle();
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
		private int tcin;
		private ProductDescription product_description;
		public int getTcin() {
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
