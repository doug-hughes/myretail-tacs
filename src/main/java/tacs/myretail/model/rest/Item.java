package tacs.myretail.model.rest;

import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;

public class Item {
	private static final String PRODUCT_DESCRIPTION = "product_description";
	private static final String TITLE = "title";
	@JsonAlias(value = {"tcin"})
	private int id;

	@JsonAnySetter
	private Map<String, Object> otherFields = new HashMap<>();
	
	public Item() {
		super();
	}
	public Item(int id, String name) {
		this.id = id;
		this.otherFields.put(PRODUCT_DESCRIPTION, Map.of(TITLE, name));
	}
	
	public int getId() {
		return this.id;
	}
	@SuppressWarnings("unchecked")
	public String getName() {
		return otherFields.get(PRODUCT_DESCRIPTION) != null ? ((Map<String, Object>)otherFields.get(PRODUCT_DESCRIPTION)).get(TITLE).toString() : "";
	}
    @JsonAnyGetter
    public Map<String, Object> getOtherFields() {
	return this.otherFields;
    }
}