package at.dertanzbogen.api.service;

import at.dertanzbogen.api.domain.main.Course.Course;
import at.dertanzbogen.api.domain.main.User.User;
import at.dertanzbogen.api.domain.main.error.UserNotFoundException;
import at.dertanzbogen.api.persistent.UserRepository;
import at.dertanzbogen.api.presentation.DTOs.Commands;
import at.dertanzbogen.api.presentation.DTOs.Views;
import at.dertanzbogen.api.presentation.mappers.AdminLoginViewMapper;
import at.dertanzbogen.api.presentation.mappers.generic.PageMapper;
import at.dertanzbogen.api.presentation.mappers.UserViewMapper;
import lombok.AllArgsConstructor;
import org.mapstruct.Named;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.regex.Pattern;

@Service
@AllArgsConstructor
public class UserAdminService {

    private final UserRepository userRepository;
    private final CourseAdminService courseAdminService;
    private final CourseUserService courseUserService;
    private final NotificationUserService notificationUserService;
    private final UserViewMapper userViewMapper = UserViewMapper.INSTANCE;
    private final AdminLoginViewMapper adminLoginViewMapper = AdminLoginViewMapper.INSTANCE;
    private final PageMapper pageMapper = new PageMapper() {};
    private static final Logger LOGGER = LoggerFactory.getLogger(UserAdminService.class);

    public Page<User> getAllUsers(Pageable pageable) {

        LOGGER.info("Get all users with pagination page: {} and size: {}", pageable.getPageNumber(), pageable.getPageSize());
        return userRepository.findAll(pageable);
    }

    public User getUserById(String id) {
        return userRepository.findById(id).orElseThrow(() -> new UserNotFoundException("User not found"));
    }
    public Views.AdminLoginView login(User user, Pageable notificationPageable, Pageable pageable) {
        return adminLoginViewMapper.adminLoginView(user,
                                                    notificationUserService.getAllMyNotifications(user, notificationPageable),
                                                    pageMapper.convert(getAllUsers(pageable), userViewMapper),
                                                    courseAdminService.getCoursesByCoursesStatus("OPEN", pageable));
    }

    public Page<User> searchGeneric(Commands.UserSearchFilterCommand userSearchFilterCommand, Pageable pageable) {

        Query query = new Query().with(pageable);
        List<Criteria> criteria = new ArrayList<>();
        userSearchHelper(userSearchFilterCommand, query, criteria);

        return userRepository.searchGeneric(query, User.class, pageable);
    }

    private boolean userSearchHelper(Commands.UserSearchFilterCommand userSearchFilterCommand, Query query, List<Criteria> criteria) {
        boolean leader = false;
        for (var field : userSearchFilterCommand.getClass().getDeclaredFields()) {
            field.setAccessible(true);
            try {
//                if(Objects.nonNull(field.get(userSearchFilterDTO)));
                if(field.get(userSearchFilterCommand) != null && !field.get(userSearchFilterCommand).toString().isEmpty()){
                    switch (field.getName()) {
                        case "firstName" ->
                                criteria.add(Criteria.where("personal.firstName").regex(Pattern.quote(field.get(userSearchFilterCommand).toString()), "i"));
                        case "lastName" ->
                                criteria.add(Criteria.where("personal.lastName").regex(Pattern.quote(field.get(userSearchFilterCommand).toString()), "i"));
                        case "email" ->
                                criteria.add(Criteria.where("email.email").regex(Pattern.quote(field.get(userSearchFilterCommand).toString()), "i"));
                        case "userGroup" ->
                                criteria.add(Criteria.where("userGroup").is(User.userGroup.valueOf(field.get(userSearchFilterCommand).toString())));
                        case "isLeader" ->
                                criteria.add(Criteria.where("isLeader").is(field.get(userSearchFilterCommand)));
                        case "isHelper" ->
                                criteria.add(Criteria.where("isHelper").is(field.get(userSearchFilterCommand)));
                        case "completedCourseByType" -> {
                            if(Objects.nonNull(userSearchFilterCommand.isLeader())) {
                                criteria.add(Criteria.where(String.format("completedCourses.%b", userSearchFilterCommand.isLeader())).in(Course.CourseType.valueOf(field.get(userSearchFilterCommand).toString())));
                            }
                            else {
                                criteria.add(Criteria.where(String.format("completedCourses.%b", leader)).in(Course.CourseType.valueOf(field.get(userSearchFilterCommand).toString())));
                                criteria.add(Criteria.where(String.format("completedCourses.%b", !leader)).in(Course.CourseType.valueOf(field.get(userSearchFilterCommand).toString())));
                            }
                        }
                    }
                }
            } catch (IllegalArgumentException | IllegalAccessException e) {
                return true;
            }
        }

        if (!criteria.isEmpty()) {
            query.addCriteria(new Criteria().andOperator(criteria.toArray(new Criteria[0])));
        }
        return false;
    }

    public List<User> searchGenericAll(Commands.UserSearchFilterCommand userSearchFilterCommand) {
        Query query = new Query();
        List<Criteria> criteria = new ArrayList<>();
        userSearchHelper(userSearchFilterCommand, query, criteria);

        return userRepository.searchGenericAll(query, User.class);
    }

    public Views.UserView getUserByIdAsView(String id) {
        return userViewMapper.convert(userRepository.findById(id).orElseThrow(() -> new UserNotFoundException("User not found with id: " + id)));
    }

    public Views.UserView getUserByEmailEmail(String email) {
        return userViewMapper.convert(userRepository.findByEmailEmail(email).orElseThrow(() -> new UserNotFoundException("User not found with email: " + email)));
    }
}
