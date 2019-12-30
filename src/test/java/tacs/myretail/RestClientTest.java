package tacs.myretail;
import org.junit.Test;
import org.springframework.test.web.reactive.server.WebTestClient;

public class RestClientTest {

    private final WebTestClient webClient = WebTestClient.bindToServer().baseUrl("http://localhost:8080").build();

    private WebTestClient getWebClient() {
    	return this.webClient;
    }
    
    @Test
    public void givenItemIdAndPriceExists_ThenReturnSingleProduct() {
    	// Given
    	String id = "26396662";
    	getWebClient().get().uri("/products/{id}", id)
    			.exchange()
    		    .expectStatus().isOk()
    		    .expectHeader().valueEquals("Content-Type", "application/json")
    		    .expectBody().json("{\"id\":26396662,\"name\":\"Exploding Kittens Game\"}");
    }
    
    @Test
    public void givenItemIdAndPriceDoesntExist_ThenReturnSingleProduct() {
    	// Given
    	// When
    	String id = "13860428";
    	
    	//Then
    	getWebClient().get().uri("/products/{id}", id)
    			.exchange()
    		    .expectStatus().isOk()
    		    .expectHeader().valueEquals("Content-Type", "application/json")
    		    .expectBody().json("{\"id\":26396662,\"name\":\"Exploding Kittens Game\"}");
    }
}
