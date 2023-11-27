package at.dertanzbogen.api.presentation.DTOs;

import at.dertanzbogen.api.domain.main.Address;
import at.dertanzbogen.api.domain.main.Forum.Answer;
import at.dertanzbogen.api.domain.main.Media;
import at.dertanzbogen.api.domain.main.User.Personal;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

public abstract class Views {
    //generic Views
    public record PageDomainXtoPageDTO<T>(int pageNumber, int totalPages, long totalElements, List<T> content) {}

    //login Views
    public record UserLoginView(UserView user,
                                List<PartnerView> partners,
                                PageDomainXtoPageDTO<NotificationUserView> notifications,
                                PageDomainXtoPageDTO<CourseUserView> currentOpenCourses){}

    public record AdminLoginView(UserView user,
                                 PageDomainXtoPageDTO<NotificationUserView> notifications,
                                 PageDomainXtoPageDTO<UserView> userViewPageX,
                                 PageDomainXtoPageDTO<CourseAdminView> currentOpenCourses){}



    public record UserView(String id, Personal personal, String email, String userGroup, boolean isLeader, boolean isHelper) { }

    public record PartnerView(String id, String fullName) {}

    //Course Views
    public record CourseAdminView(String id, CourseView courseView, List<EventAdminView> events,
                                  String courseProgramm, String courseDescription,
                                  List<UserView> teachers, List<UserView> bookedUsers){}

    public record EventAdminView(String id, EventView eventView, List<UserView> teachers, List<UserView> bookedUsers, List<UserView> attendedUsers){}

    public record CourseUserView(String id, CourseView courseView,
                                 List<EventView> events){}


    //Base Views for Course and Event

    public record DrinkView(String id, String name, BigDecimal price){}

    public record CourseView(String id, String theme, Instant startsAt, Instant endsAt,
                             int maxUsers, int followerLeaderBalance, boolean isPairCourse,
                             Address address, String courseType, String courseRequirement,
                             String courseStatus, int runTime, Instant bookAbleAt,
                             Instant bookAbleTo, BigDecimal price, int amountLeader, int amountFollower){}


    public record EventView(String id, Instant startsAt, Instant endsAt,
                            int maxUsers, int followerLeaderBalance, boolean isPairCourse,
                            Address address, String courseType, String courseRequirement, String courseStatus,
                            Media video, int amountLeader, int amountFollower){}

    public record NotificationView(String id, String content,
                                   String notificationType, int acceptAmount,
                                   int acceptedBy){}

    public record NotificationUserView(NotificationView notificationView,
                                       boolean isRead, boolean isAccepted){}

    public record NotificationAdminView(UserView sendBy, NotificationView notificationView, List<UserView> sendTo, List<UserView> readBy, boolean isRead, boolean isAccepted){}

    public record ForumView(String id, String creatorName, String title, String question, List<AnswerView> answers) {
    }

    public record AnswerView(String id, String creatorName, String answer) {
    }
//    public record DummyView(Email email) {}
//    public record DummyLoginView(List<DummyView> dummys) {}
}
