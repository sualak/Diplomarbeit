package at.dertanzbogen.api.persistent;

import at.dertanzbogen.api.domain.main.User.Drink;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DrinkRepository extends MongoRepository<Drink, String> {


}
