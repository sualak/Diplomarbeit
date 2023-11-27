package at.dertanzbogen.api.domain.main.Forum;

import at.dertanzbogen.api.domain.main.User.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class AnswerTest {

    private Answer a;
    private User userS;

    @BeforeEach
    void beforeEach()
    {
        userS = new User.UserBuilder()
                .setFirstName("berni")
                .setLastName("piffel")
                .setEmail("bernhard.piffel@gmx.at")
                .setPassword("warcraft12")
                .setHelper(true)
                .setLeader(true)
                .setUserGroup(User.userGroup.STUDENT)
                .build();

        a = new Answer(userS.getId(), userS.getPersonal().getFirstName(), "test");
    }

    @Test
    void setAnswer()
    {
        assertEquals("test", a.getAnswer());
        a.setAnswer("aa");
        assertEquals("aa", a.getAnswer());
        a.setAnswer("a".repeat(512));
        assertEquals("a".repeat(512), a.getAnswer());

    }

    @Test
    void setAnswerFails()
    {
        IllegalArgumentException e = assertThrows(IllegalArgumentException.class, ()-> a.setAnswer(""));
        assertEquals("Answer darf nicht leer sein",e.getMessage());
        e = assertThrows(IllegalArgumentException.class, ()-> a.setAnswer("1"));
        assertEquals("Answer muss zwischen 2 und 512 sein.",e.getMessage());
        NullPointerException nl = assertThrows(NullPointerException.class, ()-> a.setAnswer(null));
        assertEquals("Answer darf nicht null sein",nl.getMessage());
        e = assertThrows(IllegalArgumentException.class, ()-> a.setAnswer("a".repeat(513)));
        assertEquals("Answer muss zwischen 2 und 512 sein.",e.getMessage());
    }
}
