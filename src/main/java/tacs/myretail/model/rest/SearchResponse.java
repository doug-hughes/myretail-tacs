package tacs.myretail.model.rest;

import java.math.BigDecimal;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Parses the search results from the WebClient rest response
 * 
 * { search_response: { . . . items: { Item: [ product_description: { title:
 * <String> tcin: price: { current_retail_min: . . . } } . . . } . . . } }
 */

//@JsonIgnoreProperties(ignoreUnknown = true)
public class SearchResponse {
	private Search_Recommendations search_recommendations;
	private Search_Response search_response;

	public Search_Response getSearch_response() {
		return search_response;
	}

	public Search_Recommendations getSearch_recommendations() {
		return this.search_recommendations;
	}

	@Override
	public String toString() {
		return "SearchResponse [search_recommendations=" + search_recommendations + ", search_response="
				+ search_response + "]";
	}

	// ************************** search_recommendation is a query string
	// *********************************
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

	// ************************** search_response is a list of items
	// *********************************
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

	// ************************** search_response is a list of items
	// *********************************
	@JsonIgnoreProperties(ignoreUnknown = true)
	public static class Items {
		private List<Item> item;

		@JsonProperty("Item")
		public List<Item> getItem() {
			return item;
		}
	}

	// ****************************************** Item
	// ***************************************
	@JsonIgnoreProperties(ignoreUnknown = true)
	public static class Item {
		private int tcin;
		private Price price;

		public int getTcin() {
			return tcin;
		}

		public Price getPrice() {
			return price;
		}
		public BigDecimal getCurrentRetail() {
			return price.getCurrent_retail();
		}

		@Override
		public String toString() {
			return "Item [tcin=" + tcin + ", price=" + price + "]";
		}
	}

	// ****************************************** Price
	// ***************************************
	@JsonIgnoreProperties(ignoreUnknown = true)
	public static class Price {

		private BigDecimal current_retail;

		public BigDecimal getCurrent_retail() {
			return current_retail;
		}

		@Override
		public String toString() {
			return "Price [current_retail=" + current_retail + "]";
		}
	}
}
