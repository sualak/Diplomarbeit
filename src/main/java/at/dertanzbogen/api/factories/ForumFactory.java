package at.dertanzbogen.api.factories;

import at.dertanzbogen.api.domain.main.Address;
import at.dertanzbogen.api.domain.main.Course.Course;
import at.dertanzbogen.api.domain.main.Course.CourseBaseEntity;
import at.dertanzbogen.api.domain.main.Forum.ForumEntry;
import at.dertanzbogen.api.domain.main.User.User;
import at.dertanzbogen.api.presentation.DTOs.Commands;

import java.time.Instant;

public class ForumFactory {

    public static ForumEntry of(User user, Commands.ForumEntryCreationCommand forumEntryCreationCommand) {
        return new ForumEntry.ForumEntryBuilder()
                .setCreatorId(user.getId())
                .setCreatorName(user.getPersonal().getFirstName() + " " + user.getPersonal().getLastName())
                .setTitel(forumEntryCreationCommand.title())
                .setQuestion(forumEntryCreationCommand.question())
                .build();
    }
}
