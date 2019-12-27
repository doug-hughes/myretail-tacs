package tacs.myretail.model;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface PriceRepository extends MongoRepository<Price, ObjectId> {
	void deleteAll();
	Price findByTcin(long tcin);
}
