package at.dertanzbogen.api.presentation.DTOs;

import at.dertanzbogen.api.domain.main.User.Personal;
import at.dertanzbogen.api.foundation.notnull.AllNotNull;

import java.math.BigDecimal;
import java.util.List;

public abstract class Commands {

    //Create Commands

    @AllNotNull public record DrinkCreationCommand(String name, BigDecimal price) {}

    @AllNotNull public record UserRegistrationCommand(Personal personal, String email,
                                                      String password, Boolean isLeader,
                                                      Boolean isHelper, String userGroup) { }

    @AllNotNull public record CourseCreationCommand(long startsAt, long endsAt, Boolean isPairCourse,
                                                    int maxUsers, int followerLeaderBalance, List<String> teachers,
                                                    String street, int houseNumber, int zip, String city, String country,
                                                    String courseRequirement, String courseType, String courseStatus,
                                                    int runTime, long bookAbleAt, long bookAbleTo, String theme,
                                                    BigDecimal price, int amountOfDays){}


    @AllNotNull public record NotificationCreationCommand(List<String> sendToId, String content, String notificationType, int acceptAmount){}

    @AllNotNull public record PartnerRequestCommand(String email){}
    @AllNotNull public record PartnerDeleteCommand(String Id){}
    //Verification Commands
    @AllNotNull public record VerificationCommand(String userId, String tokenId){}

    //Update Commands
    public record UserUpdateCommand(Personal personal, String password, Boolean isLeader, Boolean isHelper){}
    //wird für alle events übernommen
    public record CourseUpdateCommand(Long startsAt, Long endsAt, Boolean isPairCourse,
                                      Integer maxUsers, Integer followerLeaderBalance, List<String> teachers,
                                      String street, Integer houseNumber, Integer zip, String city, String country,
                                      String courseRequirement, String courseType, String courseStatus,
                                      Integer runTime, Long bookAbleAt, Long bookAbleTo, String theme,
                                      BigDecimal price, Integer amountOfDays) {}
    //wird nur für das event übernommen
    public record EventUpdateCommand(Long startsAt, Long endsAt, Boolean isPairCourse,
                                     Integer maxUsers, Integer followerLeaderBalance, List<String> teachers,
                                     String street, Integer houseNumber, Integer zip, String city, String country,
                                     String courseRequirement, String courseType, String courseStatus) {}

    @AllNotNull public record UserUpdateEmailCommand(String email){}

    public record UploadMediaCommand(String fileName, String mimeType, int size, int width, int height) {}

    @AllNotNull public record ForumEntryCreationCommand(String title, String question) {}

    @AllNotNull public record AnswerCreationCommand(String answer) {}

    @AllNotNull public record EventCreationCommand(Long startsAt, Long endsAt) {}

    @AllNotNull public record PairCourseBookCommand(String partnerId, String courseId, boolean isLeader) {}

    //Payment Commands
    @AllNotNull public record buyDrinkCommand(String userId, String drinkId, int amount) {}

    @AllNotNull public record buyCourseCommand(String userId, String courseId, boolean isLeader) {}

    //SearchFilter Commands
    public record CourseSearchFilterCommand(Long startsAt, Long endsAt, Boolean isPairCourse, String teacherId,
                                            String street, Integer houseNumber, Integer zip, String city, String country,
                                            String courseRequirement, String courseType, String courseStatus,
                                            Integer runTime, Long bookAbleAt, Long bookAbleTo, String theme,
                                            BigDecimal price, String userId){}

    public record UserSearchFilterCommand(String firstName, String lastName, String email, String userGroup, Boolean isLeader, Boolean isHelper, String completedCourseByType){}


    public record DrinkUpdateCommand (String id, String name, BigDecimal price){}

}
