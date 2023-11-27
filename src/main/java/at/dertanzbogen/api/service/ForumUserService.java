package at.dertanzbogen.api.service;

import at.dertanzbogen.api.domain.main.Forum.Answer;
import at.dertanzbogen.api.domain.main.Forum.ForumEntry;
import at.dertanzbogen.api.domain.main.User.User;
import at.dertanzbogen.api.factories.ForumFactory;
import at.dertanzbogen.api.persistent.ForumRepository;
import at.dertanzbogen.api.presentation.DTOs.Commands;
import at.dertanzbogen.api.presentation.DTOs.Views;
import at.dertanzbogen.api.presentation.mappers.ForumViewMapper;
import at.dertanzbogen.api.presentation.mappers.generic.PageMapper;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class ForumUserService {

    private final ForumRepository forumRepository;
    private static final Logger LOGGER = LoggerFactory.getLogger(ForumUserService.class);
    private static final ForumViewMapper mapper = ForumViewMapper.INSTANCE;
    private static final PageMapper mapperPage = new PageMapper() {};

    public Views.PageDomainXtoPageDTO<Views.ForumView> getAllForumEntries(Pageable pageable) {
        LOGGER.info("Getting Forum page {} from Filter Forum", pageable.getPageNumber());
        return mapperPage.convert(forumRepository.findAll(pageable), mapper);
    }

    public Views.ForumView createForumEntry(User user, Commands.ForumEntryCreationCommand forumEntryCreationCommand) {
        LOGGER.info("Creating Forum entry for user {}", user.getId());
        return mapper.convert(forumRepository.save(ForumFactory.of(user, forumEntryCreationCommand)));
    }

    public void deleteForumEntry(User user, String id) {
        LOGGER.info("Deleting Forum entry for user {}", user.getId());
        ForumEntry forumEntry = forumRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Forum entry not found"));
        if(forumEntry.getCreatorId().equals(user.getId())) {
            forumRepository.delete(forumEntry);
        } else {
            throw new IllegalArgumentException("User is not allowed to delete this forum entry");
        }
    }

    public Views.ForumView updateForumEntry(User user, String id, Commands.ForumEntryCreationCommand forumEntryCreationCommand) {
        LOGGER.info("Updating Forum entry for user {}", user.getId());
        ForumEntry forumEntry = forumRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Forum entry not found"));
        if(forumEntry.getCreatorId().equals(user.getId())) {
            for (var field : forumEntryCreationCommand.getClass().getDeclaredFields()) {
                field.setAccessible(true);
                try {
                    if (field.get(forumEntryCreationCommand) != null && !field.get(forumEntryCreationCommand).toString().isEmpty()) {
                        switch (field.getName()) {
                            case "titel" -> forumEntry.setTitle(forumEntry.getTitle());
                            case "question" -> forumEntry.setQuestion(forumEntryCreationCommand.question());
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            return mapper.convert(forumRepository.save(forumEntry));
        } else {
            throw new IllegalArgumentException("User is not allowed to delete this forum entry");
        }
    }

    public Views.ForumView addAnswerToForumEntry(User user, String id, Commands.AnswerCreationCommand answerCreationCommand) {
        LOGGER.info("Adding answer to Forum entry for user {}", user.getId());
        ForumEntry forumEntry = forumRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Forum entry not found"));
        if(forumEntry.isOpen())
        {
            forumEntry.addAnswer(user.getId(), user.getPersonal().getFirstName() + " " + user.getPersonal().getLastName(), answerCreationCommand.answer());
            return mapper.convert(forumRepository.save(forumEntry));
        }
        else
        {
            throw new IllegalArgumentException("Forum entry is closed");
        }
    }

    public void deleteAnswerFromForumEntry(User user, String id, String answerId) {
        LOGGER.info("Deleting Forum entry for user {}", user.getId());
        ForumEntry forumEntry = forumRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Forum entry not found"));
        Answer answerFound = forumEntry.getAnswers().stream().filter(answer -> answer.getId().equals(answerId)).findFirst().orElseThrow(() -> new IllegalArgumentException("Answer not found"));
        if(answerFound.getCreatorId().equals(user.getId())) {
            forumEntry.deleteAnswer(answerFound.getId());
            forumRepository.save(forumEntry);
        } else {
            throw new IllegalArgumentException("User is not allowed to delete this forum entry");
        }
    }

    public Views.ForumView updateAnswerFromForumEntry(User user, String id, String answerId, Commands.AnswerCreationCommand answerCreationCommand) {
        LOGGER.info("Updating Forum entry for user {}", user.getId());
        ForumEntry forumEntry = forumRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Forum entry not found"));
        Answer answerFound = forumEntry.getAnswers().stream().filter(answer -> answer.getId().equals(answerId)).findFirst().orElseThrow(() -> new IllegalArgumentException("Answer not found"));
        if(answerFound.getCreatorId().equals(user.getId())) {
            for (var field : answerCreationCommand.getClass().getDeclaredFields()) {
                field.setAccessible(true);
                try {
                    if (field.get(answerCreationCommand) != null && !field.get(answerCreationCommand).toString().isEmpty()) {
                        if (field.getName().equals("answer")) {
//                            answerFound.setAnswer(answerCreationCommand.answer());
                            forumEntry.getAnswers().stream().filter(answer -> answer.getId().equals(answerId)).findFirst().get().setAnswer(answerCreationCommand.answer());
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            return mapper.convert(forumRepository.save(forumEntry));
        } else {
            throw new IllegalArgumentException("User is not allowed to delete this forum entry");
        }
    }

    public Views.ForumView closeForumEntry(User user, String id) {
        LOGGER.info("Closing Forum entry for user {}", user.getId());
        ForumEntry forumEntry = forumRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Forum entry not found"));
        if(forumEntry.getCreatorId().equals(user.getId())) {
            forumEntry.changeIsOpen();
            return mapper.convert(forumRepository.save(forumEntry));
        } else {
            throw new IllegalArgumentException("User is not allowed to delete this forum entry");
        }
    }
}
