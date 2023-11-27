package at.dertanzbogen.api.presentation;

import at.dertanzbogen.api.persistent.UserRepository;
import at.dertanzbogen.api.presentation.DTOs.Commands;
import at.dertanzbogen.api.presentation.DTOs.Views;
import at.dertanzbogen.api.presentation.mappers.*;
import at.dertanzbogen.api.presentation.mappers.generic.PageMapper;
import at.dertanzbogen.api.service.CourseAdminService;
import at.dertanzbogen.api.service.CourseUserService;
import at.dertanzbogen.api.service.UserAdminService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.net.URI;

@RestController
@RequestMapping("/api/admin/course")
@AllArgsConstructor
public class CourseAdminController {

    private final CourseUserService courseUserService;
    private final UserAdminService userAdminService;
    private final CourseAdminService courseAdminService;
    private final UserRepository userRepository;
    private final Logger logger = LoggerFactory.getLogger(CourseAdminController.class);
    private static final CourseViewMapper COURSE_VIEW_MAPPER = CourseViewMapper.INSTANCE;
    private static final EventViewMapper EVENT_VIEW_MAPPER = EventViewMapper.INSTANCE;
    private static final UserViewMapper USER_VIEW_MAPPER = UserViewMapper.INSTANCE;
    private static final PageMapper mapperPage = new PageMapper() {};
    private static final UploadMediaCommandMapper UPLOAD_MEDIA_COMMAND_MAPPER = UploadMediaCommandMapper.INSTANCE;

    private static final String DEFAULT_PAGE = "0";
    private static final String DEFAULT_SIZE = "50";

    @GetMapping()
    public Views.PageDomainXtoPageDTO<Views.CourseAdminView> getAllCoursesByStatus(@RequestParam(defaultValue = "OPEN") String courseStatus,
                                                                                   @RequestParam(defaultValue = DEFAULT_PAGE) int page,
                                                                                   @RequestParam(defaultValue = DEFAULT_SIZE) int size)
    {
        Pageable pageable = PageRequest.of(page, size);

        return courseAdminService.getAllCoursesByStatus(courseStatus, pageable);
    }

    @PostMapping("/search")
    public Views.PageDomainXtoPageDTO<Views.CourseAdminView> searchCourses(
            @RequestBody Commands.CourseSearchFilterCommand courseSearchFilterCommand,
            @RequestParam(defaultValue = DEFAULT_PAGE) int page,
            @RequestParam(defaultValue = DEFAULT_SIZE) int size)
    {
        logger.info("Getting Course page {} from Filter Courses", page);

        courseSearchFilterLog(courseSearchFilterCommand, page, size);
        Pageable pageable = PageRequest.of(page, size);
        return mapperPage.convert(courseUserService.searchGeneric(courseSearchFilterCommand, pageable), getAdminMapper());
    }

    @PostMapping("/searchUsers")
    public Views.PageDomainXtoPageDTO<Views.UserView> searchUsersWithCourseFilter(
            @RequestBody Commands.CourseSearchFilterCommand courseSearchFilterCommand,
            @RequestParam(defaultValue = DEFAULT_PAGE) int page,
            @RequestParam(defaultValue = DEFAULT_SIZE) int size)
    {
        logger.info("Getting User page {} from Filter Course", page);

        courseSearchFilterLog(courseSearchFilterCommand, page, size);
        Pageable pageable = PageRequest.of(page, size);

        return courseAdminService.usersByCourseFilter(courseSearchFilterCommand, pageable);
    }

    @PostMapping("/create")
    public Views.CourseAdminView createCourse(@Valid @RequestBody Commands.CourseCreationCommand courseCreationCommand) {
        return courseAdminService.createCourse(courseCreationCommand);
    }

    @PostMapping("/update/{id}")
    public Views.CourseAdminView updateCourse(@PathVariable String id, @Valid @RequestBody Commands.CourseUpdateCommand courseUpdateCommand) {
        logger.info("Updating course with id: " + id);
        return courseAdminService.updateCourse(id, courseUpdateCommand);
    }

