package tacs.myretail.rs;

import java.net.URISyntaxException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.server.ExposesResourceFor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import reactor.core.publisher.Mono;
import tacs.myretail.model.Price;
import tacs.myretail.model.Product;
import tacs.myretail.model.ProductService;

@RestController()
@ExposesResourceFor(Product.class)
@RequestMapping("/products")
public class ProductController {
	private static final Logger log = LogManager.getLogger();
	
	private final ProductService productService;
	
	@Autowired
	public ProductController(ProductService productService) {
		this.productService = productService;
	}
	
	public ProductService getProductService() {
		return productService;
	}

//	@GetMapping("/products/{id}")
//	public ResponseEntity<Product> getProduct(@PathVariable(name = "id") String tcin) {
//		try {
//			Product product = getProductService().findByTcin(tcin);
//			ResponseEntity<Product> response = ResponseEntity.ok(product);
//			return response;
//		} catch (NoSuchElementException | NumberFormatException notFound) {
//			return new ResponseEntity<>(HttpStatus.NOT_FOUND);			
//		}
//	}
	@GetMapping("/{id}")
	Mono<Product> getProduct(@PathVariable(name = "id") String tcin) {
			return getProductService().findProductByTcin(tcin);
	}
	
	// compliments of https://spring.io/guides/tutorials/rest/
	@PutMapping("/{id}")
	ResponseEntity<Mono<Product>> replaceCurrentPrice(@RequestBody Price newPrice, @PathVariable String id) throws URISyntaxException {
		Mono<Product> updatedProduct = getProductService().replaceCurrentPriceForProduct(newPrice, id);
		return new ResponseEntity<Mono<Product>>(updatedProduct, HttpStatus.CREATED);
	}
	@ExceptionHandler(value = { Exception.class })
	public ResponseEntity<Object> handleException(Exception ex) {
		log.error("handling exception encountered", ex);
		return new ResponseEntity<>(HttpStatus.NOT_FOUND);
	}
}
