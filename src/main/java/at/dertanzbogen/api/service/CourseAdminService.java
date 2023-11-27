package at.dertanzbogen.api.service;

import at.dertanzbogen.api.domain.main.Course.Course;
import at.dertanzbogen.api.domain.main.Course.CourseBaseEntity;
import at.dertanzbogen.api.domain.main.Course.Event;
import at.dertanzbogen.api.domain.main.Media;
import at.dertanzbogen.api.domain.main.error.CourseNotFoundException;
import at.dertanzbogen.api.domain.main.error.EventNotFoundException;
import at.dertanzbogen.api.email.EmailService;
import at.dertanzbogen.api.factories.CourseFactory;
import at.dertanzbogen.api.persistent.CourseRepository;
import at.dertanzbogen.api.persistent.UserRepository;
import at.dertanzbogen.api.presentation.DTOs.Commands;
import at.dertanzbogen.api.presentation.DTOs.Views;
import at.dertanzbogen.api.presentation.mappers.*;
import at.dertanzbogen.api.presentation.mappers.generic.PageMapper;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.Instant;
import java.util.*;

@Service
@AllArgsConstructor
public class CourseAdminService {

    private final CourseRepository courseRepository;
    private final EmailService emailService;
    private final UserRepository userRepository;
    private final MediaService mediaService;
    private static final Logger LOGGER = LoggerFactory.getLogger(CourseAdminService.class);
    private static final CourseViewMapper COURSE_VIEW_MAPPER = CourseViewMapper.INSTANCE;
    private static final EventViewMapper EVENT_VIEW_MAPPER = EventViewMapper.INSTANCE;
    private static final UserViewMapper USER_VIEW_MAPPER = UserViewMapper.INSTANCE;
    private static final PageMapper mapperPage = new PageMapper() {};

    public Views.PageDomainXtoPageDTO<Views.CourseAdminView> getAllCoursesByStatus(String courseStatus, Pageable pageable)
    {
        LOGGER.info("getting all courses by Status"+ courseStatus);
        return mapperPage.convert(courseRepository.findCoursesByCourseStatus(CourseBaseEntity.CourseStatus.valueOf(courseStatus), pageable), getAdminMapper());
    }

    public List<Course> searchGenericBookedList(Commands.CourseSearchFilterCommand courseSearchFilterCommand) {
        LOGGER.info("Searching for courses with Query");
        Query query = new Query();
        List<Criteria> criteria = new ArrayList<>();
        if (CourseUserService.courseSearchHelper(courseSearchFilterCommand, query, criteria)) return null;
        query.fields().include("booked");
        return courseRepository.searchGenericAll(query, Course.class);
    }

    public Views.CourseAdminView createCourse(Commands.CourseCreationCommand courseCreationCommand) {
        LOGGER.info("Creating new course");
        return getAdminMapper().convert(courseRepository.save(CourseFactory.of(courseCreationCommand)));
    }

    public Views.PageDomainXtoPageDTO<Views.UserView> usersByCourseFilter(Commands.CourseSearchFilterCommand courseSearchFilterCommand, Pageable pageable) {
        LOGGER.info("Getting Users by Course Filter");
        Set<String> userIDs = new HashSet<>();
        searchGenericBookedList(courseSearchFilterCommand).forEach(course -> course.getBooked().forEach(booked -> userIDs.add(booked.getUserID())));
        return mapperPage.convert(userRepository.findAllByIdIn(userIDs, pageable), USER_VIEW_MAPPER);
    }

    public Views.PageDomainXtoPageDTO<Views.CourseAdminView> getCoursesByCoursesStatus(String courseStatus, Pageable pageable) {
        LOGGER.info("Getting Open Courses");
        return mapperPage.convert(courseRepository.findCoursesByCourseStatus(CourseBaseEntity.CourseStatus.valueOf(courseStatus) , pageable), getAdminMapper());
    }

    public void deleteEvent(String id, String eventId) {
        LOGGER.info("Deleting Event with id: " + eventId);
        courseRepository.findById(id).ifPresent(course -> {
            course.getEvents().removeIf(event -> event.getId().equals(eventId));
            courseRepository.save(course);
        });
    }

    public void deleteCourse(String id) {
        LOGGER.info("Deleting Course with id: " + id);
        courseRepository.deleteById(id);
    }

