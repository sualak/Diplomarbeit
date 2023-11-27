package at.dertanzbogen.api.domain.main.fixtures;

import at.dertanzbogen.api.domain.main.Address;
import at.dertanzbogen.api.domain.main.Course.Course;
import at.dertanzbogen.api.domain.main.Course.CourseBaseEntity;
import at.dertanzbogen.api.presentation.DTOs.Commands;
import com.github.javafaker.Faker;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.Instant;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import java.util.stream.Stream;

public abstract class CourseFaker {

    public static List<Course> generateCoursesLevel1(int amount) {
        return generateCourseOfType(amount, CourseBaseEntity.CourseType.LEVEL1, CourseBaseEntity.CourseRequirement.LEVEL0);
    }

    public static List<Course> generateCoursesLevel2(int amount) {
        return generateCourseOfType(amount, CourseBaseEntity.CourseType.LEVEL2, CourseBaseEntity.CourseRequirement.LEVEL1);
    }

    public static List<Course> generateCoursesLevel3(int amount) {
        return generateCourseOfType(amount, CourseBaseEntity.CourseType.LEVEL3, CourseBaseEntity.CourseRequirement.LEVEL2);
    }

    public static List<Course> generateCoursesLevel4(int amount) {
        return generateCourseOfType(amount, CourseBaseEntity.CourseType.LEVEL4, CourseBaseEntity.CourseRequirement.LEVEL3);
    }

    public static List<Course> generateCoursesLevel5(int amount) {
        return generateCourseOfType(amount, CourseBaseEntity.CourseType.LEVEL5, CourseBaseEntity.CourseRequirement.LEVEL4);
    }

    public static List<Course> generateCoursesOfAllTypes(int amount) {
        List<Course> courses = new ArrayList<>();
        courses.addAll(generateCoursesLevel1(amount));
        courses.addAll(generateCoursesLevel2(amount));
        courses.addAll(generateCoursesLevel3(amount));
        courses.addAll(generateCoursesLevel4(amount));
        courses.addAll(generateCoursesLevel5(amount));
        return courses;
    }

    public static List<Course> generateCourseOfType(int amount, CourseBaseEntity.CourseType courseType, CourseBaseEntity.CourseRequirement courseRequirementType) {
        Faker courseFaker = new Faker(new Locale("de-AT"), new Random(10000));



        return Stream.generate(() -> new Course.CourseBuilder()
                .setPairCourse(true)
                .setRunTime(courseFaker.number().numberBetween(1, 10))
                .setBookAbleAt(Instant.now())
                .setBookAbleTo(Instant.now().plus(Period.ofWeeks(courseFaker.number().numberBetween(1, 10))))
                .setTheme(courseFaker.name().firstName())
                .setPrice(BigDecimal.valueOf(courseFaker.number().randomDouble(2, 0, 100)))
                .setCourseProgramm(courseFaker.lorem().characters(1, 255))
                .setCourseDescription(courseFaker.lorem().characters(1, 255))
                .setCourseType(courseType)
                .setCourseRequirementType(courseRequirementType)
                .setStartsAt(Instant.now().plus(Period.ofDays(1)).plus(Duration.ofHours(courseFaker.number().numberBetween(1,2))))
                .setEndsAt(Instant.now().plus(Period.ofDays(1)).plus(Duration.ofHours(courseFaker.number().numberBetween(3,4))))
                .setCourseStatus(CourseBaseEntity.CourseStatus.OPEN)
                .setMaxUser(10)
                .setFollowerLeaderBalance(2)
                .setAddress(new Address(
                        courseFaker.address().streetAddress(),
                        Integer.parseInt(courseFaker.address().streetAddressNumber()),
                        Integer.parseInt(courseFaker.address().zipCode()),
                        courseFaker.address().city(),
                        courseFaker.address().country()
                ))
                .build(7)).limit(amount).toList();
    }

    public static List<Commands.CourseCreationCommand> generateCourseCommandOfType(int amount, String courseType, String courseRequirementType) {
        Faker courseFaker = new Faker(new Locale("de-AT"), new Random(10000));

        return Stream.generate(() -> new Commands.CourseCreationCommand(
                Instant.now().plus(Period.ofDays(1)).plus(Duration.ofHours(courseFaker.number().numberBetween(1, 2))).toEpochMilli(),
                Instant.now().plus(Period.ofDays(1)).plus(Duration.ofHours(courseFaker.number().numberBetween(3, 4))).toEpochMilli(),
                courseFaker.bool().bool(),
                100,
                50,
                null,
                courseFaker.address().streetAddress(),
                Integer.parseInt(courseFaker.address().streetAddressNumber()),
                Integer.parseInt(courseFaker.address().zipCode()),
                courseFaker.address().city(),
                courseFaker.address().country(),
                courseRequirementType,
                courseType,
                "OPEN",
                courseFaker.number().numberBetween(1, 10),
                Instant.now().toEpochMilli(),
                Instant.now().plus(Period.ofWeeks(courseFaker.number().numberBetween(1, 10))).toEpochMilli(),
                courseFaker.name().firstName(),
                BigDecimal.valueOf(courseFaker.number().randomDouble(2, 0, 100)),
                7)).limit(amount).toList();
    }
}
