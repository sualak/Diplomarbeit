package at.dertanzbogen.api.domain.main.Course;

import at.dertanzbogen.api.domain.main.Address;
import at.dertanzbogen.api.domain.main.BaseEntity;
import at.dertanzbogen.api.domain.main.User.User;
import at.dertanzbogen.api.domain.validation.Ensure;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.index.CompoundIndex;

import java.time.Instant;
import java.util.*;

@NoArgsConstructor
@CompoundIndex(name = "courseType", def = "{'courseType': 1, 'courseStatus': 1, 'booked.userID': 1}")
public abstract class CourseBaseEntity extends BaseEntity {

    private Instant startsAt;
    private Instant endsAt;
    private int maxUsers;
    private int followerLeaderBalance;
    private boolean isPairCourse;
    private Set<Booked> booked = new HashSet<>();
    private Set<String> teachers = new HashSet<>();
    private Address address;
    private CourseType courseType;
    private CourseRequirement courseRequirement;
    private CourseStatus courseStatus;

    public CourseBaseEntity(Instant startsAt, Instant endsAt, int maxUsers, boolean isPairCourse, Address address, int followerLeaderBalance) {
        this.startsAt = Ensure.startsAtValid(startsAt);
        this.endsAt = Ensure.endsAtValid(endsAt, startsAt);
        this.maxUsers = Ensure.maxUserValid(maxUsers, "maxUser");
        this.isPairCourse = isPairCourse;
        this.address = address;
        this.followerLeaderBalance = followerLeaderBalance;
    }

    public Instant getStartsAt() {
        return startsAt;
    }
    public Instant getEndsAt() {
        return endsAt;
    }
    public int getMaxUsers() {
        return maxUsers;
    }
    public boolean isPairCourse() {
        return isPairCourse;
    }
    public Set<Booked> getBooked() {
        return Collections.unmodifiableSet(booked);
    }
    public Set<String> getTeachers() {
        return Collections.unmodifiableSet(teachers);
    }
    public int getFollowerLeaderBalance() {
        return followerLeaderBalance;
    }

    public Address getAddress() {
        return address;
    }

    public CourseType getCourseType() {
        return courseType;
    }

    public CourseRequirement getCourseRequirement() {
        return courseRequirement;
    }

    public CourseStatus getCourseStatus() {
        return courseStatus;
    }

    public void setStartsAt(Instant startsAt) {
        this.startsAt = Ensure.startsAtValid(startsAt);
        
    }

//    public void setStartsAt(long startsAt)
//    {
//        this.startsAt = Instant.ofEpochMilli(startsAt);
//    }

    public void setEndsAt(Instant endsAt) {
        this.endsAt = Ensure.endsAtValid(endsAt, startsAt);
    }

//    public void setEndsAt(long endsAt)
//    {
//        this.endsAt = Instant.ofEpochMilli(endsAt);
//    }

    public void setMaxUsers(int maxUsers) {
        this.maxUsers = Ensure.maxUserValid(maxUsers, "maxUser");
        
    }

    public void setPairCourse(boolean pairCourse) {
        isPairCourse = pairCourse;
        
    }

    public void setCourseType(CourseType courseType) {
        this.courseType = courseType;

    }

    public void setCourseRequirement(CourseRequirement courseRequirement) {
        this.courseRequirement = courseRequirement;

    }

    public void setCourseStatus(CourseStatus courseStatus) {
        this.courseStatus = courseStatus;
    }

    public void addBooked(String user, boolean isLeader) {
        booked.add(new Booked(Ensure.addUserValid(
                user, isPairCourse ? maxUsers / 2 - 1 : maxUsers - 1, "User", booked, followerLeaderBalance), isLeader));
        
    }

    public void addBookedForChange(User user, boolean isLeader) {
        if(this.courseStatus != CourseStatus.OPEN)
            throw new IllegalArgumentException("Event is not Open");
        if(getCourseRequirement() != null && getCourseRequirement().getLevel() > 0 &&
                user.getCompletedCourses().stream()
                        .reduce(0, (integer, completedCourses) -> integer + (completedCourses.isLeader() == isLeader && completedCourses.getCourseType().name().equals(getCourseRequirement().name()) ? 1 : 0), Integer::sum) < ((getCourseRequirement().getLevel() >= 3) ? 5 : 1))
//                user.getCompletedCourses().stream().filter(completedCourses -> completedCourses.isLeader() == isLeader)
//                        .filter(course -> course.getCourseType().name().equals(getCourseRequirement().name())).toList().size() < 5)
            throw new IllegalArgumentException("You have to book a " + getCourseRequirement() + " course first");

        booked.add(new Booked(Ensure.addUserPairValid(
                user.getId(), isPairCourse ? maxUsers / 2 - 1 : maxUsers - 1, "User", booked), isLeader));
    }

    public void addAllBooked(Set<Booked> booked) {
        this.booked.addAll(booked);
    }

    public void removeBooked(String user) {
        booked.removeIf(b -> b.getUserID().equals(user));
    }


    public void addTeacher(String teacher) {
        teachers.add(teacher);
    }

    public void addAllTeachers(Set<String> teachers) {
        teachers.forEach(this::addTeacher);
    }

    public void removeTeacher(String teacher) {
        teachers.remove(teacher);
    }

    public void clearTeachers() {
        teachers.clear();
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public void setAddress(String street, int houseNumber, int zip, String city, String country){
        this.address = new Address(street, houseNumber, zip, city, country);
    }

    public void setFollowerLeaderBalance(int followerLeaderBalance) {
        this.followerLeaderBalance = Ensure.isPositiv(followerLeaderBalance, "followerLeaderBalance");
        
    }

    public enum CourseStatus{
        OPEN, CLOSED, CANCELLED, BOOKABLE
    }

    public enum CourseType {
        LEVEL1,
        LEVEL2,
        LEVEL3,
        LEVEL4,
        LEVEL5,
        //        OPENTRAINING,
//        FOLLOWERSTYLING,
//        WORKOUT,
        COUNTEDCOURSE,
        //        PRIVATECOURSE,
        WORKSHOP,
        EVENT,
        CHECKIN,
    }

    public enum CourseRequirement {
        LEVEL0(0),
        LEVEL1(1),
        LEVEL2(2),
        LEVEL3(3),
        LEVEL4(4),
        LEVEL5(5),
        ;

        private final int level;

        CourseRequirement(int i) {
            this.level = i;
        }

        public int getLevel() {
            return level;
        }
    }
}
