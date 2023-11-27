package at.dertanzbogen.api.presentation;

import at.dertanzbogen.api.domain.main.User.User;
import at.dertanzbogen.api.presentation.DTOs.Commands;
import at.dertanzbogen.api.presentation.DTOs.Views;
import at.dertanzbogen.api.presentation.annotations.AuthenticationUser;
import at.dertanzbogen.api.presentation.mappers.CourseUserViewMapper;
import at.dertanzbogen.api.presentation.mappers.generic.PageMapper;
import at.dertanzbogen.api.service.CourseUserService;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.net.URI;

@RestController
@RequestMapping("/api/user/course")
@AllArgsConstructor
public class CourseUserController {

    private final CourseUserService courseUserService;
    private final Logger logger = LoggerFactory.getLogger(CourseUserController.class);
    private static final CourseUserViewMapper mapper = CourseUserViewMapper.INSTANCE;
    private static final PageMapper mapperPage = new PageMapper() {};

    private static final String DEFAULT_PAGE = "0";
    private static final String DEFAULT_SIZE = "50";

    @GetMapping()
    public Views.PageDomainXtoPageDTO<Views.CourseUserView> getMyBookedCourses(@AuthenticationUser User user,
                                                                               @RequestParam(defaultValue = DEFAULT_PAGE) int page,
                                                                               @RequestParam(defaultValue = DEFAULT_SIZE) int size,
                                                                               @RequestParam(defaultValue = "OPEN") String courseStatus) {
        logger.info("Getting page {} from allUsers", page);
        logger.info("Getting booked courses for user with id: " + user.getId());

        Pageable pageable = PageRequest.of(page, size);

        return courseUserService.getMyBookedCoursesStatus(user, pageable, courseStatus);
    }

    @PostMapping("/search")
    public Views.PageDomainXtoPageDTO<Views.CourseUserView> searchCoursesUser(
            @RequestBody Commands.CourseSearchFilterCommand courseSearchFilterCommand,
            @RequestParam(defaultValue = DEFAULT_PAGE) int page,
            @RequestParam(defaultValue = DEFAULT_SIZE) int size)
    {
        logger.info("Getting page {} from Filter Courses", page);
        logger.info("Searching courses with name: " + courseSearchFilterCommand.theme() +
                " and courseType: " + courseSearchFilterCommand.courseType() +
                " and courseStatus: " + courseSearchFilterCommand.courseStatus() +
                " and startsAt: " + courseSearchFilterCommand.startsAt() +
                " and endsAt: " + courseSearchFilterCommand.endsAt() +
                " and isPairCourse: " + courseSearchFilterCommand.isPairCourse() +
                " and teacherID: " + courseSearchFilterCommand.teacherId() +
                " and street: " + courseSearchFilterCommand.street()+
                " and houseNumber: " + courseSearchFilterCommand.houseNumber() +
                " and zip: " + courseSearchFilterCommand.zip() +
                " and city: " + courseSearchFilterCommand.city() +
                " and country: " + courseSearchFilterCommand.country() +
                " and courseRequirement: " + courseSearchFilterCommand.courseRequirement() +
                " and runTime: " + courseSearchFilterCommand.runTime() +
                " and bookAbleAt: " + courseSearchFilterCommand.bookAbleAt() +
                " and bookAbleTo: " + courseSearchFilterCommand.bookAbleTo() +
                " and price: " + courseSearchFilterCommand.price() +
                " and userID: " + courseSearchFilterCommand.userId());
        Pageable pageable = PageRequest.of(page, size);
        return mapperPage.convert(courseUserService.searchGeneric(courseSearchFilterCommand, pageable), mapper);
    }

    @GetMapping("/getPrice")
    public ResponseEntity<BigDecimal> getPrice(@AuthenticationUser User user, @RequestParam String courseID) {
        logger.info("Getting price for course with id: " + courseID + " for user with id: " + user.getId());
        return ResponseEntity.created(URI.create("/api/user/course/getPrice"))
                .body(courseUserService.getPrice(courseID));
    }

    @PostMapping("/book")
    public ResponseEntity<Views.CourseUserView> bookCourse(@AuthenticationUser User user, @RequestParam String courseID, @RequestParam boolean isLeader) {
        logger.info("Booking course with id: " + courseID + " for user with id: " + user.getId());
        return ResponseEntity.created(URI.create("/api/user/course/book"))
                .body(courseUserService.bookCourse(user, courseID, isLeader));
    }

    @PostMapping("/book/pair")
    public ResponseEntity<Views.CourseUserView> bookCourseAsPair(@AuthenticationUser User user, @RequestBody Commands.PairCourseBookCommand pairCourseBookCommand) {
        logger.info("Booking course with id: " + pairCourseBookCommand.courseId() + " for user with id: " + user.getId() + " with Partner " + pairCourseBookCommand.partnerId() + " as pair");
        return ResponseEntity.created(URI.create("/api/user/course/book/pair"))
                .body(courseUserService.bookCourseAsPair(user, pairCourseBookCommand));
    }

    @PostMapping("/remove")
    public ResponseEntity<Views.CourseUserView> removeFromEvent(@AuthenticationUser User user, @RequestParam String courseID, @RequestParam String eventId) {
        logger.info("Removing course with id: " + courseID + " for user with id: " + user.getId());
        return ResponseEntity.created(URI.create("/api/user/course/remove"))
                .body(courseUserService.removeFromEvent(user, courseID, eventId));
    }

    @PostMapping("/add")
    public ResponseEntity<Views.CourseUserView> addToEvent(@AuthenticationUser User user, @RequestParam String courseID, @RequestParam String eventId, @RequestParam boolean isLeader) {
        logger.info("Adding course with id: " + courseID + " for user with id: " + user.getId());
        return ResponseEntity.created(URI.create("/api/user/course/add"))
                .body(courseUserService.addToEvent(user, courseID, eventId, isLeader));
    }

    //TODO: PAYMENT
}
