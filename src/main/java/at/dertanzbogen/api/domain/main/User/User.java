package at.dertanzbogen.api.domain.main.User;

import at.dertanzbogen.api.domain.main.Account;
import at.dertanzbogen.api.domain.main.BaseEntity;
import at.dertanzbogen.api.domain.main.Course.Course;
import at.dertanzbogen.api.domain.main.Course.CourseBaseEntity;
import at.dertanzbogen.api.domain.main.Media;
import at.dertanzbogen.api.domain.main.Notification;
import at.dertanzbogen.api.domain.main.Tables.CompletedCourse;
import at.dertanzbogen.api.domain.validation.Ensure;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.*;
/**
 * User class
 *
 */
@Document(collection = "user")
@NoArgsConstructor
@ToString
@TypeAlias("user")
public class User extends BaseEntity {
    private Personal personal = new Personal();
    @Indexed(unique = true)
    private Email email;
    private Password password;
    private boolean isHelper;
    private boolean isLeader;
    private Partner partner = new Partner(super.getId());
    private Map<String, Integer> drinks = new HashMap<>();
    private Map<Course.CourseType, Integer> countedCourses = new HashMap<>();
    private Set<CompletedCourse> completedCourseList = new HashSet<>();
    private userGroup userGroup;
    private Account account = new Account();

//    public User(String firstName, String lastName, String email, String password, boolean isHelper, boolean isLeader, String mimeType, int size, int with, int height, String fileName, userGroup userGroup) {
//        this.personal = new Personal(firstName, lastName, mimeType, size, with, height, fileName);
////        this.email = new Email(email);
//        this.newEmail = new Email(email);
//        this.password = new Password(password);
//        this.isHelper = isHelper;
//        this.isLeader = isLeader;
//        completedCourses.put(true, new HashSet<>());
//        completedCourses.put(false, new HashSet<>());
//        this.userGroup = userGroup;
//    }
//
//    public User(String firstName, String lastName, String email, String password, boolean isHelper, boolean isLeader, userGroup userGroup) {
//        this.personal = new Personal(firstName, lastName);
////        this.email = new Email(email);
//        this.newEmail = new Email(email);
//        this.password = new Password(password);
//        this.isHelper = isHelper;
//        this.isLeader = isLeader;
//        completedCourses.put(true, new HashSet<>());
//        completedCourses.put(false, new HashSet<>());
//        this.userGroup = userGroup;
//    }

//    public User(User user)
//    {
//        this.personal = user.personal;
//        this.email = user.email;
//        this.newEmail = user.newEmail;
//        this.password = user.password;
//        this.isHelper = user.isHelper;
//        this.isLeader = user.isLeader;
//        this.partner = user.partner;
//        this.drinks = user.drinks;
//        this.countedCourses = user.countedCourses;
//        this.completedCourses = user.completedCourses;
//        this.userGroup = user.userGroup;
//    }

    //getter
    public userGroup getUserGroup() {
        return userGroup;
    }

    public Personal getPersonal() {
        return personal;
    }

    public Email getEmail() {
        return email;
    }

    public Password getPassword() {
        return password;
    }

    public boolean isHelper() {
        return isHelper;
    }

    public boolean isLeader() {
        return isLeader;
    }

    public Partner getPartner() {
        return partner;
    }

    public Map<String, Integer> getDrinks() {
        return Collections.unmodifiableMap(drinks);
    }

    public Map<Course.CourseType, Integer> getCountedCourses() {
        return Collections.unmodifiableMap(countedCourses);
    }

    public Set<CompletedCourse> getCompletedCourses() {
        return Collections.unmodifiableSet(completedCourseList);
    }

    public Account getAccount() {
        return account;
    }

    //setter

    public void setAccount(Account account) {
        this.account = account;
    }

    public void setPersonal(Personal personal) {
        this.personal = personal;
    }

    public void setDrinks(Map<String, Integer> drinks) {
        this.drinks = drinks;
    }

    public void setCountedCourses(Map<Course.CourseType, Integer> countedCourses) {
        this.countedCourses = countedCourses;
    }

    public void setCompletedCourses(Set<CompletedCourse> completedCourses) {
        this.completedCourseList = completedCourses;
    }

    public void setFirstName(String firstName) {
        this.personal.setFirstName(firstName);
    }

    public void setLastName(String lastName) {
        this.personal.setLastName(lastName);
    }

//    public void setProfilePicture(String fileName, String mimeType, int size, int with, int height) {
//        this.personal.setProfilPicture(fileName, mimeType, size, with, height);
//    }

    public void setProfilePicture(Media profilePicture) {
        this.personal.setProfilPicture(profilePicture);
    }

