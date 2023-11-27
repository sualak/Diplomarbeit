package at.dertanzbogen.api.domain.main.Course;

import at.dertanzbogen.api.domain.main.Address;
import at.dertanzbogen.api.domain.main.Tables.CompletedCourse;
import at.dertanzbogen.api.domain.main.User.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.Period;

import static org.junit.jupiter.api.Assertions.*;

public class CourseTest {

    private Course course;
    private Instant start;
    private Instant end;
    private User userFollower;
    private User userFollower2;
    private User userLeader;
    private User userLeader2;
    private Course courseRequired;
    private User userRequired;
    @BeforeEach
    void beforEach()
    {
        userFollower = new User.UserBuilder()
                .setFirstName("berni")
                .setLastName("piffel")
                .setEmail("bernhard.piffel@gmx.at")
                .setPassword("warcraft12")
                .setHelper(true)
                .setLeader(false)
                .setUserGroup(User.userGroup.STUDENT)
                .build();


        userFollower2 = new User.UserBuilder()
                .setFirstName("berni")
                .setLastName("piffel")
                .setEmail("bernhard.piffel@gmx.at")
                .setPassword("warcraft12")
                .setHelper(true)
                .setLeader(false)
                .setUserGroup(User.userGroup.STUDENT)
                .build();

        userLeader = new User.UserBuilder()
                .setFirstName("berni")
                .setLastName("piffel")
                .setEmail("bernhard.piffel@gmx.at")
                .setPassword("warcraft12")
                .setHelper(true)
                .setLeader(true)
                .setUserGroup(User.userGroup.STUDENT)
                .build();

        userLeader2 = new User.UserBuilder()
                .setFirstName("berni")
                .setLastName("piffel")
                .setEmail("bernhard.piffel@gmx.at")
                .setPassword("warcraft12")
                .setHelper(true)
                .setLeader(true)
                .setUserGroup(User.userGroup.STUDENT)
                .build();

        userRequired = new User.UserBuilder()
                .setFirstName("berni")
                .setLastName("piffel")
                .setEmail("bernhard.piffel@gmx.at")
                .setPassword("warcraft12")
                .setHelper(true)
                .setLeader(true)
                .setUserGroup(User.userGroup.STUDENT)
                .build();


        start = Instant.now().plus(Period.ofDays(1));
        end = start.plus(Period.ofDays(1));
        course = new Course.CourseBuilder()
                .setStartsAt(start)
                .setRunTime(1)
                .setBookAbleAt(start)
                .setTheme("test")
                .setMaxUser(2)
                .setPrice(BigDecimal.valueOf(30))
                .setCourseProgramm("TestProgramm")
                .setCourseDescription("TestDescription")
                .setCourseType(Course.CourseType.COUNTEDCOURSE)
                .setEndsAt(end)
                .setAddress(new Address("Mondweg", 45, 1140, "Wien", "Wien"))
                .setFollowerLeaderBalance(1)
                .setBookAbleTo(start)
                .setPairCourse(true)
                .build(0);

        courseRequired = new Course.CourseBuilder()
                .setStartsAt(start)
                .setRunTime(6)
                .setBookAbleAt(start)
                .setTheme("test")
                .setMaxUser(2)
                .setPrice(BigDecimal.valueOf(30))
                .setCourseProgramm("TestProgramm")
                .setCourseDescription("TestDescription")
                .setCourseType(Course.CourseType.COUNTEDCOURSE)
                .setEndsAt(end)
                .setAddress(new Address("Mondweg", 45, 1140, "Wien", "Wien"))
                .setFollowerLeaderBalance(1)
                .setBookAbleTo(start)
                .setPairCourse(true)
                .setCourseRequirementType(Course.CourseRequirement.LEVEL3)
                .build(0);

        //        userRequired.addCompletedCourse(Course.CourseType.LEVEL3, true);
//        userRequired.addCompletedCourse(Course.CourseType.LEVEL3, true);
//        userRequired.addCompletedCourse(Course.CourseType.LEVEL3, true);
//        userRequired.addCompletedCourse(Course.CourseType.LEVEL3, true);
//        userRequired.addCompletedCourse(Course.CourseType.LEVEL3, true);

        CompletedCourse completedCourse0 = new CompletedCourse(true,course.getTheme(),course.getId(), courseRequired.getId(), CourseBaseEntity.CourseType.LEVEL3);
        CompletedCourse completedCourse1 = new CompletedCourse(true,course.getTheme(),course.getId(), courseRequired.getId(), CourseBaseEntity.CourseType.LEVEL3);
        CompletedCourse completedCourse2 = new CompletedCourse(true,course.getTheme(),course.getId(), courseRequired.getId(), CourseBaseEntity.CourseType.LEVEL3);
        CompletedCourse completedCourse3 = new CompletedCourse(true,course.getTheme(),course.getId(), courseRequired.getId(), CourseBaseEntity.CourseType.LEVEL3);
        CompletedCourse completedCourse4 = new CompletedCourse(true,course.getTheme(),course.getId(), courseRequired.getId(), CourseBaseEntity.CourseType.LEVEL3);
        userRequired.addCompletedCourse(completedCourse0);
        userRequired.addCompletedCourse(completedCourse1);
        userRequired.addCompletedCourse(completedCourse2);
        userRequired.addCompletedCourse(completedCourse3);
        userRequired.addCompletedCourse(completedCourse4);
    }

