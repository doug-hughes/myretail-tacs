package tacs.myretail.model.rest;

import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
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
	
	public static class Item {
		@JsonAlias(value = {"tcin"})
		private int id;

//		private ProductDescription product_description;
		
		@JsonAnySetter
		private Map<String, Object> otherFields = new HashMap<>();
		
		public int getId() {
			return this.id;
		}
//		public void setProduct_description(ProductDescription product_description) {
//			this.product_description = product_description;
//		}
		@SuppressWarnings("unchecked")
		public String getName() {
			return otherFields.get("product_description") != null ? ((Map<String, Object>)otherFields.get("product_description")).get("title").toString() : "";
		}
	    @JsonAnyGetter
	    public Map<String, Object> getOtherFields() {
		return this.otherFields;
	    }
	}
//	public static class ProductDescription {
//		private String title;
//
//		public String getTitle() {
//			return title;
//		}
//	}
}
