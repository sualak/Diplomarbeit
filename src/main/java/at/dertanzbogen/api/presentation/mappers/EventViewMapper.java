package at.dertanzbogen.api.presentation.mappers;

import at.dertanzbogen.api.domain.main.Course.Booked;
import at.dertanzbogen.api.domain.main.Course.Event;
import at.dertanzbogen.api.presentation.DTOs.Views;
import at.dertanzbogen.api.presentation.mappers.generic.GenericTypeMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

import java.util.Set;

@Mapper
public interface EventViewMapper extends GenericTypeMapper<Event, Views.EventView> {

    EventViewMapper INSTANCE = Mappers.getMapper(EventViewMapper.class);

    // If the source and target field names are the same, we can omit the @Mapping annotation
    // @Mapping(target = "roles", source = "userRoles")

    // If the source and the target differ in type, we can use a qualifiedByName
    @Mapping(target = "isPairCourse", source = "pairCourse", qualifiedByName = "isPairCourse")
    @Mapping(target = "amountLeader", source = "booked", qualifiedByName = "eventAmountLeader")
    @Mapping(target = "amountFollower", source = "booked", qualifiedByName = "eventAmountFollower")
    @Override
    Views.EventView convert(Event course);

    @Named("isPairCourse")
    default boolean isPairCourse(boolean pairCourse) {
        return pairCourse;
    }

    @Named("eventAmountLeader")
    default int amountLeader(Set<Booked> booked)
    {
        return booked.stream().reduce(0, (integer, bookedValue) -> integer + ((bookedValue.isLeader()) ? 1 : 0), Integer::sum);
    }

    @Named("eventAmountFollower")
    default int amountFollower(Set<Booked> booked)
    {
        return booked.stream().reduce(0, (integer, bookedValue) -> integer + ((!bookedValue.isLeader()) ? 1 : 0), Integer::sum);
    }
}