    public Views.CourseAdminView updateCourse(String id, Commands.CourseUpdateCommand courseUpdateCommand) {
        LOGGER.info("Updating Course with id: " + id);
        Course course = courseRepository.findById(id).orElseThrow(() -> new CourseNotFoundException(id));
        boolean isCourseChanged = false;
        for (var field : courseUpdateCommand.getClass().getDeclaredFields()) {
            field.setAccessible(true);
            try {
                if (field.get(courseUpdateCommand) != null && !field.get(courseUpdateCommand).toString().isEmpty()) {
                    switch (field.getName()) {
                        case "startsAt" -> course.setStartsAt(Instant.ofEpochMilli(courseUpdateCommand.startsAt()));
                        case "endsAt" -> course.setEndsAt(Instant.ofEpochMilli(courseUpdateCommand.endsAt()));
                        case "isPairCourse" -> course.setPairCourse(courseUpdateCommand.isPairCourse());
                        case "street" -> course.getAddress().setStreet(courseUpdateCommand.street());
                        case "houseNumber" -> course.getAddress().setHouseNumber(courseUpdateCommand.houseNumber());
                        case "zip" -> course.getAddress().setZip(courseUpdateCommand.zip());
                        case "city" -> course.getAddress().setCity(courseUpdateCommand.city());
                        case "country" -> course.getAddress().setCountry(courseUpdateCommand.country());
                        case "courseRequirement" -> course.setCourseRequirement(CourseBaseEntity.CourseRequirement.valueOf(courseUpdateCommand.courseRequirement()));
                        case "courseType" -> course.setCourseType(CourseBaseEntity.CourseType.valueOf(courseUpdateCommand.courseType()));
                        case "courseStatus" -> course.setCourseStatus(CourseBaseEntity.CourseStatus.valueOf(courseUpdateCommand.courseStatus()));
                        case "runTime" -> course.setRunTime(courseUpdateCommand.runTime());
                        case "bookAbleAt" -> course.setBookAbleAt(Instant.ofEpochMilli(courseUpdateCommand.bookAbleAt()));
                        case "bookAbleTo" -> course.setBookAbleTo(Instant.ofEpochMilli(courseUpdateCommand.bookAbleTo()));
                        case "theme" -> course.setTheme(courseUpdateCommand.theme());
                        case "price" -> course.setPrice(courseUpdateCommand.price());
                        case "followerLeaderBalance" -> course.setFollowerLeaderBalance(courseUpdateCommand.followerLeaderBalance());
                        case "maxUsers" -> course.setMaxUsers(courseUpdateCommand.maxUsers());
                        case "teachers" -> {
                            course.clearTeachers();
                            courseUpdateCommand.teachers().forEach(course::addTeacher);
                        }
                    }
                    isCourseChanged = true;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if(isCourseChanged) {
            course.getEvents().clear();
            course.createEventOncePerDay();
            courseRepository.save(course);
        }
        return getAdminMapper().convert(courseRepository.save(course));
    }

    public Views.CourseAdminView updateEvent(String id, String eventId, Commands.EventUpdateCommand eventUpdateCommand) {
        LOGGER.info("Updating Event with id: " + eventId);
        Course course = courseRepository.findById(id).orElseThrow(() -> new CourseNotFoundException(id));
        Event event = course.getEvents().stream().filter(event1 -> event1.getId().equals(eventId)).findFirst().orElseThrow(() -> new EventNotFoundException(eventId));
        for (var field : eventUpdateCommand.getClass().getDeclaredFields()) {
            field.setAccessible(true);
            try {
                if (field.get(eventUpdateCommand) != null && !field.get(eventUpdateCommand).toString().isEmpty()) {
                    switch (field.getName()) {
                        case "startsAt" -> event.setStartsAt(Instant.ofEpochMilli(eventUpdateCommand.startsAt()));
                        case "endsAt" -> event.setEndsAt(Instant.ofEpochMilli(eventUpdateCommand.endsAt()));
                        case "isPairCourse" -> event.setPairCourse(eventUpdateCommand.isPairCourse());
                        case "teachers" -> {
                            event.clearTeachers();
                            eventUpdateCommand.teachers().forEach(event::addTeacher);
                        }
                        case "street" -> event.getAddress().setStreet(eventUpdateCommand.street());
                        case "houseNumber" -> event.getAddress().setHouseNumber(eventUpdateCommand.houseNumber());
                        case "zip" -> event.getAddress().setZip(eventUpdateCommand.zip());
                        case "city" -> event.getAddress().setCity(eventUpdateCommand.city());
                        case "country" -> event.getAddress().setCountry(eventUpdateCommand.country());
                        case "courseRequirement" -> event.setCourseRequirement(CourseBaseEntity.CourseRequirement.valueOf(eventUpdateCommand.courseRequirement()));
                        case "courseType" -> event.setCourseType(CourseBaseEntity.CourseType.valueOf(eventUpdateCommand.courseType()));
                        case "courseStatus" -> event.setCourseStatus(CourseBaseEntity.CourseStatus.valueOf(eventUpdateCommand.courseStatus()));
                        case "maxUsers" -> event.setMaxUsers(eventUpdateCommand.maxUsers());
                        case "followerLeaderBalance" -> event.setFollowerLeaderBalance(eventUpdateCommand.followerLeaderBalance());
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return getAdminMapper().convert(courseRepository.save(course));
    }

    public Views.CourseAdminView addVideoToEvent(String id, String eventId, Commands.UploadMediaCommand uploadMediaCommand, MultipartFile mediaFiles) {
        LOGGER.info("Adding Video to Event with id: " + eventId);

        Media media = mediaService.saveMedia(mediaFiles, uploadMediaCommand);

        Optional<Course> byId = courseRepository.findById(id);
        byId.ifPresent(course -> {
            course.getEvents().stream().filter(event -> event.getId().equals(eventId)).toList().stream().findFirst().orElseThrow(() -> new EventNotFoundException(eventId)).setVideo(media);
            courseRepository.save(course);
        });

        return getAdminMapper().convert(byId.orElseThrow(() -> new CourseNotFoundException(id)));
    }

    public Views.CourseAdminView removeVideoFromEvent(String id, String eventId) {
        LOGGER.info("Removing Video from Event with id: " + eventId);
        Optional<Course> byId = courseRepository.findById(id);
        mediaService.deleteMedia(byId.orElseThrow(() -> new CourseNotFoundException(id)).getEvents().stream().filter(event -> event.getId().equals(eventId)).toList().stream().findFirst().orElseThrow(() -> new EventNotFoundException(eventId)).getVideo().id());
        byId.ifPresent(course -> {
            course.getEvents().stream().filter(event -> event.getId().equals(eventId)).toList().stream().findFirst().orElseThrow(() -> new EventNotFoundException(eventId)).setVideo(null);
            courseRepository.save(course);
        });
        return getAdminMapper().convert(byId.orElseThrow(() -> new CourseNotFoundException(id)));
    }

    public Views.CourseAdminView addEvent(String id, Commands.EventCreationCommand eventCreationCommand) {
        LOGGER.info("Adding Event to Course with id: " + id);
        Optional<Course> byId = courseRepository.findById(id);
        byId.orElseThrow(() -> new CourseNotFoundException(id)).addEvent(Instant.ofEpochMilli(eventCreationCommand.startsAt()), Instant.ofEpochMilli(eventCreationCommand.endsAt()));
        byId.ifPresent(courseRepository::save);
        return getAdminMapper().convert(byId.orElseThrow(() -> new CourseNotFoundException(id)));
    }

    public Views.CourseAdminView removeUserFromCourse(String userId, String courseId) {
       Course byId = courseRepository.findById(courseId).orElseThrow(() -> new CourseNotFoundException(courseId));
       byId.getBooked().removeIf(booked -> booked.getUserID().equals(userId));
       byId.getEvents().forEach(event -> event.getBooked().removeIf(booked -> booked.getUserID().equals(userId)));
       return getAdminMapper().convert(courseRepository.save(byId));
    }

    public Views.CourseAdminView addUserAttended(String id, String eventId, String userId) {
        LOGGER.info("Adding User to Event with id: " + eventId);
        Optional<Course> byId = courseRepository.findById(id);
        byId.ifPresent(course -> {
            course.getEvents().stream().filter(event -> event.getId().equals(eventId)).toList().stream().findFirst().orElseThrow(() -> new EventNotFoundException(eventId)).addAttended(userId);
            courseRepository.save(course);
        });
        return getAdminMapper().convert(byId.orElseThrow(() -> new CourseNotFoundException(id)));
    }

    private CourseAdminViewMapper getAdminMapper() {
        return new CourseAdminViewMapper(userRepository, COURSE_VIEW_MAPPER, new EventAdminViewMapper(userRepository, EVENT_VIEW_MAPPER, USER_VIEW_MAPPER) {}, USER_VIEW_MAPPER) {};
    }



}
