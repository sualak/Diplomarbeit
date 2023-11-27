package at.dertanzbogen.api.presentation;

import at.dertanzbogen.api.domain.main.User.User;
import at.dertanzbogen.api.presentation.DTOs.Commands;
import at.dertanzbogen.api.presentation.DTOs.Views;
//import at.dertanzbogen.api.presentation.mappers.UserListToUserViewList;
import at.dertanzbogen.api.presentation.annotations.AuthenticationUser;
import at.dertanzbogen.api.presentation.mappers.generic.PageMapper;
import at.dertanzbogen.api.presentation.mappers.UserViewMapper;
import at.dertanzbogen.api.service.UserAdminService;
import at.dertanzbogen.api.service.UserService;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/api/admin")
@AllArgsConstructor
public class UserAdminController {

    private final UserService userService;
    private final UserAdminService userAdminService;
    private final Logger logger = LoggerFactory.getLogger(UserController.class);
    private static final UserViewMapper mapper = UserViewMapper.INSTANCE;
    private static final PageMapper mapperPage = new PageMapper() {};

    private static final String DEFAULT_PAGE = "0";
    private static final String DEFAULT_SIZE = "50";

    //rename to getData
    @GetMapping("/getData")
    public Views.AdminLoginView login(@AuthenticationUser User user,
                                      @RequestParam(defaultValue = DEFAULT_PAGE) int page,
                                      @RequestParam(defaultValue = DEFAULT_SIZE) int size,
                                      @RequestParam(defaultValue = DEFAULT_PAGE) int notificationPage,
                                      @RequestParam(defaultValue = DEFAULT_SIZE) int notificationSize){
        logger.info("Getting user with email: " + user.getEmail().getEmail());
        Pageable pageable = PageRequest.of(page, size);
        Pageable notificationPageable = PageRequest.of(notificationPage, notificationSize, Sort.by("createdAt").descending());
        return userAdminService.login(user, notificationPageable, pageable);
    }

    @GetMapping()
    public Views.PageDomainXtoPageDTO<Views.UserView> getAllUsers(@RequestParam(defaultValue = DEFAULT_PAGE) int page,
                                                                  @RequestParam(defaultValue = DEFAULT_SIZE) int size) {
        logger.info("Getting page {} from allUsers", page);

        Pageable pageable = PageRequest.of(page, size);

        return mapperPage.convert(userAdminService.getAllUsers(pageable), mapper);
    }

    // /admin/search
    @PostMapping("/search")
    public Views.PageDomainXtoPageDTO<Views.UserView> searchUsers(
            @RequestBody Commands.UserSearchFilterCommand userSearchFilterCommand,
            @RequestParam(defaultValue = DEFAULT_PAGE) int page,
            @RequestParam(defaultValue = DEFAULT_SIZE) int size)
    {
        logger.info("Searching users with firstName: " + userSearchFilterCommand.firstName() +
                " and lastName: " + userSearchFilterCommand.lastName() +
                " and email: " + userSearchFilterCommand.email() +
                " and userGroup: " + userSearchFilterCommand.userGroup() +
                " and leader: " + userSearchFilterCommand.isLeader() +
                " and helper: " + userSearchFilterCommand.isHelper() +
                " and completedCourseByType: " + userSearchFilterCommand.completedCourseByType());
        Pageable pageable = PageRequest.of(page, size);
        return mapperPage.convert(userAdminService.searchGeneric(userSearchFilterCommand, pageable), mapper);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteUserById(@PathVariable("id") String id) {
        logger.info("Deleting user by admin with id: " + id);
        userService.deleteUserById(id);
        return ResponseEntity.ok()
                .body("User deleted with id: " + id);
    }

    // /user/{id}
    @GetMapping("/{id}")
    public Views.UserView getUserById(@PathVariable("id") String id) {
        logger.info("Getting user by admin with id: " + id);
        return userAdminService.getUserByIdAsView(id);
    }

//     /user/email/{email}
    @GetMapping("/email/{email}")
    public Views.UserView getUserByEmail(@PathVariable String email) {
        logger.info("Getting user by admin with email: " + email);
        return userAdminService.getUserByEmailEmail(email);
    }

    @PostMapping("/user/{id}")
    public ResponseEntity<Views.UserView> updateUserById(@PathVariable("id") String id, @RequestBody Commands.UserUpdateCommand user) {
        logger.info("Updating user by admin with id: " + id);
        User userById = userAdminService.getUserById(id);
        return ResponseEntity
                .created(URI.create("/user"))
                .body(userService.updateUser(userById, user));
    }

    @PostMapping("/user/email/{id}")
    public void updateUserEmailRequestById(@PathVariable("id") String id , @RequestBody Commands.UserUpdateEmailCommand emailCommand) {
        logger.info("Updating user by admin with id: " + id);
        User userById = userAdminService.getUserById(id);
        userService.updateUserEmail(userById, emailCommand);
    }


}