    public void setEmail(Email email) {
        this.email = email;
    }

    public void setPassword(Password password) {
        this.password = password;
    }

    public void setHelper(boolean helper) {
        isHelper = helper;
    }

    public void setLeader(boolean isLeader) {
        this.isLeader = isLeader;
    }

    //TODO: notification text
    public void requestCancelCourse(String userName, String course, String... sendTo) {
        new Notification(super.getId(), 1, "", Notification.NotificationType.CANCEL_REQUEST, sendTo);
    }

    public void addCountedCourse(Course.CourseType courseType, int amount) {
        if (countedCourses.containsKey(courseType)) {
            countedCourses.put(courseType, countedCourses.get(courseType) + Ensure.isPositiv(amount, "Amount"));
        } else {
            countedCourses.put(courseType, Ensure.isPositiv(amount, "Amount"));
        }
    }

    public void reduceCountedCourse(Course.CourseType courseType, int amount) {
        if (countedCourses.containsKey(courseType)) {
            countedCourses.put(courseType, countedCourses.get(courseType) - Ensure.removeCountedCourseValid(countedCourses.get(courseType), amount, "Amount"));
        } else {
            throw new IllegalArgumentException("CourseType not found");
        }
    }

    public User addDrink(String drink, int amount) {
        if (drinks.containsKey(drink)) {
            drinks.put(drink, drinks.get(drink) + Ensure.isPositiv(amount, "Amount"));
        } else {
            drinks.put(drink, Ensure.isPositiv(amount, "Amount"));
        }
        return this;
    }

    public User removeDrink(String drink, int amount) {
        if (drinks.containsKey(drink)) {
            drinks.put(drink, drinks.get(drink) - Ensure.removeDrinkValid(drinks.get(drink), amount, "Amount"));
        } else {
            throw new IllegalArgumentException("Drink not found");
        }
        return this;
    }

    public void addCompletedCourse(CompletedCourse completedCourseId) {
        completedCourseList.add(completedCourseId);
    }

//    public CourseBaseEntity.CourseType getHighestCourseType() {
//        CourseBaseEntity.CourseType highestCourseType = CourseBaseEntity.CourseType.NONE;
//        for (CourseBaseEntity.CourseType courseType : countedCourses.keySet()) {
//            if (countedCourses.get(courseType) >= countedCourses.get(highestCourseType)) {
//                highestCourseType = courseType;
//            }
//        }
//        return highestCourseType;
//    }

    public enum userGroup {
        ADMIN, TEACHER, STUDENT
    }

    public static class UserBuilder {
        private User user;

        public UserBuilder() {
            user = new User();
        }

        public UserBuilder setId(String id) {
            user.setId(id);
            return this;
        }

        public UserBuilder setPersonal(Personal personal) {
            user.setPersonal(personal);
            return this;
        }

        public UserBuilder setFirstName(String firstName) {
            user.setFirstName(firstName);
            return this;
        }

        public UserBuilder setLastName(String lastName) {
            user.setLastName(lastName);
            return this;
        }

//        public UserBuilder setProfilePicture(String fileName, String mimeType, int size, int with, int height) {
//            user.setProfilePicture(fileName, mimeType, size, with, height);
//            return this;
//        }

        public UserBuilder setProfilePicture(Media profilePicture) {
            user.setProfilePicture(profilePicture);
            return this;
        }

//        public UserBuilder setEmail(Email email) {
//            user.email = email;
//            return this;
//        }

        public UserBuilder setEmail(String email) {
            user.email = new Email(email);
            return this;
        }


        public UserBuilder setPassword(String password) {
            user.password = new Password(password);
            return this;
        }

        public UserBuilder setHelper(boolean isHelper) {
            user.isHelper = isHelper;
            return this;
        }

        public UserBuilder setLeader(boolean isLeader) {
            user.isLeader = isLeader;
            return this;
        }

        public UserBuilder setPartner(String partner) {
            user.partner = new Partner(partner);
            return this;
        }

        public UserBuilder setDrinks(Map<String, Integer> drinks) {
            user.drinks = drinks;
            return this;
        }

        public UserBuilder setCountedCourses(Map<Course.CourseType, Integer> countedCourses) {
            user.countedCourses = countedCourses;
            return this;
        }

        public UserBuilder setCompletedCourses(Set<CompletedCourse> completedCourses){
            user.completedCourseList = completedCourses;
            return this;
        }

        public UserBuilder setUserGroup(userGroup userGroup) {
            user.userGroup = userGroup;
            return this;
        }

        public UserBuilder setAccount(Account account) {
            user.account = account;
            return this;
        }

        public User build() {
            return user;
        }
    }
}
