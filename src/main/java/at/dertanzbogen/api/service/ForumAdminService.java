package at.dertanzbogen.api.service;

import at.dertanzbogen.api.domain.main.Forum.Answer;
import at.dertanzbogen.api.domain.main.Forum.ForumEntry;
import at.dertanzbogen.api.domain.main.User.User;
import at.dertanzbogen.api.persistent.ForumRepository;
import at.dertanzbogen.api.presentation.DTOs.Views;
import at.dertanzbogen.api.presentation.mappers.ForumViewMapper;
import at.dertanzbogen.api.presentation.mappers.generic.PageMapper;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class ForumAdminService {

    private final ForumRepository forumRepository;
    private static final Logger LOGGER = LoggerFactory.getLogger(ForumAdminService.class);
    private static final ForumViewMapper mapper = ForumViewMapper.INSTANCE;
    private static final PageMapper mapperPage = new PageMapper() {};


    public void deleteAnswerFromForumEntry(User user, String id, String answerId) {
        LOGGER.info("Deleting Forum entry for user {}", user.getId());
        ForumEntry forumEntry = forumRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Forum entry not found"));
        Answer answerFound = forumEntry.getAnswers().stream().filter(answer -> answer.getId().equals(answerId)).findFirst().orElseThrow(() -> new IllegalArgumentException("Answer not found"));
        forumEntry.deleteAnswer(answerFound.getId());
        forumRepository.save(forumEntry);
    }

    public void deleteForumEntry(User user, String id) {
        LOGGER.info("Deleting Forum entry for user {}", user.getId());
        ForumEntry forumEntry = forumRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Forum entry not found"));
        forumRepository.delete(forumEntry);
    }

    public Views.ForumView closeForumEntry(User user, String id) {
        LOGGER.info("Closing Forum entry for user {}", user.getId());
        ForumEntry forumEntry = forumRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Forum entry not found"));
        forumEntry.changeIsOpen();
        return mapper.convert(forumRepository.save(forumEntry));
    }
}
