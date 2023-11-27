package at.dertanzbogen.api.domain.main.Course;

import at.dertanzbogen.api.domain.main.User.Personal;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.DBRef;


@AllArgsConstructor
@NoArgsConstructor
@Getter
public class Booked {
    private String userID;
    private boolean isLeader;
}
