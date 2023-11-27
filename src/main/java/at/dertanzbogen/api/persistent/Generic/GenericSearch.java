package at.dertanzbogen.api.persistent.Generic;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.query.Query;

import java.util.List;

public interface GenericSearch {
    <T> Page<T> searchGeneric(Query query, Class<T> clazz,
                              Pageable pageable);

    <T> List<T> searchGenericAll(Query query, Class<T> clazz);

}
