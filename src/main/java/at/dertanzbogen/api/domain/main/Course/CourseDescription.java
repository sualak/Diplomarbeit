package at.dertanzbogen.api.domain.main.Course;


import at.dertanzbogen.api.domain.validation.Ensure;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class CourseDescription {
    private String description;

    public CourseDescription(String description) {
        this.description = Ensure.ensureNonNullValid(description, "Description");
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = Ensure.ensureNonNullValid(description, "Description");
    }
}
