package at.dertanzbogen.api.domain.main.Course;

import at.dertanzbogen.api.domain.main.Address;
import at.dertanzbogen.api.domain.main.Media;
import at.dertanzbogen.api.domain.main.User.User;
import at.dertanzbogen.api.domain.validation.Ensure;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.Period;
import java.util.*;

@NoArgsConstructor
@Document(collection = "course")
@TypeAlias("course")
@ToString
public class Course extends CourseBaseEntity {

    private int runTime;
    private Instant bookAbleAt;
    private Instant bookAbleTo;
    private String theme;
    private BigDecimal price;
    private CourseProgramm courseProgramm = new CourseProgramm();
    private CourseDescription courseDescription = new CourseDescription();
    private List<Event> events = new ArrayList<>();




//    public Course(Instant startsAt, int runTime, Instant bookAbleAt, String theme, int maxUser, BigDecimal price, String courseProgramm, String courseDescription, boolean isPairCourse, CourseType courseType, Instant endsAt, Address address, int followerLeaderBalance, Instant bookAbleTo) {
//        super(startsAt, endsAt, maxUser, isPairCourse, address, followerLeaderBalance);
//        this.runTime = Ensure.isPositiv(runTime, "Run time");
//        this.bookAbleAt = bookAbleAt;
//        this.bookAbleTo = bookAbleTo;
//        this.theme = Ensure.ensureNonNullNonBlankValid(theme, "Theme");
//        this.price = Ensure.coursePriceValid(price, "Price");
//        this.courseProgramm = new CourseProgramm(courseProgramm);
//        this.courseDescription = new CourseDescription(courseDescription);
//        this.courseType = courseType;
//    }

    //getter
    public int getRunTime() {
        return runTime;
    }

    public Instant getBookAbleAt() {
        return bookAbleAt;
    }

    public Instant getBookAbleTo() {
        return bookAbleTo;
    }

