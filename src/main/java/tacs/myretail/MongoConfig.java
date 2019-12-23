package tacs.myretail;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.AbstractMongoClientConfiguration;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;

@Configuration
public class MongoConfig extends  AbstractMongoClientConfiguration {

	@Override
	public MongoClient mongoClient() {
		return MongoClients.create();
	}

	@Override
	protected String getDatabaseName() {
		return "myretail";
	}
	
/*
 * 	> -----------------------------------------------------------------------------------------
	> @EventListener(ApplicationReadyEvent.class)
	> public void initIndicesAfterStartup() {
	>
	>     IndexOperations indexOps = mongoTemplate.indexOps(DomainType.class);
	>
	>     IndexResolver resolver = new MongoPersistentEntityIndexResolver(mongoMappingContext);
	>     resolver.resolveIndexFor(DomainType.class).forEach(indexOps::ensureIndex);
	> }
	> -----------------------------------------------------------------------------------------

 */
}