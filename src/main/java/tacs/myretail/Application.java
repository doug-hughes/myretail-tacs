package tacs.myretail;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.event.EventListener;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.index.IndexOperations;
import org.springframework.data.mongodb.core.index.IndexResolver;
import org.springframework.data.mongodb.core.index.MongoPersistentEntityIndexResolver;
import org.springframework.data.mongodb.core.mapping.MongoMappingContext;
import org.springframework.hateoas.config.EnableHypermediaSupport;
import org.springframework.hateoas.config.EnableHypermediaSupport.HypermediaType;
import org.springframework.hateoas.support.WebStack;

import tacs.myretail.model.Price;

@SpringBootApplication
@EnableHypermediaSupport(type= {HypermediaType.COLLECTION_JSON}, stacks= {WebStack.WEBFLUX})
public class Application {
	@Autowired
	private MongoTemplate mongoTemplate;
	@Autowired
	private MongoMappingContext mongoMappingContext;
	
	public static void main(String[] args) {
		@SuppressWarnings("unused")
		ConfigurableApplicationContext ctx = SpringApplication.run(Application.class, args);
//		System.out.printf("Application title is: %s%n", ctx.getBean("appTitle"));
	}
	@EventListener(ApplicationReadyEvent.class)
	public void initIndicesAfterStartup() {

		IndexOperations indexOps = this.mongoTemplate.indexOps(Price.class);

		IndexResolver resolver = new MongoPersistentEntityIndexResolver(mongoMappingContext);
		resolver.resolveIndexFor(Price.class).forEach(indexOps::ensureIndex);

	}

}
