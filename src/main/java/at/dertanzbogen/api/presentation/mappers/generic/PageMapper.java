package at.dertanzbogen.api.presentation.mappers.generic;

import at.dertanzbogen.api.presentation.DTOs.Views;
import at.dertanzbogen.api.presentation.mappers.generic.GenericTypeMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.stream.Collectors;


@Mapper
public abstract class PageMapper {
    public <S, T> Views.PageDomainXtoPageDTO<T> convert(Page<S> sPage,
                                                        GenericTypeMapper<S, T> mapper) {
        List<T> content = sPage.getContent().stream()
                .map(mapper::convert)
                .collect(Collectors.toList());

        return new Views.PageDomainXtoPageDTO<>(
                sPage.getNumber(),
                sPage.getTotalPages(),
                sPage.getTotalElements(),
                content
        );
    }
}


