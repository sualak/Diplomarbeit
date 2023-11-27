package at.dertanzbogen.api.presentation;

import at.dertanzbogen.api.domain.main.User.User;
import at.dertanzbogen.api.presentation.DTOs.Commands;
import at.dertanzbogen.api.presentation.DTOs.Views;
import at.dertanzbogen.api.presentation.annotations.AuthenticationUser;
import at.dertanzbogen.api.presentation.mappers.generic.PageMapper;
import at.dertanzbogen.api.service.ForumUserService;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user/forum")
@AllArgsConstructor
public class ForumUserController {

    private final Logger logger = LoggerFactory.getLogger(ForumUserController.class);
    private static final PageMapper mapperPage = new PageMapper() {};

    private static final String DEFAULT_PAGE = "0";
    private static final String DEFAULT_SIZE = "50";
    private ForumUserService forumUserService;

    @GetMapping()
    public Views.PageDomainXtoPageDTO<Views.ForumView> getAllForumEntries(
            @RequestParam(defaultValue = DEFAULT_PAGE) int page,
            @RequestParam(defaultValue = DEFAULT_SIZE) int size)
    {
        logger.info("Getting Forum page {} from Filter Forum", page);
        Sort sorted = Sort.by("isOpen").descending().and(Sort.by("updatedAt").descending());
        Pageable pageable = PageRequest.of(page, size, sorted);
        return forumUserService.getAllForumEntries(pageable);
    }

    @PostMapping("/create")
    public Views.ForumView createForumEntry(@AuthenticationUser User user, @RequestBody Commands.ForumEntryCreationCommand forumEntryCreationCommand) {
        return forumUserService.createForumEntry(user, forumEntryCreationCommand);
    }

    @DeleteMapping("/delete/{id}")
    public void deleteForumEntry(@AuthenticationUser User user, @PathVariable String id) {
        forumUserService.deleteForumEntry(user, id);
    }

    @PostMapping("/update/{id}")
    public Views.ForumView updateForumEntry(@AuthenticationUser User user, @PathVariable String id, @RequestBody Commands.ForumEntryCreationCommand forumEntryCreationCommand) {
        return forumUserService.updateForumEntry(user, id, forumEntryCreationCommand);
    }

    @PostMapping("/addAnswer/{id}")
    public Views.ForumView addAnswerToForumEntry(@AuthenticationUser User user, @PathVariable String id, @RequestBody Commands.AnswerCreationCommand answerCreationCommand) {
        return forumUserService.addAnswerToForumEntry(user, id, answerCreationCommand);
    }

    @DeleteMapping("/delete/{id}/deleteAnswer/{answerId}")
    public void deleteAnswerFromForumEntry(@AuthenticationUser User user, @PathVariable String id, @PathVariable String answerId) {
        forumUserService.deleteAnswerFromForumEntry(user, id, answerId);
    }

    @PostMapping("/update/{id}/updateAnswer/{answerId}")
    public Views.ForumView updateAnswerFromForumEntry(@AuthenticationUser User user, @PathVariable String id, @PathVariable String answerId, @RequestBody Commands.AnswerCreationCommand answerCreationCommand) {
        return forumUserService.updateAnswerFromForumEntry(user, id, answerId, answerCreationCommand);
    }

    @PostMapping("/changeOpen/{id}")
    public Views.ForumView closeForumEntry(@AuthenticationUser User user, @PathVariable String id) {
        return forumUserService.closeForumEntry(user, id);
    }
}
