package tacs.myretail.rs;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import tacs.myretail.model.Product;
import tacs.myretail.model.ProductService;

@RestController
public class ProductsRs {
	@Autowired
	private ProductService productService;
	
	@GetMapping("/products/{id}")
	public ResponseEntity<Product> getProduct(@PathVariable(name = "id") String tcin) {
		Product product = productService.findByTcin(tcin);
		ResponseEntity<Product> response = ResponseEntity.ok(product);
		return response;
//		return 
	}
}
