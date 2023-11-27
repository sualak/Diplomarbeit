package at.dertanzbogen.api.presentation.mappers;

import at.dertanzbogen.api.Fakers.CourseFaker;
import at.dertanzbogen.api.domain.main.Course.Course;
import at.dertanzbogen.api.presentation.DTOs.Views;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.Assertions.*;

class CourseUserViewMapperTest {


    @Test
    void convert() {
        List<Course> courses = CourseFaker.generateCoursesLevel1(1);

        Course course = courses.get(0);

        CourseViewMapper courseViewMapper = CourseViewMapper.INSTANCE;

        Views.CourseView convert = courseViewMapper.convert(course);

        assertEquals(course.getId(), convert.id());
        assertEquals(course.getStartsAt(), convert.startsAt());
        assertEquals(course.getEndsAt(), convert.endsAt());
        assertEquals(course.getMaxUsers(), convert.maxUsers());
        assertEquals(course.getFollowerLeaderBalance(), convert.followerLeaderBalance());
        assertEquals(course.isPairCourse(), convert.isPairCourse());
        assertEquals(course.getAddress(), convert.address());
        assertEquals(course.getCourseType().name(), convert.courseType());
        assertEquals(course.getCourseRequirement().name(), convert.courseRequirement());
        assertEquals(course.getCourseStatus().name(), convert.courseStatus());
    }
}