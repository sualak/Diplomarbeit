package at.dertanzbogen.api.domain.main.Course;

import at.dertanzbogen.api.domain.main.Address;
import at.dertanzbogen.api.domain.main.Media;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.*;

@NoArgsConstructor
public class Event extends CourseBaseEntity {

    private Media video;
    private Set<String> attended = new HashSet<>();

    public Media getVideo() {
        return video;
    }

    public Set<String> getAttended() {
        return Collections.unmodifiableSet(attended);
    }

    public void addAttended(String user) {
        Optional<Booked> first = getBooked().stream().filter(booked -> booked.getUserID().equals(user)).findFirst();
        if(first.isEmpty())
            throw new IllegalArgumentException("User already attended");
        attended.add(user);
    }

    public void removeAttended(String user) {
        attended.remove(user);
    }

    public void setVideo(Media video) {
        this.video = video;
    }

    public static class eventBuilder {
        private Event event;

        public eventBuilder() {
            event = new Event();
        }

        public eventBuilder setStartsAt(Instant startsAt) {
            event.setStartsAt(startsAt);
            return this;
        }

        public eventBuilder setEndsAt(Instant endsAt) {
            event.setEndsAt(endsAt);
            return this;
        }

        public eventBuilder setMaxUser(int maxUser) {
            event.setMaxUsers(maxUser);
            return this;
        }

        public eventBuilder setPairCourse(boolean isPairCourse) {
            event.setPairCourse(isPairCourse);
            return this;
        }

        public eventBuilder setAddress(Address address) {
            event.setAddress(address);
            return this;
        }

        public eventBuilder setFollowerLeaderBalance(int followerLeaderBalance) {
            event.setFollowerLeaderBalance(followerLeaderBalance);
            return this;
        }

        public eventBuilder setVideo(Media video) {
            event.setVideo(video);
            return this;
        }

        public eventBuilder setCourseType(CourseType courseType) {
            event.setCourseType(courseType);
            return this;
        }

        public eventBuilder setCourseRequirement(CourseRequirement courseRequirement) {
            event.setCourseRequirement(courseRequirement);
            return this;
        }

        public eventBuilder setCourseStatus(CourseStatus courseStatus) {
            event.setCourseStatus(courseStatus);
            return this;
        }

        public eventBuilder addAllBooked(Set<Booked> booked) {
            event.addAllBooked(booked);
            return this;
        }

        public eventBuilder addAllTeachers(Set<String> teachers) {
            event.addAllTeachers(teachers);
            return this;
        }

        public Event build() {
            return event;
        }
    }
}
