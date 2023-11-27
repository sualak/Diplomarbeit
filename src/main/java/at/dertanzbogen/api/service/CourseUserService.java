package at.dertanzbogen.api.service;

import at.dertanzbogen.api.domain.main.Course.Course;
import at.dertanzbogen.api.domain.main.Course.CourseBaseEntity;
import at.dertanzbogen.api.domain.main.User.User;
import at.dertanzbogen.api.domain.main.error.CourseNotFoundException;
import at.dertanzbogen.api.domain.main.error.UserNotFoundException;
import at.dertanzbogen.api.email.EmailService;
import at.dertanzbogen.api.persistent.CourseRepository;
import at.dertanzbogen.api.persistent.UserRepository;
import at.dertanzbogen.api.presentation.DTOs.Commands;
import at.dertanzbogen.api.presentation.DTOs.Views;
import at.dertanzbogen.api.presentation.mappers.CourseUserViewMapper;
import at.dertanzbogen.api.presentation.mappers.generic.PageMapper;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

@Service
@AllArgsConstructor
public class CourseUserService {

    private final CourseRepository courseRepository;
    private final EmailService emailService;
    private final UserRepository userRepository;
    private final NotificationUserService notificationUserService;
    private static final Logger LOGGER = LoggerFactory.getLogger(CourseUserService.class);
    private static final CourseUserViewMapper mapper = CourseUserViewMapper.INSTANCE;
    private static final PageMapper mapperPage = new PageMapper() {};

    public Views.PageDomainXtoPageDTO<Views.CourseUserView> getMyBookedCoursesStatus(User user,
                                                                                     Pageable pageable,
                                                                                     String courseStatus) {
        LOGGER.info("Getting booked courses for user with id: " + user.getId());
        return mapperPage.convert(courseRepository.findCoursesByCourseStatusAndBookedUserID
                (CourseBaseEntity.CourseStatus.valueOf(courseStatus), user.getId(), pageable), mapper);
    }

    public Views.PageDomainXtoPageDTO<Views.CourseUserView> getAllCourses(Pageable pageable,
                                                                          String courseStatus)
    {
        LOGGER.info("getting all courses by Status"+ courseStatus);
        return mapperPage.convert(courseRepository
                .findCoursesByCourseStatus(CourseBaseEntity.CourseStatus.valueOf(courseStatus),
                        pageable), mapper);
    }

    public Page<Course> searchGeneric(Commands.CourseSearchFilterCommand courseSearchFilterCommand, Pageable pageable) {
        LOGGER.info("Searching for courses with pagination page: {} and size: {}", pageable.getPageNumber(), pageable.getPageSize());
        Query query = new Query().with(pageable);
        List<Criteria> criteria = new ArrayList<>();
        courseSearchHelper(courseSearchFilterCommand, query, criteria);

        return courseRepository.searchGeneric(query, Course.class, pageable);
    }

    public Views.CourseUserView bookCourse(User user, String courseID, boolean isLeader) {
        Course course = courseRepository.findById(courseID).orElseThrow(() -> new CourseNotFoundException("Course not found"));

        try{
            course.bookCourse(user, isLeader);
            emailService.sendCourseBookedEmail(course, user);
        } catch (Exception e)
        {
            throw new CourseNotFoundException(e.getMessage());
        }

        return mapper.convert(courseRepository.save(course));
    }


