package tacs.myretail.model.rest;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.ClientResponse;

import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;
public class ItemResponseTest {

	@Test
	public void given200_whenExtractItemResponse_thenValidItem() throws Exception {
		// Given
		String resourceFilename = "RestClientTest-givenItemIdAndPriceNotExist_ThenReturnSingleProduct.json";
		Path path = Paths.get(getClass().getClassLoader().getResource(resourceFilename).toURI());
		ClientResponse cr = ClientResponse.create(HttpStatus.OK).header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE).body(Files.readString(path)).build();
		
		// When
		Mono<ItemResponse> mono = cr.bodyToMono(ItemResponse.class);

		// Then
		StepVerifier.create(mono)
		.assertNext(ir -> validateItemResponse(ir, 13860428, "The Big Lebowski (Blu-ray)"))
		.expectComplete()
		.verify();
	}
	@Test
	public void given404_whenExtractItemResponse_thenEmptyItem() throws Exception {
		// Given
		ClientResponse cr = ClientResponse.create(HttpStatus.NOT_FOUND).header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE).body("{\"product\":{\"item\":{}}}").build();
		
		// When
		Mono<ItemResponse> mono = cr.bodyToMono(ItemResponse.class);

		// Then
		StepVerifier.create(mono)
		.assertNext(ir -> validateItemResponse(ir, 0, ""))
		.expectComplete()
		.verify();
	}
	private static void validateItemResponse(ItemResponse ir, int expectedTcin, String expectedTitle) {
		assertEquals(expectedTcin, ir.getTcin());
		assertEquals(expectedTitle, ir.getTitle());
	}
}