    @Test
    void setStartsAt()
    {
        assertEquals(start, course.getStartsAt());
        course.setStartsAt(start.plus(Period.ofDays(1)));
        assertEquals(start.plus(Period.ofDays(1)), course.getStartsAt());
    }

    @Test
    void setStartsAtFail()
    {
        assertEquals(start, course.getStartsAt());
        IllegalArgumentException e = assertThrows(IllegalArgumentException.class, () -> course.setStartsAt(Instant.now().minus(Period.ofDays(1))));
        assertEquals("Start muss in der Zukunft sein", e.getMessage());
        assertEquals(start, course.getStartsAt());
    }

    @Test
    void setEndsAt()
    {
        assertEquals(end, course.getEndsAt());
        course.setEndsAt(end.plus(Period.ofDays(1)));
        assertEquals(end.plus(Period.ofDays(1)), course.getEndsAt());
    }

    @Test
    void setEndsAtFail()
    {
        assertEquals(end, course.getEndsAt());
        IllegalArgumentException e = assertThrows(IllegalArgumentException.class, () -> course.setEndsAt(start.minus(Period.ofDays(1))));
        assertEquals("End muss in der Zukunft sein", e.getMessage());
        assertEquals(end, course.getEndsAt());
    }

    @Test
    void setRunTime()
    {
        assertEquals(1, course.getRunTime());
        course.setRunTime(7);
        assertEquals(7, course.getRunTime());
    }

    @Test
    void setRunTimeFail()
    {
        assertEquals(1, course.getRunTime());
        IllegalArgumentException e = assertThrows(IllegalArgumentException.class, () -> course.setRunTime(-1));
        assertEquals("Run time muss größer 0 sein", e.getMessage());
        assertEquals(1, course.getRunTime());
    }

    @Test
    void setBookAbleAt()
    {
        assertEquals(start, course.getBookAbleAt());
        course.setBookAbleAt(start.plus(Period.ofDays(1)));
        assertEquals(start.plus(Period.ofDays(1)), course.getBookAbleAt());
    }

//    @Test
//    void setLocation()
//    {
//        assertEquals("test", course.getLocation());
//        course.setLocation("test2");
//        assertEquals("test2", course.getLocation());
//    }
//
//    @Test
//    void setLocationFail()
//    {
//        assertEquals("test", course.getLocation());
//        IllegalArgumentException e = assertThrows(IllegalArgumentException.class, () -> course.setLocation(""));
//        assertEquals("Location darf nicht leer sein", e.getMessage());
//        assertEquals("test", course.getLocation());
//    }

    @Test
    void setTheme()
    {
        assertEquals("test", course.getTheme());
        course.setTheme("TestProgramm2");
        assertEquals("TestProgramm2", course.getTheme());
    }

    @Test
    void setThemeFail()
    {
        assertEquals("test", course.getTheme());
        IllegalArgumentException e = assertThrows(IllegalArgumentException.class, () -> course.setTheme(""));
        assertEquals("Theme darf nicht leer sein", e.getMessage());
        assertEquals("test", course.getTheme());
    }

    @Test
    void setMaxUser()
    {
        assertEquals(2, course.getMaxUsers());
        course.setMaxUsers(50);
        assertEquals(50, course.getMaxUsers());
        course.setMaxUsers(0);
        assertEquals(0, course.getMaxUsers());
    }

    @Test
    void setMaxUserFail()
    {
        assertEquals(2, course.getMaxUsers());
        IllegalArgumentException e = assertThrows(IllegalArgumentException.class, () -> course.setMaxUsers(-1));
        assertEquals("maxUser muss zwischen 0 und 300 sein.", e.getMessage());
        assertEquals(2, course.getMaxUsers());
        e = assertThrows(IllegalArgumentException.class, () -> course.setMaxUsers(301));
        assertEquals("maxUser muss zwischen 0 und 300 sein.", e.getMessage());
        assertEquals(2, course.getMaxUsers());
    }

