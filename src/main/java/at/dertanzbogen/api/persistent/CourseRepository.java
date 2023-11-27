package at.dertanzbogen.api.persistent;

import at.dertanzbogen.api.domain.main.Course.Course;
import at.dertanzbogen.api.domain.main.Course.CourseBaseEntity;
import at.dertanzbogen.api.persistent.Generic.GenericSearch;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CourseRepository extends MongoRepository<Course, String>,
        GenericSearch {

    List<Course> findCoursesByCourseStatus(
            CourseBaseEntity.CourseStatus courseStatus);
    Optional<Course> findCourseByIdAndCourseStatus(String courseId, CourseBaseEntity.CourseStatus courseStatus);
    List<Course> findCoursesByCourseStatusAndCourseType(CourseBaseEntity.CourseStatus courseStatus, CourseBaseEntity.CourseType courseType);
    Page<Course> findCoursesByCourseStatus(CourseBaseEntity.CourseStatus courseStatus, Pageable pageable);
    Page<Course> findCoursesByCourseStatusAndBookedUserID(CourseBaseEntity.CourseStatus courseStatus, String userID, Pageable pageable);
    boolean existsCourseByEventsId(String eventId);
    Optional<Course> findCourseByEventsIdAndCourseStatus(String eventId, CourseBaseEntity.CourseStatus courseStatus);
    List<Course> findCoursesByCourseStatusAndCourseTypeAndBookedUserID(CourseBaseEntity.CourseStatus courseStatus, CourseBaseEntity.CourseType courseType, String userID);
}
