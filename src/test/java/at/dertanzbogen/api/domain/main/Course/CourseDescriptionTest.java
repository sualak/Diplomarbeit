package at.dertanzbogen.api.domain.main.Course;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class CourseDescriptionTest {

    private CourseDescription courseDescription;
    @BeforeEach
    void beforEach()
    {
        courseDescription = new CourseDescription("test");
    }

    @Test
    void setDescription()
    {
        assertEquals("test", courseDescription.getDescription());
        courseDescription.setDescription("test2");
        assertEquals("test2", courseDescription.getDescription());
    }

    @Test
    void setDescriptionFails()
    {
        assertEquals("test", courseDescription.getDescription());
        NullPointerException e = assertThrows(NullPointerException.class, () -> courseDescription.setDescription(null));
        assertEquals("Description darf nicht null sein", e.getMessage());
        assertEquals("test", courseDescription.getDescription());
    }
}
