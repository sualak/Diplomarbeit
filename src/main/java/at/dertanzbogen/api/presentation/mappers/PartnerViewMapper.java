package at.dertanzbogen.api.presentation.mappers;

import at.dertanzbogen.api.domain.main.User.Personal;
import at.dertanzbogen.api.domain.main.User.User;
import at.dertanzbogen.api.presentation.DTOs.Views;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface PartnerViewMapper {
    PartnerViewMapper INSTANCE =
            Mappers.getMapper(PartnerViewMapper.class);

    @Mapping(target = "id", source = "id")
    @Mapping(target = "fullName", source = "personal", qualifiedByName = "fullName")
    Views.PartnerView convert(User user);

    @Named("fullName")
    default String fullName(Personal personal) {
        return personal.getFullName();
    }
}