    @Test
    void setPrice()
    {
        assertEquals(BigDecimal.valueOf(30), course.getPrice());
        course.setPrice(BigDecimal.valueOf(0));
        assertEquals(BigDecimal.valueOf(0), course.getPrice());
        course.setPrice(BigDecimal.valueOf(100));
        assertEquals(BigDecimal.valueOf(100), course.getPrice());
    }

    @Test
    void setPriceFails()
    {
        assertEquals(BigDecimal.valueOf(30), course.getPrice());
        IllegalArgumentException e = assertThrows(IllegalArgumentException.class, () -> course.setPrice(BigDecimal.valueOf(-1)));
        assertEquals("Price muss zwischen 0,00 und 200,00 sein.", e.getMessage());
        assertEquals(BigDecimal.valueOf(30), course.getPrice());
        e = assertThrows(IllegalArgumentException.class, () -> course.setPrice(BigDecimal.valueOf(201)));
        assertEquals("Price muss zwischen 0,00 und 200,00 sein.", e.getMessage());
        assertEquals(BigDecimal.valueOf(30), course.getPrice());
    }

    @Test
    void createEventEveryXDays()
    {
        assertEquals(1, course.getEvents().size());
        course.removeEvent(course.getEvents().get(0));
        course.setRunTime(6);
        course.createEventEveryXDays(7);
        assertEquals(6, course.getEvents().size());
        assertEquals(start, course.getEvents().get(0).getStartsAt());
        assertEquals(start.plus(Period.ofDays(7)), course.getEvents().get(1).getStartsAt());
        assertEquals(start.plus(Period.ofDays(14)), course.getEvents().get(2).getStartsAt());
        assertEquals(start.plus(Period.ofDays(21)), course.getEvents().get(3).getStartsAt());
        assertEquals(start.plus(Period.ofDays(28)), course.getEvents().get(4).getStartsAt());
        assertEquals(start.plus(Period.ofDays(35)), course.getEvents().get(5).getStartsAt());
    }

    @Test
    void createEventOncePerWeek()
    {
        assertEquals(1, course.getEvents().size());
        course.removeEvent(course.getEvents().get(0));
        course.setRunTime(6);
        course.createEventOncePerWeek();
        assertEquals(6, course.getEvents().size());
        assertEquals(start, course.getEvents().get(0).getStartsAt());
        assertEquals(start.plus(Period.ofDays(7)), course.getEvents().get(1).getStartsAt());
        assertEquals(start.plus(Period.ofDays(14)), course.getEvents().get(2).getStartsAt());
        assertEquals(start.plus(Period.ofDays(21)), course.getEvents().get(3).getStartsAt());
        assertEquals(start.plus(Period.ofDays(28)), course.getEvents().get(4).getStartsAt());
        assertEquals(start.plus(Period.ofDays(35)), course.getEvents().get(5).getStartsAt());
    }

    @Test
    void createEventOncePerDay()
    {
        assertEquals(1, course.getEvents().size());
        course.removeEvent(course.getEvents().get(0));
        course.setRunTime(6);
        course.createEventOncePerDay();
        assertEquals(6, course.getEvents().size());
        assertEquals(start, course.getEvents().get(0).getStartsAt());
        assertEquals(start.plus(Period.ofDays(1)), course.getEvents().get(1).getStartsAt());
        assertEquals(start.plus(Period.ofDays(2)), course.getEvents().get(2).getStartsAt());
        assertEquals(start.plus(Period.ofDays(3)), course.getEvents().get(3).getStartsAt());
        assertEquals(start.plus(Period.ofDays(4)), course.getEvents().get(4).getStartsAt());
        assertEquals(start.plus(Period.ofDays(5)), course.getEvents().get(5).getStartsAt());
        assertEquals(end, course.getEvents().get(0).getEndsAt());
        assertEquals(end.plus(Period.ofDays(1)), course.getEvents().get(1).getEndsAt());
        assertEquals(end.plus(Period.ofDays(2)), course.getEvents().get(2).getEndsAt());
        assertEquals(end.plus(Period.ofDays(3)), course.getEvents().get(3).getEndsAt());
        assertEquals(end.plus(Period.ofDays(4)), course.getEvents().get(4).getEndsAt());
        assertEquals(end.plus(Period.ofDays(5)), course.getEvents().get(5).getEndsAt());
    }

    @Test
    void changeDescription()
    {
        assertEquals("TestDescription", course.getCourseDescription().getDescription());
        course.changeDescription("test2");
        assertEquals("test2", course.getCourseDescription().getDescription());
    }

    @Test
    void changeDescriptionFail()
    {
        assertEquals("TestDescription", course.getCourseDescription().getDescription());
        NullPointerException e = assertThrows(NullPointerException.class, () -> course.changeDescription(null));
        assertEquals("Description darf nicht null sein", e.getMessage());
        assertEquals("TestDescription", course.getCourseDescription().getDescription());
    }

