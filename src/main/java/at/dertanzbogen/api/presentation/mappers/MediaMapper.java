package at.dertanzbogen.api.presentation.mappers;

import at.dertanzbogen.api.domain.main.Media;
import at.dertanzbogen.api.presentation.DTOs.Commands;
import org.bson.types.ObjectId;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface MediaMapper
{
    @Mapping(target = "id", source = "id")
    @Mapping(target = "createdAt", expression = "java(java.time.Instant.now())")
    Media toMedia(Commands.UploadMediaCommand mediaMeta, String id);
}
