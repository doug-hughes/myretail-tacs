package tacs.myretail;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;
import org.springframework.data.mapping.context.MappingContext;
import org.springframework.data.mongodb.config.AbstractMongoClientConfiguration;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.index.IndexOperations;
import org.springframework.data.mongodb.core.index.IndexResolver;
import org.springframework.data.mongodb.core.index.MongoPersistentEntityIndexResolver;
import org.springframework.data.mongodb.core.mapping.MongoMappingContext;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;

import tacs.myretail.model.Price;

@Configuration
public class MongoConfig extends AbstractMongoClientConfiguration implements CommandLineRunner {
	@Autowired
	MongoMappingContext mongoMappingContext;
	@Autowired
	MongoTemplate mongoTemplate;

	@Override
	public MongoClient mongoClient() {
		return MongoClients.create();
	}

	@Override
	protected String getDatabaseName() {
		return "myretail";
	}

	@Override
	public void run(String... args) throws Exception {
		// TODO Auto-generated method stub

	}

	@EventListener(ApplicationReadyEvent.class)
	public void initIndicesAfterStartup() {
		IndexOperations indexOps = mongoTemplate.indexOps(Price.class);

		IndexResolver resolver = new MongoPersistentEntityIndexResolver(mongoMappingContext);
		resolver.resolveIndexFor(Price.class).forEach(indexOps::ensureIndex);
	}
}