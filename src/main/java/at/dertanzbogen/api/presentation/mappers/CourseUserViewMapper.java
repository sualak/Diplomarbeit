package at.dertanzbogen.api.presentation.mappers;

import at.dertanzbogen.api.domain.main.Course.Course;
import at.dertanzbogen.api.presentation.DTOs.Views;
import at.dertanzbogen.api.presentation.mappers.generic.GenericTypeMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper( uses = {EventViewMapper.class, CourseViewMapper.class})
public interface CourseUserViewMapper extends GenericTypeMapper<Course, Views.CourseUserView> {

    CourseUserViewMapper INSTANCE = Mappers.getMapper(CourseUserViewMapper.class);
    @Mapping(target = "id", source = "course.id")
    @Mapping(target = "courseView", source = "course", qualifiedByName = "CourseToCourseView")
    @Mapping(target = "events", source = "course", qualifiedByName = "EventUserToEventUserView")
    @Override
    Views.CourseUserView convert(Course course);

    @Named("CourseToCourseView")
    default Views.CourseView courseToCourseView(Course course) {
        return CourseViewMapper.INSTANCE.convert(course);
    }

    @Named("EventUserToEventUserView")
    default List<Views.EventView> eventToEventUserView(Course course) {
        return course.getEvents().stream().map(EventViewMapper.INSTANCE::convert).toList();
    }

    // If the source and target field names are the same, we can omit the @Mapping annotation
    // @Mapping(target = "roles", source = "userRoles")

    // If the source and the target differ in type, we can use a qualifiedByName
//    @Mapping(target = "courseProgramm", source = "courseProgramm", qualifiedByName = "courseProgramme")
//    @Mapping(target = "courseDescription", source = "courseDescription", qualifiedByName = "courseDescription")
//    @Mapping(target = "isPairCourse", source = "pairCourse", qualifiedByName = "mapPairCourse")
//    @Mapping(target = "amountLeader", source = "booked", qualifiedByName = "amountLeader")
//    @Mapping(target = "amountFollower", source = "booked", qualifiedByName = "amountFollower")
//    @Override
//    Views.CourseUserView convert(Course course);
//
////    @Named("courseProgramme")
////    default String courseProgramme(CourseProgramm courseProgramme) {
////        return courseProgramme.getProgramm();
////    }
////
////    @Named("courseDescription")
////    default String courseDescription(CourseDescription courseDescription) {
////        return courseDescription.getDescription();
////    }
//
//    @Named("mapPairCourse")
//    default boolean isPairCourse(boolean pairCourse) {
//        return pairCourse;
//    }
//
//    @Named("amountLeader")
//    default int amountLeader(Set<Booked> booked)
//    {
//        return booked.stream().reduce(0, (integer, bookedValue) -> integer + ((bookedValue.isLeader()) ? 1 : 0), Integer::sum);
//    }
//
//    @Named("amountFollower")
//    default int amountFollower(Set<Booked> booked)
//    {
//        return booked.stream().reduce(0, (integer, bookedValue) -> integer + ((!bookedValue.isLeader()) ? 1 : 0), Integer::sum);
//    }
}
