package at.dertanzbogen.api.presentation.mappers;

import at.dertanzbogen.api.domain.main.Course.Booked;
import at.dertanzbogen.api.domain.main.Course.Course;
import at.dertanzbogen.api.presentation.DTOs.Views;
import at.dertanzbogen.api.presentation.mappers.generic.GenericTypeMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

import java.util.Set;
@Mapper
public interface CourseViewMapper extends GenericTypeMapper<Course, Views.CourseView> {


    CourseViewMapper INSTANCE = Mappers.getMapper(CourseViewMapper.class);

    @Mapping(target = "isPairCourse", source = "pairCourse", qualifiedByName = "mapPairCourse")
    @Mapping(target = "amountLeader", source = "booked", qualifiedByName = "amountLeader")
    @Mapping(target = "amountFollower", source = "booked", qualifiedByName = "amountFollower")
    @Override
    Views.CourseView convert(Course course);

    @Named("mapPairCourse")
    default boolean isPairCourse(boolean pairCourse) {
        return pairCourse;
    }

    @Named("amountLeader")
    default int amountLeader(Set<Booked> booked)
    {
        return booked.stream().reduce(0, (integer, bookedValue)
                -> integer + ((bookedValue.isLeader()) ? 1 : 0), Integer::sum);
    }

    @Named("amountFollower")
    default int amountFollower(Set<Booked> booked)
    {
        return booked.stream().reduce(0, (integer, bookedValue)
                -> integer + ((!bookedValue.isLeader()) ? 1 : 0), Integer::sum);
    }
}
