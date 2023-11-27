package at.dertanzbogen.api.presentation.mappers;

import at.dertanzbogen.api.domain.main.Forum.Answer;
import at.dertanzbogen.api.domain.main.Forum.ForumEntry;
import at.dertanzbogen.api.presentation.DTOs.Views;
import at.dertanzbogen.api.presentation.mappers.generic.GenericTypeMapper;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface AnswerViewMapper extends GenericTypeMapper<Answer, Views.AnswerView> {


    AnswerViewMapper INSTANCE = Mappers.getMapper(AnswerViewMapper.class);

    @Override
    Views.AnswerView convert(Answer source);
}
