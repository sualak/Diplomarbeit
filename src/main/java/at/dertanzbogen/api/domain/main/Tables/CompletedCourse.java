package at.dertanzbogen.api.domain.main.Tables;

import at.dertanzbogen.api.domain.main.BaseEntity;
import at.dertanzbogen.api.domain.main.Course.Course;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.mongodb.core.mapping.Document;

//V3
@TypeAlias("completedCourseByType")
@Document(collection = "completedCourseByType")
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class CompletedCourse extends BaseEntity {
    private boolean isLeader;
    private String theme;
    private String courseID;
    private String userID;
    private Course.CourseType courseType;
}
