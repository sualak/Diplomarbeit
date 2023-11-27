package at.dertanzbogen.api.presentation.mappers;

import at.dertanzbogen.api.domain.main.Course.Course;
import at.dertanzbogen.api.domain.main.User.Drink;
import at.dertanzbogen.api.presentation.DTOs.Views;
import at.dertanzbogen.api.presentation.mappers.generic.GenericTypeMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface DrinkViewMapper extends GenericTypeMapper<Drink, Views.DrinkView> {

    DrinkViewMapper INSTANCE = Mappers.getMapper(DrinkViewMapper.class);

    @Override
    Views.DrinkView convert(Drink drink);
}
