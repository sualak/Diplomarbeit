package at.dertanzbogen.api.persistent;

import at.dertanzbogen.api.domain.main.Notification;
import at.dertanzbogen.api.persistent.Generic.GenericSearch;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.util.Assert;

import java.util.List;

public class ForumRepositoryImpl implements GenericSearch {

    private final MongoOperations operations;

    public ForumRepositoryImpl(MongoOperations operations) {
        Assert.notNull(operations, "MongoOperations must not be null!");
        this.operations = operations;
    }

    @Override
    public <T> Page<T> searchGeneric(Query query, Class<T> clazz, Pageable pageable) {
        return PageableExecutionUtils.getPage(
                operations.find(query, clazz),
                pageable,
                () -> operations.count(Query.of(query).limit(-1).skip(-1), Notification.class)
        );
    }

    @Override
    public <T> List<T> searchGenericAll(Query query, Class<T> clazz) {
        return operations.find(query, clazz);
    }
}
