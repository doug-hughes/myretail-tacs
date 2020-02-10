package tacs.myretail.dev;

import java.math.BigDecimal;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Parses the search results from the WebClient rest response
 * 
 * { search_response: { . . . items: { Item: [ product_description: { title:
 * <String> tcin: price: { current_retail_min: . . . } } . . . } . . . } }
 */

//@JsonIgnoreProperties(ignoreUnknown = true)
public class SearchResult {
	@JsonAlias(value = {"search_recommendations"})
	private SearchRecommendations searchRecommendations;
	@JsonAlias(value = {"search_response"})
	private SearchResponse searchResponse;

	public SearchResponse getSearchResponse() {
		return searchResponse;
	}

	public SearchRecommendations getSearchRecommendations() {
		return this.searchRecommendations;
	}

	@Override
	public String toString() {
		return "SearchResponse [search_recommendations=" + searchRecommendations + ", search_response="
				+ searchResponse + "]";
	}

	// ************************** search_recommendation is a query string
	// *********************************
	@JsonIgnoreProperties(ignoreUnknown = true)
	public static class SearchRecommendations {

		private String query;

		public void setQuery(String query) {
			this.query = query;
		}

		public String getQuery() {
			return this.query;
		}

		@Override
		public String toString() {
			return "SearchRecommendations [query=" + query + "]";
		}

	}

	// ************************** search_response is a list of items
	// *********************************
	@JsonIgnoreProperties(ignoreUnknown = true)
	public static class SearchResponse {

		private Items items;

		public Items getItems() {
			return items;
		}

		public void setItems(Items items) {
			this.items = items;
		}

		@Override
		public String toString() {
			return "SearchResponse [items=" + items + "]";
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
			return price.getCurrentRetail();
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
		
		@JsonAlias(value = {"current_retail"})
		private BigDecimal currentRetail;

		public BigDecimal getCurrentRetail() {
			return currentRetail;
		}

		@Override
		public String toString() {
			return "Price [current_retail=" + currentRetail + "]";
		}
	}
}
