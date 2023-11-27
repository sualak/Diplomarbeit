package at.dertanzbogen.api.domain.main.Course;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class CourseProgrammTest {

    private CourseProgramm courseProgramm;
    @BeforeEach
    void beforEach()
    {
        courseProgramm = new CourseProgramm("test");
    }

    @Test
    void setProgramm()
    {
        assertEquals("test", courseProgramm.getProgramm());
        courseProgramm.setProgramm("test2");
        assertEquals("test2", courseProgramm.getProgramm());
    }

    @Test
    void setProgrammFails()
    {
        assertEquals("test", courseProgramm.getProgramm());
        NullPointerException e = assertThrows(NullPointerException.class, () -> courseProgramm.setProgramm(null));
        assertEquals("Programm darf nicht null sein", e.getMessage());
        assertEquals("test", courseProgramm.getProgramm());
    }
}
