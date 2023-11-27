package at.dertanzbogen.api.presentation.mappers;

import at.dertanzbogen.api.domain.main.Course.Event;
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
public abstract class EventAdminViewMapper implements GenericTypeMapper<Event, Views.EventAdminView> {
    UserRepository userRepo;
    EventViewMapper eventViewMapper;
    UserViewMapper userViewMapper;

    @Override
    public Views.EventAdminView convert(Event event) {
        {
            List<User> bookedUsers = new ArrayList<>();

            event.getBooked()
                    .forEach(booked -> userRepo.findById(booked.getUserID())
                            .ifPresent(user -> {
                                user.setLeader(booked.isLeader());
                                bookedUsers.add(user);
                            }));

            return new Views.EventAdminView(event.getId(),
                    eventViewMapper.convert(event),
                    userRepo.findAllById(event.getTeachers()).stream().map(userViewMapper::convert).toList(),
                    bookedUsers.stream().map(userViewMapper::convert).toList(),
                    userRepo.findAllById(event.getAttended()).stream().map(userViewMapper::convert).toList());
        }
    }
}
