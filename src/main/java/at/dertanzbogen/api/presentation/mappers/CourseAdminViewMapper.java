package at.dertanzbogen.api.presentation.mappers;

import at.dertanzbogen.api.domain.main.Course.Course;
import at.dertanzbogen.api.domain.main.User.User;
import at.dertanzbogen.api.persistent.UserRepository;
import at.dertanzbogen.api.presentation.DTOs.Views;
import at.dertanzbogen.api.presentation.mappers.generic.GenericTypeMapper;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@AllArgsConstructor
public abstract class CourseAdminViewMapper implements GenericTypeMapper<Course, Views.CourseAdminView> {

    UserRepository userRepo;
    CourseViewMapper courseUserMapper;
    EventAdminViewMapper eventAdminViewMapper;
    UserViewMapper userViewMapper;


    @Override
    public Views.CourseAdminView convert(Course course) {
        {
            List<User> bookedUsers = new ArrayList<>();

            course.getBooked()
                    .forEach(booked -> userRepo.findById(booked.getUserID())
                            .ifPresent(user -> {
                                user.setLeader(booked.isLeader());
                                bookedUsers.add(user);
                            }));

            return new Views.CourseAdminView(course.getId(),
                    courseUserMapper.convert(course),
                    course.getEvents().stream().map(eventAdminViewMapper::convert).toList(),
                    course.getCourseProgramm().getProgramm(),
                    course.getCourseDescription().getDescription(),
                    userRepo.findAllById(course.getTeachers()).stream().map(userViewMapper::convert).toList(),
                    bookedUsers.stream().map(userViewMapper::convert).toList());
        }
    }
}
