package at.dertanzbogen.api.presentation.mappers;

import at.dertanzbogen.api.domain.main.Forum.ForumEntry;
import at.dertanzbogen.api.domain.main.Notification;
import at.dertanzbogen.api.presentation.DTOs.Views;
import at.dertanzbogen.api.presentation.mappers.generic.GenericTypeMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

@Mapper(uses = {AnswerViewMapper.class})
public interface ForumViewMapper extends GenericTypeMapper<ForumEntry, Views.ForumView> {

    ForumViewMapper INSTANCE = Mappers.getMapper(ForumViewMapper.class);

    @Override
    Views.ForumView convert(ForumEntry source);
}