    @Test
    void changeProgram()
    {
        assertEquals("TestProgramm", course.getCourseProgramm().getProgramm());
        course.changeProgramm("test2");
        assertEquals("test2", course.getCourseProgramm().getProgramm());
    }

    @Test
    void changeProgramFail()
    {
        assertEquals("TestProgramm", course.getCourseProgramm().getProgramm());
        NullPointerException e = assertThrows(NullPointerException.class, () -> course.changeProgramm(null));
        assertEquals("Programm darf nicht null sein", e.getMessage());
        assertEquals("TestProgramm", course.getCourseProgramm().getProgramm());
    }


    @Test
    void bookCourse()
    {
        assertEquals(0, course.getBooked().size());
        course.bookCourse(userFollower, userFollower.isLeader());
        assertEquals(1, course.getBooked().stream().filter(booked -> booked.getUserID().equals(userFollower.getId())).toList().size());
        course.createEventOncePerDay();
        course.getEvents().forEach(event -> assertEquals(1, event.getBooked().stream().filter(booked -> booked.getUserID().equals(userFollower.getId())).toList().size()));
        course.bookCourse(userLeader, userLeader.isLeader());
        assertEquals(1, course.getBooked().stream().filter(booked -> booked.getUserID().equals(userLeader.getId())).toList().size());
        assertEquals(2, course.getBooked().size());
        course.getEvents().forEach(event -> assertEquals(1, event.getBooked().stream().filter(booked -> booked.getUserID().equals(userLeader.getId())).toList().size()));
        courseRequired.bookCourse(userRequired, userRequired.isLeader());
    }

    @Test
    void bookCourseFails()
    {
        assertEquals(0, course.getBooked().size());
        course.bookCourse(userFollower, userFollower.isLeader());
        assertEquals(1, course.getBooked().stream().filter(booked -> booked.getUserID().equals(userFollower.getId())).toList().size());
        course.bookCourse(userLeader, userLeader.isLeader());
        assertEquals(1, course.getBooked().stream().filter(booked -> booked.getUserID().equals(userLeader.getId())).toList().size());
        assertEquals(2, course.getBooked().size());
        IllegalArgumentException e = assertThrows(IllegalArgumentException.class, () -> course.bookCourse(userFollower2, userFollower2.isLeader()));
        assertEquals("User muss kleiner sein als 0.", e.getMessage());
        assertEquals(2, course.getBooked().size());
        e = assertThrows(IllegalArgumentException.class, () -> course.bookCourse(userLeader2, userLeader2.isLeader()));
        assertEquals("User muss kleiner sein als 0.", e.getMessage());
        assertEquals(2, course.getBooked().size());
        e = assertThrows(IllegalArgumentException.class, () -> courseRequired.bookCourse(userLeader, userLeader.isLeader()));
        assertEquals("You have to book a LEVEL3 course first", e.getMessage());
    }

    @Test
    void cancelBooking()
    {
        assertEquals(0, course.getBooked().size());
        course.bookCourse(userFollower, userFollower.isLeader());
        assertEquals(1, course.getBooked().stream().filter(booked -> booked.getUserID().equals(userFollower.getId())).toList().size());
        course.bookCourse(userLeader, userLeader.isLeader());
        assertEquals(1, course.getBooked().stream().filter(booked -> booked.getUserID().equals(userLeader.getId())).toList().size());
        assertEquals(2, course.getBooked().size());
        course.removeBooked(userFollower.getId());
        assertEquals(0, course.getBooked().stream().filter(booked -> booked.getUserID().equals(userFollower.getId())).toList().size());
        assertEquals(1, course.getBooked().size());
        course.removeBooked(userLeader.getId());
        assertEquals(0, course.getBooked().stream().filter(booked -> booked.getUserID().equals(userLeader.getId())).toList().size());
        assertEquals(0, course.getBooked().size());
    }

    @Test
    void removeEvent()
    {
        assertEquals(1, course.getEvents().size());
        course.createEventOncePerWeek();
        assertEquals(2, course.getEvents().size());
        course.removeEvent(course.getEvents().get(0));
        assertEquals(1, course.getEvents().size());
        course.removeEvent(course.getEvents().get(0));
        assertEquals(0, course.getEvents().size());
    }

    @Test
    void removeTeacher()
    {
        assertEquals(0, course.getTeachers().size());
        course.addTeacher(userLeader.getId());
        assertEquals(1, course.getTeachers().size());
        course.addTeacher(userFollower.getId());
        assertEquals(2, course.getTeachers().size());
        course.removeTeacher(userLeader.getId());
        assertEquals(1, course.getTeachers().size());
        course.removeTeacher(userFollower.getId());
        assertEquals(0, course.getTeachers().size());
    }
}
