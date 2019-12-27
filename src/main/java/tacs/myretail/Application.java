package tacs.myretail;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import tacs.myretail.model.Price;
import tacs.myretail.model.PriceRepository;

@SpringBootApplication
public class Application implements CommandLineRunner {
	@Autowired
	private PriceRepository priceRepository;
	
	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		priceRepository.deleteAll();
		Price saved = priceRepository.save(new Price(13860428L, BigDecimal.valueOf(13.49), "USD"));
		System.out.println(saved);
	}

}
