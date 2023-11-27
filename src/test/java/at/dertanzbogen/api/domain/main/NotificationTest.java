package at.dertanzbogen.api.domain.main;


import at.dertanzbogen.api.domain.main.User.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static java.lang.String.format;
import static org.junit.jupiter.api.Assertions.*;

public class NotificationTest {

    private User userS;
    private User userA;
    private User userT;
    private Notification n;

    @BeforeEach
    void beforEach()
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

        userA = new User.UserBuilder()
                .setFirstName("berni")
                .setLastName("piffel")
                .setEmail("bernhard.piffel@gmx.at")
                .setPassword("warcraft12")
                .setHelper(true)
                .setLeader(true)
                .setUserGroup(User.userGroup.ADMIN)
                .build();

        userT = new User.UserBuilder()
                .setFirstName("berni")
                .setLastName("piffel")
                .setEmail("bernhard.piffel@gmx.at")
                .setPassword("warcraft12")
                .setHelper(true)
                .setLeader(true)
                .setUserGroup(User.userGroup.TEACHER)
                .build();

        n = new Notification(userA.getId(),1,"test", Notification.NotificationType.INFO,userS.getId());
    }

    @Test
    void setAcceptAmount()
    {
        assertEquals(1,n.getAcceptAmount());
        n.setAcceptAmount(2);
        assertEquals(2,n.getAcceptAmount());
    }

    @Test
    void setAcceptAmountFails()
    {
        assertEquals(1,n.getAcceptAmount());

        IllegalArgumentException e = assertThrows(IllegalArgumentException.class, ()-> n.setAcceptAmount(-1));
        assertEquals("acceptAmount muss positiv sein",e.getMessage());
    }

    @Test
    void setContent()
    {
        assertEquals("test",n.getContent());
        n.setContent("test2");
        assertEquals("test2",n.getContent());
        n.setContent("a".repeat(255));
        assertEquals("a".repeat(255),n.getContent());
    }

    @Test
    void setContentFails()
    {
        IllegalArgumentException e = assertThrows(IllegalArgumentException.class, ()-> n.setContent(""));
        assertEquals("Notification Content darf nicht leer sein",e.getMessage());
        NullPointerException nl = assertThrows(NullPointerException.class, ()-> n.setContent(null));
        assertEquals("Notification Content darf nicht null sein",nl.getMessage());
        e = assertThrows(IllegalArgumentException.class, ()-> n.setContent("a".repeat(256)));
        assertEquals("Notification Content muss zwischen 1 und 255 sein.",e.getMessage());
    }

    @Test
    void addToReadBy()
    {
        n.addToReadBy(userS.getId());
        assertTrue(n.getReadById().contains(userS.getId()));
    }

    @Test
    void addToAcceptedBy()
    {
        n.addToAcceptedBy(userS.getId());
        assertTrue(n.getAcceptedById().contains(userS.getId()));
    }

    @Test
    void addToAcceptedByFails()
    {
        n.addToAcceptedBy(userS.getId());
        IllegalArgumentException e = assertThrows(IllegalArgumentException.class, ()-> n.addToAcceptedBy(userA.getId()));
        assertEquals("Accepted by muss kleiner sein als 0.",e.getMessage());
    }
}
