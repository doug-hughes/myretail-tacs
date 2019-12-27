package tacs.myretail.model;

import java.math.BigDecimal;

import org.springframework.stereotype.Repository;
//{"value": 13.49,"currency_code":"USD"}}
@Repository
interface PriceIF {
	BigDecimal getValue();
	String getCurrencyCode();
}
