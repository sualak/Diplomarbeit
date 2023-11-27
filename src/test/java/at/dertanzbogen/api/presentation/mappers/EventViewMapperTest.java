package at.dertanzbogen.api.presentation.mappers;

import at.dertanzbogen.api.Fakers.CourseFaker;
import at.dertanzbogen.api.domain.main.Course.Course;
import at.dertanzbogen.api.domain.main.Course.Event;
import at.dertanzbogen.api.presentation.DTOs.Views;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class EventViewMapperTest {


    @Test
    void convert() {
        List<Course> courses = CourseFaker.generateCoursesLevel1(1);

        Event event = courses.get(0).getEvents().get(0);

        EventViewMapper eventViewMapper = EventViewMapper.INSTANCE;

        Views.EventView convert = eventViewMapper.convert(event);

        assertEquals(event.getId(), convert.id());
        assertEquals(event.getStartsAt(), convert.startsAt());
        assertEquals(event.getEndsAt(), convert.endsAt());
        assertEquals(event.getMaxUsers(), convert.maxUsers());
        assertEquals(event.getFollowerLeaderBalance(), convert.followerLeaderBalance());
        assertEquals(event.isPairCourse(), convert.isPairCourse());
        assertEquals(event.getAddress(), convert.address());
        assertEquals(event.getCourseType().name(), convert.courseType());
        assertEquals(event.getCourseRequirement().name(), convert.courseRequirement());
        assertEquals(event.getCourseStatus().name(), convert.courseStatus());
    }
}