    public String getTheme() {
        return theme;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public CourseProgramm getCourseProgramm() {
        return courseProgramm;
    }

    public CourseDescription getCourseDescription() {
        return courseDescription;
    }

    public List<Event> getEvents() {
        return events;
    }

    //setter
    public void setRunTime(int runTime) {
        this.runTime = Ensure.isPositivNoZero(runTime, "Run time");
        
    }

    public void setBookAbleAt(Instant bookAbleAt) {
        this.bookAbleAt = bookAbleAt;
    }

//    public void setBookAbleAt(long bookAbleAt) {
//        this.bookAbleAt = Instant.ofEpochMilli(bookAbleAt);
//    }

    public void setBookAbleTo(Instant bookAbleTo) {
        this.bookAbleTo = bookAbleTo;
        
    }

//    public void setBookAbleTo(long bookAbleTo) {
//        this.bookAbleTo = Instant.ofEpochMilli(bookAbleTo);
//    }

    public void setTheme(String theme) {
        this.theme = Ensure.ensureNonNullNonBlankValid(theme, "Theme");
        
    }

    public void setPrice(BigDecimal price) {
        this.price = Ensure.coursePriceValid(price, "Price");
        
    }

    public void addEvent(Instant startsAt,Instant endsAt, Media video) {
        events.add(new Event.eventBuilder()
                .setStartsAt(startsAt)
                .setEndsAt(endsAt)
                .setMaxUser(getMaxUsers())
                .setPairCourse(isPairCourse())
                .setAddress(getAddress())
                .setFollowerLeaderBalance(getFollowerLeaderBalance())
                .addAllBooked(getBooked())
                .setVideo(video)
                .setCourseType(getCourseType())
                .setCourseStatus(getCourseStatus())
                .setCourseRequirement(getCourseRequirement())
                .addAllTeachers(getTeachers())
                .build());
        
    }

    public void addEvent(Instant startsAt,Instant endsAt) {
        events.add(new Event.eventBuilder()
                .setStartsAt(startsAt)
                .setEndsAt(endsAt)
                .setMaxUser(getMaxUsers())
                .setPairCourse(isPairCourse())
                .setAddress(getAddress())
                .setFollowerLeaderBalance(getFollowerLeaderBalance())
                .addAllBooked(getBooked())
                .setCourseType(getCourseType())
                .setCourseStatus(getCourseStatus())
                .setCourseRequirement(getCourseRequirement())
                .addAllTeachers(getTeachers())
                .build());
    }

    public void removeEvent(Event event) {
        events.remove(event);
        
    }

//    public void bookCourse(String user, boolean isLeader) {
//        if(Instant.now().isBefore(bookAbleAt) && Instant.now().isAfter(bookAbleTo))
//            throw new IllegalArgumentException("Course is not bookable at this time");
//        if(requiredType != null)
//            throw new IllegalArgumentException("You have to book a " + requiredType + " course first");
//
//        addBooked(user, isLeader);
//        events.forEach(event -> event.addBooked(user, isLeader));
//        
//    }
    //                user.getCompletedCourses().stream().filter(completedCourses -> completedCourses.isLeader() == isLeader)
//                        .filter(course -> course.getCourseType().name().equals(getCourseRequirement().name())).toList().size() < 5)

    public void bookCourse(User user, boolean isLeader) {
        if(Instant.now().isBefore(bookAbleAt) && Instant.now().isAfter(bookAbleTo))
            throw new IllegalArgumentException("Course is not bookable at this time");
        if(getCourseRequirement() != null && getCourseRequirement().getLevel() > 0 &&
                user.getCompletedCourses().stream()
                .reduce(0, (integer, completedCourses) -> integer + (completedCourses.isLeader() == isLeader
                        && completedCourses.getCourseType().name().equals(getCourseRequirement().name()) ? 1 : 0), Integer::sum)
                        < ((getCourseRequirement().getLevel() >= 3) ? 5 : 1))
            throw new IllegalArgumentException("You have to book a " + getCourseRequirement() + " course first");
        addBooked(user.getId(), isLeader);
        events.forEach(event -> event.addBooked(user.getId(), isLeader));
    }

    public void bookCourseWithPartner(User user, User partner, boolean isLeader){
        if(Instant.now().isBefore(bookAbleAt) && Instant.now().isAfter(bookAbleTo))
            throw new IllegalArgumentException("Course is not bookable at this time");
        if(getCourseRequirement() != null && getCourseRequirement().getLevel() > 0 &&
                user.getCompletedCourses().stream()
                        .reduce(0, (integer, completedCourses) -> integer + (completedCourses.isLeader() == isLeader
                                && completedCourses.getCourseType().name().equals(getCourseRequirement().name()) ? 1 : 0), Integer::sum)
                        < ((getCourseRequirement().getLevel() >= 3) ? 5 : 1))
            throw new IllegalArgumentException("You have to book a " + getCourseRequirement() + " course first");
        if(getCourseRequirement() != null && getCourseRequirement().getLevel() > 0 &&
                partner.getCompletedCourses().stream()
                        .reduce(0, (integer, completedCourses) -> integer + (completedCourses.isLeader() == isLeader
                                && completedCourses.getCourseType().name().equals(getCourseRequirement().name()) ? 1 : 0), Integer::sum)
                        < ((getCourseRequirement().getLevel() >= 3) ? 5 : 1))
            throw new IllegalArgumentException("Partner has to book a " + getCourseRequirement() + " course first");
        addBookedForChange(user, isLeader);
        addBookedForChange(partner, !isLeader);
        events.forEach(event -> event.addBookedForChange(user, isLeader));
        events.forEach(event -> event.addBookedForChange(partner, !isLeader));
    }

    //logic
    public void createEventEveryXDays(int x) {
        for (int i = 0; i < runTime; i++) {
            addEvent(getStartsAt().plus(Period.ofDays(i*x)),
                    getEndsAt().plus(Period.ofDays(i*x)));
        }
    }
    
    public void createEventEveryXDays(int x, Media... video) {
        for (int i = 0; i < runTime; i++) {
            addEvent(getStartsAt().plus(Period.ofDays(i*x)),getEndsAt().plus(Period.ofDays(i*x)), video[i]);
        }
    }
    
    public void createEventOncePerWeek(Media... video) {
        for (int i = 0; i < runTime; i++) {
            addEvent(getStartsAt().plus(Period.ofWeeks(i)),getEndsAt().plus(Period.ofWeeks(i)), video[i]);
        }
    }

    public void createEventOncePerWeek() {
        for (int i = 0; i < runTime; i++) {
            addEvent(getStartsAt().plus(Period.ofWeeks(i)), getEndsAt().plus(Period.ofWeeks(i)));
        }
    }

    public void createEventOncePerDay() {
        for (int i = 0; i < runTime; i++) {
            addEvent(getStartsAt().plus(Period.ofDays(i)), getEndsAt().plus(Period.ofDays(i)));
        }
    }

    public void createEventOncePerDay(Media... video) {
        for (int i = 0; i < runTime; i++) {
            addEvent(getStartsAt().plus(Period.ofDays(i)), getEndsAt().plus(Period.ofDays(i)), video[i]);
        }
    }

    public void changeDescription(String description) {
        courseDescription.setDescription(description);
        
    }

    public void changeProgramm(String programm) {
        courseProgramm.setProgramm(programm);
        
    }



    public static class CourseBuilder
    {
        private Course course;

        public CourseBuilder()
        {
            course = new Course();
        }

        public CourseBuilder setRunTime(int runTime) {
            course.setRunTime(runTime);
            return this;
        }

        public CourseBuilder setBookAbleAt(Instant bookAbleAt) {
            course.setBookAbleAt(bookAbleAt);
            return this;
        }

        public CourseBuilder setBookAbleAt(long bookAbleAt) {
            course.setBookAbleAt(Instant.ofEpochMilli(bookAbleAt));
            return this;
        }

        public CourseBuilder setBookAbleTo(Instant bookAbleTo) {
            course.setBookAbleTo(bookAbleTo);
            return this;
        }

        public CourseBuilder setBookAbleTo(long bookAbleTo) {
            course.setBookAbleTo(Instant.ofEpochMilli(bookAbleTo));
            return this;
        }

        public CourseBuilder setTheme(String theme) {
            course.setTheme(theme);
            return this;
        }

        public CourseBuilder setTeachers(List<String> teachers) {
            teachers.forEach(course::addTeacher);
            return this;
        }

        public CourseBuilder setPrice(BigDecimal price) {
            course.setPrice(price);
            return this;
        }

        public CourseBuilder setCourseProgramm(String courseProgramm) {
            course.changeProgramm(courseProgramm);
            return this;
        }

        public CourseBuilder setCourseDescription(String courseDescription) {
            course.changeDescription(courseDescription);
            return this;
        }

        public CourseBuilder setCourseType(CourseType courseType) {
            course.setCourseType(courseType);
            return this;
        }

        public CourseBuilder setCourseRequirementType(CourseRequirement requiredType) {
            course.setCourseRequirement(requiredType);
            return this;
        }

        public CourseBuilder setCourseStatus(CourseStatus courseStatus) {
            course.setCourseStatus(courseStatus);
            return this;
        }

        public CourseBuilder setStartsAt(Instant startsAt) {
            course.setStartsAt(startsAt);
            return this;
        }

        public CourseBuilder setStartsAt(long startsAt) {
            course.setStartsAt(Instant.ofEpochMilli(startsAt));
            return this;
        }

        public CourseBuilder setEndsAt(Instant endsAt) {
            course.setEndsAt(endsAt);
            return this;
        }

        public CourseBuilder setEndsAt(long endsAt) {
            course.setEndsAt(Instant.ofEpochMilli(endsAt));
            return this;
        }

        public CourseBuilder setMaxUser(int maxUser) {
            course.setMaxUsers(maxUser);
            return this;
        }

        public CourseBuilder setPairCourse(boolean pairCourse) {
            course.setPairCourse(pairCourse);
            return this;
        }

        public CourseBuilder setFollowerLeaderBalance(int followerLeaderBalance) {
            course.setFollowerLeaderBalance(followerLeaderBalance);
            return this;
        }

        public CourseBuilder setAddress(Address address) {
            course.setAddress(address);
            return this;
        }

        public Course build(int amountOfDays) {
            course.createEventEveryXDays(Ensure.isPositiv(amountOfDays, "amountOfDays"));
            return course;
        }

        public Course build(int amountOfDays, Media... video) {
            course.createEventEveryXDays(Ensure.isPositiv(amountOfDays, "amountOfDays"), video);
            return course;
        }
    }
}
