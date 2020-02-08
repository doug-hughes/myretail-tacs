package tacs.myretail.rs;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import reactor.core.publisher.Mono;
import tacs.myretail.model.Product;
import tacs.myretail.model.ProductService;

@RestController()
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
	public Mono<Product> getProduct(@PathVariable(name = "id") String tcin) {
			return getProductService().findProductByTcin(tcin);
	}

	@ExceptionHandler(value = { Exception.class })
	public ResponseEntity<Object> handleException(Exception ex) {
		log.error("handling exception encountered", ex);
		return new ResponseEntity<>(HttpStatus.NOT_FOUND);
	}
}
