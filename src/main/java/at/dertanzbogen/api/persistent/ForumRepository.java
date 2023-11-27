package at.dertanzbogen.api.persistent;

import at.dertanzbogen.api.domain.main.Course.Course;
import at.dertanzbogen.api.domain.main.Forum.ForumEntry;
import at.dertanzbogen.api.persistent.Generic.GenericSearch;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ForumRepository extends MongoRepository<ForumEntry, String>, GenericSearch {
}
