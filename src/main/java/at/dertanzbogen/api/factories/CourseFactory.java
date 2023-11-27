package at.dertanzbogen.api.factories;

import at.dertanzbogen.api.domain.main.Address;
import at.dertanzbogen.api.domain.main.Course.Course;
import at.dertanzbogen.api.domain.main.Course.CourseBaseEntity;
import at.dertanzbogen.api.domain.main.User.User;
import at.dertanzbogen.api.presentation.DTOs.Commands;

import java.time.Instant;

public class CourseFactory {
    public static Course of(Commands.CourseCreationCommand courseCreationCommand) {
        return new Course.CourseBuilder()
                .setStartsAt(Instant.ofEpochMilli(courseCreationCommand.startsAt()))
                .setEndsAt(Instant.ofEpochMilli(courseCreationCommand.endsAt()))
                .setPairCourse(courseCreationCommand.isPairCourse())
                .setMaxUser(courseCreationCommand.maxUsers())
                .setFollowerLeaderBalance(courseCreationCommand.followerLeaderBalance())
                .setTeachers(courseCreationCommand.teachers())
                .setAddress(new Address(courseCreationCommand.street(), courseCreationCommand.houseNumber(), courseCreationCommand.zip(), courseCreationCommand.city(), courseCreationCommand.country()))
                .setCourseRequirementType(CourseBaseEntity.CourseRequirement.valueOf(courseCreationCommand.courseRequirement()))
                .setCourseType(CourseBaseEntity.CourseType.valueOf(courseCreationCommand.courseType()))
                .setCourseStatus(CourseBaseEntity.CourseStatus.valueOf(courseCreationCommand.courseStatus()))
                .setRunTime(courseCreationCommand.runTime())
                .setBookAbleAt(Instant.ofEpochMilli(courseCreationCommand.bookAbleAt()))
                .setBookAbleTo(Instant.ofEpochMilli(courseCreationCommand.bookAbleTo()))
                .setTheme(courseCreationCommand.theme())
                .setPrice(courseCreationCommand.price())
                .build(courseCreationCommand.amountOfDays());
    }
}
