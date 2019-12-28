package tacs.myretail.model.rest;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import jdk.jfr.Name;
/**
 * Parses the search results from the WebClient rest response
 * 
 * 	{
 * 		search_response: {
 * 			. . .
 * 			items: {
 * 				Item: [
 * 				product_description: {
 * 					title: <String>
 * 					tcin:
 * 					price: {
 * 						current_retail_min:   
 * 					. . .
 * 					}
 * 				}
 * 				. . .
 * 			}
 * 			. . . 
 * 		}
 * }
 */

//@JsonIgnoreProperties(ignoreUnknown = true)
public class SearchResponse {
	private Search_Recommendations search_recommendations;
@Override
public String toString() {
	return "SearchResponse [search_recommendations=" + search_recommendations + ", search_response=" + search_response
			+ "]";
}
		private Search_Response search_response;
//	
//	public void setSearch_response(Search_Response search_response) {
//		this.search_response = search_response;
//	}
//
	public Search_Response getSearch_response() {
		return search_response;
	}
	
	public Search_Recommendations getSearch_recommendations() {
		return this.search_recommendations;
	}
//	public void setSearch_Recommendations(Search_Recommendations search_recommendations) {
//		this.search_recommendations = search_recommendations;
//	}
	
	// ************************** search_recommendation is a query string *********************************
	@JsonIgnoreProperties(ignoreUnknown = true)
	public static class Search_Recommendations {

		private String query;

		public void setQuery(String query) {
			this.query = query;
		}

		public String getQuery() {
			return this.query;
		}

		@Override
		public String toString() {
			return "Search_Recommendations [query=" + query + "]";
		}

	}
	
	// ************************** search_response is a list of items *********************************
	@JsonIgnoreProperties(ignoreUnknown = true)
	public static class Search_Response {

		private Items items;

		public Items getItems() {
			return items;
		}
		public void setItems(Items items) {
			this.items = items;
		}
		@Override
		public String toString() {
			return "Search_Response [items=" + items + "]";
		}
	}
	
	// ************************** search_response is a list of items *********************************
	@JsonIgnoreProperties(ignoreUnknown = true)
	public static class Items {
		private List<Item> item;
		@JsonProperty("Item")
		public List<Item> getItem() {
			return item;
		}
//		public void setItems(Item items) {
//			this.items = items;
//		}
	}
	
	// ****************************************** Item ***************************************
	@JsonIgnoreProperties(ignoreUnknown = true)
	public static class Item {
		private long tcin;
		private Price price;
		public long getTcin() {
			return tcin;
		}
		public Price getPrice() {
			return price;
		}
		@Override
		public String toString() {
			return "Item [tcin=" + tcin + ", price=" + price + "]";
		}
	}
	// ****************************************** Price ***************************************
	@JsonIgnoreProperties(ignoreUnknown = true)
	 public static class Price {

		private double current_retail_min;

		public double getCurrent_retail_min() {
			return current_retail_min;
		}
		@Override
		public String toString() {
			return "Price [current_retail_min=" + current_retail_min + "]";
		}
	}
}
