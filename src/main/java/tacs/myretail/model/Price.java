package tacs.myretail.model;

import java.math.BigDecimal;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Document
@JsonIgnoreProperties(value = {"id", "tcin"})
public class Price {
	@Id
	private ObjectId id;
	@Indexed(unique = true)
	private int tcin;

	private BigDecimal value;

    // currencyCode the ISO 4217 code of the currency
	private String currency_code;
	
	public Price(int tcin, BigDecimal value, String currency_code) {
		this.tcin = tcin;
		this.value = value;
		this.currency_code = currency_code;
	}

	public ObjectId getId() {
		return id;
	}

	public void setId(ObjectId id) {
		this.id = id;
	}

	public int getTcin() {
		return tcin;
	}

	public void setTcin(int tcin) {
		this.tcin = tcin;
	}

	public BigDecimal getValue() {
		return value;
	}

	public void setValue(BigDecimal value) {
		this.value = value;
	}

	public String getCurrency_code() {
		return currency_code;
	}

	public void setCurrency_code(String currency_code) {
		this.currency_code = currency_code;
	}

	@Override
	public String toString() {
		return "Price [id=" + id + ", externalId=" + tcin + ", value=" + value + "]";
	}

}
