package tacs.myretail.model;

import org.bson.types.ObjectId;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
@Repository
interface PriceIF extends CrudRepository<Price, ObjectId> {

}
