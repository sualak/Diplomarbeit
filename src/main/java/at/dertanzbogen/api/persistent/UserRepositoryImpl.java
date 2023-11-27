package at.dertanzbogen.api.persistent;

import at.dertanzbogen.api.domain.main.User.User;
import at.dertanzbogen.api.persistent.Generic.GenericSearch;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.util.Assert;

import java.util.List;

public class UserRepositoryImpl implements GenericSearch {

    private final MongoOperations operations;

    public UserRepositoryImpl(MongoOperations operations) {
        Assert.notNull(operations, "MongoOperations must not be null!");
        this.operations = operations;
    }

    public <T> Page<T> searchGeneric(Query query, Class<T> clazz,
                                     Pageable pageable) {
        return PageableExecutionUtils.getPage(
                operations.find(query, clazz),
                pageable,
                () -> operations.count(Query.of(query).limit(-1).skip(-1),
                        User.class)
        );
    }

    @Override
    public <T> List<T> searchGenericAll(Query query, Class<T> clazz) {
        return operations.find(query, clazz);
    }

//    @Override
//    public Page<User> searchUsers(String firstName, String lastName, String email, String userGroup, Boolean leader, Boolean helper, String completedCourseByType, Pageable pageable) {
//
//        Query query = new Query().with(pageable);
//        List<Criteria> criteria = new ArrayList<>();
//
//        if (firstName != null && !firstName.isEmpty()) {
//            criteria.add(Criteria.where("personal.firstName").regex(Pattern.quote(firstName), "i"));
//        }
//        if (lastName != null && !lastName.isEmpty()) {
//            criteria.add(Criteria.where("personal.lastName").regex(Pattern.quote(lastName), "i"));
//        }
//        if (email != null && !email.isEmpty()) {
//            criteria.add(Criteria.where("email.email").regex(Pattern.quote(email), "i"));
//        }
//        if (userGroup != null && !userGroup.isEmpty()) {
//            criteria.add(Criteria.where("userGroup").is(User.userGroup.valueOf(userGroup)));
//        }
//        if (leader != null) {
//            criteria.add(Criteria.where("isLeader").is(leader));
//        }
//        if (helper != null) {
//            criteria.add(Criteria.where("helper").is(helper));
//        }
//        if (completedCourseByType != null) {
//
//            criteria.add(Criteria.where(String.format("completedCourses.%b", leader)).in(Course.CourseType.valueOf(completedCourseByType)));
//        }
//        if (!criteria.isEmpty()) {
//            query.addCriteria(new Criteria().andOperator(criteria.toArray(new Criteria[0])));
//        }
//
//        return PageableExecutionUtils.getPage(
//                operations.find(query, User.class),
//                pageable,
//                () -> operations.count(Query.of(query).limit(-1).skip(-1), User.class)
//        );
//    }
}