    @PostMapping("/addEvent/{id}")
    public Views.CourseAdminView addEvent(@PathVariable String id, @Valid @RequestBody Commands.EventCreationCommand eventCreationCommand) {
        logger.info("Adding event to course with id: " + id);
        return courseAdminService.addEvent(id, eventCreationCommand);
    }

    @PostMapping("update/{id}/event/{eventId}")
    public Views.CourseAdminView updateEvent(@PathVariable String id, @PathVariable String eventId, @Valid @RequestBody Commands.EventUpdateCommand eventUpdateCommand) {
        logger.info("Updating event with id: " + eventId + " from course with id: " + id);
        return courseAdminService.updateEvent(id, eventId, eventUpdateCommand);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteCourse(@PathVariable String id) {
        logger.info("Deleting course with id: " + id);
        courseAdminService.deleteCourse(id);
        return ResponseEntity.created(URI.create("delete/"+id)).body("Course "+id+" deleted");
    }

    @DeleteMapping("delete/{id}/event/{eventId}")
    public ResponseEntity<String> deleteEvent(@PathVariable String id, @PathVariable String eventId) {
        logger.info("Deleting event with id: " + eventId + " from course with id: " + id);
        courseAdminService.deleteEvent(id, eventId);
        return ResponseEntity.created(URI.create("delete/"+id+"/event/"+eventId)).body("Event "+eventId+" from course +"+ id+" deleted");
    }

    @PostMapping("/addUserAttended/{id}/event/{eventId}/user/{userId}")
    public Views.CourseAdminView addUserAttended(@PathVariable String id, @PathVariable String eventId, @PathVariable String userId) {
        logger.info("Adding user to event with id: " + eventId + " from course with id: " + id);
        return courseAdminService.addUserAttended(id, eventId, userId);
    }

    @PostMapping(consumes = {MediaType.MULTIPART_FORM_DATA_VALUE}, path = "/addVideo/{id}/event/{eventId}")
    public Views.CourseAdminView addVideoToEvent(@PathVariable String id, @PathVariable String eventId, @Valid @RequestPart Commands.UploadMediaCommand uploadMediaCommand,
    @RequestPart(name = "media", required = false) MultipartFile mediaFile) {
        logger.info("Adding video to event with id: " + eventId + " from course with id: " + id);
        return courseAdminService.addVideoToEvent(id, eventId,uploadMediaCommand, mediaFile);
    }

    @DeleteMapping("/removeVideo/{id}/event/{eventId}")
    public Views.CourseAdminView removeVideoFromEvent(@PathVariable String id, @PathVariable String eventId) {
        logger.info("Adding video to event with id: " + eventId + " from course with id: " + id);
        return courseAdminService.removeVideoFromEvent(id, eventId);
    }

    @PostMapping("/removeUser")
    public Views.CourseAdminView removeUserFromCourse(@RequestParam String userId, @RequestParam String courseId)
    {
        logger.info("Removed user "+ userId + "from Course" + courseId);
        return courseAdminService.removeUserFromCourse(userId, courseId);
    }

    public void courseSearchFilterLog(@RequestBody Commands.CourseSearchFilterCommand courseSearchFilterCommand, @RequestParam(defaultValue = DEFAULT_PAGE) int page, @RequestParam(defaultValue = DEFAULT_SIZE) int size) {
        logger.info("Getting page {} from Filter Courses", page);
        logger.info("Searching courses with name: " + courseSearchFilterCommand.theme() +
                " and courseType: " + courseSearchFilterCommand.courseType() +
                " and courseStatus: " + courseSearchFilterCommand.courseStatus() +
                " and startsAt: " + courseSearchFilterCommand.startsAt() +
                " and endsAt: " + courseSearchFilterCommand.endsAt() +
                " and isPairCourse: " + courseSearchFilterCommand.isPairCourse() +
                " and teacherId: " + courseSearchFilterCommand.teacherId() +
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
                " and userId: " + courseSearchFilterCommand.userId());
    }

    private CourseAdminViewMapper getAdminMapper() {
        return new CourseAdminViewMapper(userRepository, COURSE_VIEW_MAPPER, new EventAdminViewMapper(userRepository, EVENT_VIEW_MAPPER, USER_VIEW_MAPPER) {}, USER_VIEW_MAPPER) {};
    }
}
