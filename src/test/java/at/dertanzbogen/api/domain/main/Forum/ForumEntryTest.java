package at.dertanzbogen.api.domain.main.Forum;

import at.dertanzbogen.api.domain.main.User.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ForumEntryTest {

    private ForumEntry forumEntry;
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

        forumEntry = new ForumEntry(userS.getId(), "test", "test");
    }

    @Test
    void setTitel()
    {
        assertEquals("test", forumEntry.getTitle());
        forumEntry.setTitle("aa");
        assertEquals("aa", forumEntry.getTitle());
        forumEntry.setTitle("a".repeat(255));
        assertEquals("a".repeat(255), forumEntry.getTitle());
    }

    @Test
    void setTitelFails()
    {
        IllegalArgumentException e = assertThrows(IllegalArgumentException.class, ()-> forumEntry.setTitle(""));
        assertEquals("Titel darf nicht leer sein",e.getMessage());
        e = assertThrows(IllegalArgumentException.class, ()-> forumEntry.setTitle("1"));
        assertEquals("Titel muss zwischen 2 und 255 sein.",e.getMessage());
        NullPointerException nl = assertThrows(NullPointerException.class, ()-> forumEntry.setTitle(null));
        assertEquals("Titel darf nicht null sein",nl.getMessage());
        e = assertThrows(IllegalArgumentException.class, ()-> forumEntry.setTitle("a".repeat(513)));
        assertEquals("Titel muss zwischen 2 und 255 sein.",e.getMessage());
    }

    @Test
    void setQuestion()
    {
        assertEquals("test", forumEntry.getQuestion());
        forumEntry.setQuestion("aa");
        assertEquals("aa", forumEntry.getQuestion());
        forumEntry.setQuestion("a".repeat(512));
        assertEquals("a".repeat(512), forumEntry.getQuestion());
    }

    @Test
    void setQuestionFails()
    {
        IllegalArgumentException e = assertThrows(IllegalArgumentException.class, ()-> forumEntry.setQuestion(""));
        assertEquals("Question darf nicht leer sein",e.getMessage());
        e = assertThrows(IllegalArgumentException.class, ()-> forumEntry.setQuestion("1"));
        assertEquals("Question muss zwischen 2 und 512 sein.",e.getMessage());
        NullPointerException nl = assertThrows(NullPointerException.class, ()-> forumEntry.setQuestion(null));
        assertEquals("Question darf nicht null sein",nl.getMessage());
        e = assertThrows(IllegalArgumentException.class, ()-> forumEntry.setQuestion("a".repeat(513)));
        assertEquals("Question muss zwischen 2 und 512 sein.",e.getMessage());
    }

    @Test
    void changeIsOpen()
    {
        assertTrue(forumEntry.isOpen());
        forumEntry.changeIsOpen();
        assertFalse(forumEntry.isOpen());
    }

    @Test
    void addAnswer()
    {
        forumEntry.addAnswer(userS.getId(), userS.getPersonal().getFirstName(), "test");
        assertEquals(1, forumEntry.getAnswers().size());
        assertEquals(userS.getPersonal().getFirstName(), forumEntry.getAnswers().get(0).getCreatorName());
        assertEquals("test", forumEntry.getAnswers().get(0).getAnswer());
    }

    @Test
    void addAnswerFails()
    {
        IllegalArgumentException e = assertThrows(IllegalArgumentException.class, ()-> forumEntry.addAnswer(userS.getId(),userS.getPersonal().getFirstName(), ""));
        assertEquals("Answer darf nicht leer sein",e.getMessage());
        e = assertThrows(IllegalArgumentException.class, ()-> forumEntry.addAnswer(userS.getId(),userS.getPersonal().getFirstName(), "1"));
        assertEquals("Answer muss zwischen 2 und 512 sein.",e.getMessage());
        NullPointerException nl = assertThrows(NullPointerException.class, ()-> forumEntry.addAnswer(userS.getId(),userS.getPersonal().getFirstName(), null));
        assertEquals("Answer darf nicht null sein",nl.getMessage());
        e = assertThrows(IllegalArgumentException.class, ()-> forumEntry.addAnswer(userS.getId(),userS.getPersonal().getFirstName(), "a".repeat(513)));
        assertEquals("Answer muss zwischen 2 und 512 sein.",e.getMessage());
    }

    @Test
    void deleteAnswer()
    {
        forumEntry.addAnswer(userS.getId(),userS.getPersonal().getFirstName(), "test");
        assertEquals(1, forumEntry.getAnswers().size());
        forumEntry.deleteAnswer(0);
        assertEquals(0, forumEntry.getAnswers().size());
        forumEntry.addAnswer(userS.getId(),userS.getPersonal().getFirstName(), "test");
        assertEquals(1, forumEntry.getAnswers().size());
        forumEntry.deleteAnswer(forumEntry.getAnswers().get(0).getId());
        assertEquals(0, forumEntry.getAnswers().size());
    }
}
