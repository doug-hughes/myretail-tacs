package tacs.myretail.rs;

import java.util.NoSuchElementException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;

import tacs.myretail.model.Product;
import tacs.myretail.model.ProductService;

@RestController()
public class ProductsRs {
	private static final Logger log = LogManager.getLogger();
	
	@Autowired
	private ProductService productService;
	
	public ProductService getProductService() {
		return productService;
	}

	@GetMapping("/products/{id}")
	public ResponseEntity<Product> getProduct(@PathVariable(name = "id") String tcin) {
		try {
			Product product = getProductService().findByTcin(tcin);
			ResponseEntity<Product> response = ResponseEntity.ok(product);
			return response;
		} catch (NoSuchElementException | NumberFormatException notFound) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);			
		}
	}

	@ExceptionHandler(value = { Exception.class })
	public ResponseEntity<Object> handleException(Exception ex, WebRequest request) {
		log.error(request, ex);
		return new ResponseEntity<>(HttpStatus.NOT_FOUND);
	}
}
