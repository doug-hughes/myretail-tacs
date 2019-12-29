package tacs.myretail.model;

import java.util.List;
import java.util.Optional;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface PriceRepository extends MongoRepository<Price, ObjectId> {
	void deleteAll();
	Optional<Price> findByTcin(int tcin);
	List<Price> findAllByTcin(int tcin);
}