    static boolean courseSearchHelper(Commands.CourseSearchFilterCommand courseSearchFilterCommand, Query query, List<Criteria> criteria) {
        for (var field : courseSearchFilterCommand.getClass().getDeclaredFields()) {
            field.setAccessible(true);
            try {
//                if(Objects.nonNull(field.get(userSearchFilterDTO)));
                if (field.get(courseSearchFilterCommand) != null && !field.get(courseSearchFilterCommand).toString().isEmpty()) {
                    switch (field.getName()) {
                        case "startsAt" -> criteria.add(Criteria.where("startsAt").gte(field.get(courseSearchFilterCommand)));
                        case "endsAt" -> criteria.add(Criteria.where("endsAt").lte(field.get(courseSearchFilterCommand)));
                        case "isPairCourse" -> criteria.add(Criteria.where("isPairCourse").is(field.get(courseSearchFilterCommand)));
                        case "teacherId" -> criteria.add(Criteria.where("teacherId").in(field.get(courseSearchFilterCommand).toString()));
                        case "street" -> criteria.add(Criteria.where("address.street").regex(Pattern.quote(field.get(courseSearchFilterCommand).toString())));
                        case "houseNumber" -> criteria.add(Criteria.where("address.houseNumber").is(field.get(courseSearchFilterCommand)));
                        case "zip" -> criteria.add(Criteria.where("address.zip").is(field.get(courseSearchFilterCommand)));
                        case "city" -> criteria.add(Criteria.where("address.city").regex(Pattern.quote(field.get(courseSearchFilterCommand).toString())));
                        case "country" -> criteria.add(Criteria.where("address.country").regex(Pattern.quote(field.get(courseSearchFilterCommand).toString())));
                        case "courseRequirement" -> criteria.add(Criteria.where("courseRequirement").is(Course.CourseRequirement.valueOf(field.get(courseSearchFilterCommand).toString())));
                        case "courseType" -> criteria.add(Criteria.where("courseType").is(CourseBaseEntity.CourseType.valueOf(field.get(courseSearchFilterCommand).toString())));
                        case "courseStatus" -> criteria.add(Criteria.where("courseStatus").is(CourseBaseEntity.CourseStatus.valueOf(field.get(courseSearchFilterCommand).toString())));
                        case "runTime" -> criteria.add(Criteria.where("runTime").is(field.get(courseSearchFilterCommand)));
                        case "bookAbleAt" -> criteria.add(Criteria.where("bookAbleAt").gte(field.get(courseSearchFilterCommand)));
                        case "bookAbleTo" -> criteria.add(Criteria.where("bookAbleTo").lte(field.get(courseSearchFilterCommand)));
                        case "theme" -> criteria.add(Criteria.where("theme").regex(Pattern.quote(field.get(courseSearchFilterCommand).toString())));
                        case "userId" -> criteria.add(Criteria.where("booked.userId").in(field.get(courseSearchFilterCommand)));
                    }
                }
            } catch (IllegalArgumentException | IllegalAccessException e) {
                // TODO Auto-generated catch block
                return true;
            }
        }

        if (!criteria.isEmpty()) {
            query.addCriteria(new Criteria().andOperator(criteria.toArray(new Criteria[0])));
        }
        return false;
    }

    public Views.CourseUserView removeFromEvent(User user, String courseID, String eventId) {
        Course course = courseRepository.findById(courseID).orElseThrow(() -> new CourseNotFoundException("Course not found"));
        course.getEvents().forEach(event -> {
            if (event.getId().equals(eventId)) {
                event.getBooked().removeIf(booked -> booked.getUserID().equals(user.getId()));
            }
        });
        return mapper.convert(courseRepository.save(course));
    }

    public Views.CourseUserView addToEvent(User user, String courseID, String eventId, boolean isLeader) {
        if(courseRepository.findCoursesByCourseStatusAndBookedUserID(CourseBaseEntity.CourseStatus.OPEN, user.getId(), Pageable.unpaged()).getTotalElements() <= 0)
        {
            throw new CourseNotFoundException("User has no open courses");
        }
        Course course = courseRepository.findById(courseID).orElseThrow(() -> new CourseNotFoundException("Course not found"));
        course.getEvents().forEach(event -> {
            if (event.getId().equals(eventId)) {
                event.addBookedForChange(user, isLeader);
            }
        });
        return mapper.convert(courseRepository.save(course));
    }

    public Views.CourseUserView bookCourseAsPair(User user, Commands.PairCourseBookCommand pairCourseBookCommand) {
        Course course = courseRepository.findById(pairCourseBookCommand.courseId())
                .orElseThrow(() -> new CourseNotFoundException("Course not found"));
        User partner = userRepository.findById(pairCourseBookCommand.partnerId())
                .orElseThrow(() -> new UserNotFoundException("User not found"));
        course.bookCourseWithPartner(user, partner, pairCourseBookCommand.isLeader());
        notificationUserService.createNotificationWithString(user,partner,
                (user.getPersonal().getFullName()+" hat sie als Partner für den Kurs "+course.getTheme()+" am "+ course.getStartsAt().atZone(ZoneId.of("Europe/Paris")).format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm"))+ " eingetragen"),
                0);
        return mapper.convert(courseRepository.save(course));
    }

    public BigDecimal getPrice(String courseID) {
        return courseRepository.findById(courseID).orElseThrow(() -> new CourseNotFoundException("Course not found")).getPrice();
    }
}
