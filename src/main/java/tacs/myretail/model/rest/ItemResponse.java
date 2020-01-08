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
@JsonIgnoreProperties(ignoreUnknown = false)
public class ItemResponse {
	private Product product;

	public Product getProduct() {
		return product;
	}

	public Item getItem() {
		return this.getProduct().getItem();
	}
	
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
