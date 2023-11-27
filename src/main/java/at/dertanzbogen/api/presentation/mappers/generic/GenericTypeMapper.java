package at.dertanzbogen.api.presentation.mappers.generic;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface GenericTypeMapper<S, T> {
    T convert(S source);
}

