//package at.dertanzbogen.api.persistent;
//
//import at.dertanzbogen.api.domain.main.Course.Course;
//import at.dertanzbogen.api.domain.main.User.User;
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.Pageable;
//import org.springframework.data.mongodb.core.query.Query;
//
//import java.util.List;
//
//public interface CustomUserRepository {
//
//    Page<User> searchUsers(String firstName, String lastName, String email, String userGroup, Boolean leader, Boolean helper, String completedCourseByType, Pageable pageable);
//
//}
