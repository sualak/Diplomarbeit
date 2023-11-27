package at.dertanzbogen.api.domain.main.User;

import at.dertanzbogen.api.domain.main.Address;
import at.dertanzbogen.api.domain.main.Course.Course;
import at.dertanzbogen.api.domain.main.Tables.CompletedCourse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.Period;

import static org.junit.jupiter.api.Assertions.*;

public class UserTest {

    private User userS;
    private User userA;
    private User userT;
    private Drink drink;
    private Course course;
    private Instant start = Instant.now().plus(Period.ofDays(1));
    private Instant end = start.plus(Period.ofDays(2));

    @BeforeEach
    void beforEach()
    {
        userS = new User.UserBuilder()
                .setFirstName("berni")
                .setLastName("piffel")
                .setEmail("bernhard.piffel@gmx.at")
                .setPassword("warcraft12")
                .setHelper(true)
                .setLeader(true)
                .setUserGroup(User.userGroup.STUDENT)
                .build();

        userA = new User.UserBuilder()
                .setFirstName("berni")
                .setLastName("piffel")
                .setEmail("bernhard.piffel@gmx.at")
                .setPassword("warcraft12")
                .setHelper(true)
                .setLeader(true)
                .setUserGroup(User.userGroup.ADMIN)
                .build();

        userT = new User.UserBuilder()
                .setFirstName("berni")
                .setLastName("piffel")
                .setEmail("bernhard.piffel@gmx.at")
                .setPassword("warcraft12")
                .setHelper(true)
                .setLeader(true)
                .setUserGroup(User.userGroup.TEACHER)
                .build();

        drink = new Drink.DrinkBuilder()
                .setName("test")
                .setPrice(BigDecimal.valueOf(3.50))
                .build();

        course = new Course.CourseBuilder()
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
                .build(1);
    }

    @Test
    void setPassword()
    {
        assertEquals("warcraft12", userS.getPassword().getPassword());
        userS.setPassword(new Password("A1234567"));
        assertEquals("A1234567", userS.getPassword().getPassword());
    }

    @Test
    void setEmail()
    {
        assertEquals("bernhard.piffel@gmx.at", userS.getEmail().getEmail());
        userS.setEmail(new Email("bernhard2.piffel@gmx.at"));
        assertEquals("bernhard2.piffel@gmx.at", userS.getEmail().getEmail());
    }


    @Test
    void setDrink()
    {
        assertEquals(0, userS.getDrinks().size());
        userS.addDrink(drink.getId(), 1);
        assertEquals(1, userS.getDrinks().size());
        assertEquals(1, userS.getDrinks().get(drink.getId()));
        userS.removeDrink(drink.getId(), 1);
        assertEquals(0, userS.getDrinks().get(drink.getId()));
    }

    @Test
    void removeDrinkFails()
    {
        userS.addDrink(drink.getId(), 1);
        IllegalArgumentException e = assertThrows(IllegalArgumentException.class, ()-> userS.removeDrink(drink.getId(), 2));
        assertEquals("Amount muss positiv sein",e.getMessage());
        assertEquals(1, userS.getDrinks().get(drink.getId()));
    }

    @Test
    void reduceCountedCourses()
    {
        assertEquals(0, userS.getCountedCourses().size());
        userS.addCountedCourse(course.getCourseType(), 1);
        assertEquals(1, userS.getCountedCourses().size());
        userS.reduceCountedCourse(course.getCourseType(), 1);
        assertEquals(0, userS.getCountedCourses().get(course.getCourseType()));
    }

    @Test
    void reduceCountedCoursesFails()
    {
        assertEquals(0, userS.getCountedCourses().size());
        userS.addCountedCourse(course.getCourseType(), 1);
        assertEquals(1, userS.getCountedCourses().size());
        IllegalArgumentException e = assertThrows(IllegalArgumentException.class, ()-> userS.reduceCountedCourse(course.getCourseType(), 2));
        assertEquals("Amount muss positiv sein",e.getMessage());
        assertEquals(1, userS.getCountedCourses().get(course.getCourseType()));
    }

    @Test
    void addCountedCourses()
    {
        assertEquals(0, userS.getCountedCourses().size());
        userS.addCountedCourse(course.getCourseType(), 1);
        assertEquals(1, userS.getCountedCourses().size());
        assertEquals(1, userS.getCountedCourses().get(course.getCourseType()));
    }

    @Test
    void addCountedCoursesFails()
    {
        assertEquals(0, userS.getCountedCourses().size());
        IllegalArgumentException e = assertThrows(IllegalArgumentException.class, ()-> userS.addCountedCourse(course.getCourseType(), -1));
        assertEquals("Amount muss positiv sein",e.getMessage());
        assertEquals(0, userS.getCountedCourses().size());
    }

    @Test
    void setHelper()
    {
        assertTrue(userS.isHelper());
        userS.setHelper(false);
        assertFalse(userS.isHelper());
    }

    @Test
    void setLeader()
    {
        assertTrue(userS.isLeader());
        userS.setLeader(false);
        assertFalse(userS.isLeader());
    }

    @Test
    void addCompletedCourse()
    {
        CompletedCourse c = new CompletedCourse(userS.isLeader(), course.getTheme(), course.getId(), userS.getId(), course.getCourseType());

        assertEquals(0, userS.getCompletedCourses().size());
        userS.addCompletedCourse(c);
        assertEquals(1, userS.getCompletedCourses().size());
        assertTrue(userS.getCompletedCourses().contains(c));
        userS.setLeader(false);
    }
}
