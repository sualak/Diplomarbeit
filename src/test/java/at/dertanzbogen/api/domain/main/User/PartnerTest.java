package at.dertanzbogen.api.domain.main.User;

import at.dertanzbogen.api.domain.main.Notification;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class PartnerTest {

    private User userS;
    private User userA;
    private User userT;

    @BeforeEach
    void beforEach() {
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
    }

    //es ist nicht erlaubt sich selbst zu adden(sollte nie vorkommen aber zur sicherheit wird es erneut überprüft)
    @Test
    void addUser()
    {
        userS.getPartner().addPartner(userT.getId());
        assertTrue(userS.getPartner().getPartners().contains(userT.getId()));
    }

    @Test
    void addUserFails()
    {
        IllegalArgumentException e = assertThrows(IllegalArgumentException.class, ()-> userS.getPartner().sendPartnerRequest(userS.getId(),userS.getPersonal().getFirstName()+" "+userS.getPersonal().getLastName()));
        assertEquals("Sich selbst als Partner zu adden ist nicht erlaubt",e.getMessage());
    }

    //man darf sich nicht selbst adden oder jemanden der bereits Partner ist
    @Test
    void sendPartnerRequest()
    {
        Notification n = userS.getPartner().sendPartnerRequest(userT.getId(), userS.getPersonal().getFirstName()+" "+userS.getPersonal().getLastName());
        assertTrue(n.getSendToId().contains(userT.getId()));
        assertEquals(1,n.getAcceptAmount());
        assertEquals(userS.getId(),n.getCreatorId());
        assertEquals("Partneranfrage von "+userS.getPersonal().getFullName(),n.getContent());
    }

    @Test
    void sendPartnerRequestFails()
    {
        IllegalArgumentException e = assertThrows(IllegalArgumentException.class, ()-> userS.getPartner().sendPartnerRequest(userS.getId(),userS.getPersonal().getFirstName()+" "+userS.getPersonal().getLastName()));
        assertEquals("Sich selbst als Partner zu adden ist nicht erlaubt",e.getMessage());
        userS.getPartner().addPartner(userT.getId());
        e = assertThrows(IllegalArgumentException.class, ()-> userS.getPartner().sendPartnerRequest(userT.getId(),userS.getPersonal().getFirstName()+" "+userS.getPersonal().getLastName()));
        assertEquals("Angefragter User ist bereits ein Partner",e.getMessage());
    }

    //remove muss nicht überprüft werden da es nicht möglich ist etwas zu löschen das nicht enthalten ist
    @Test
    void removePartner()
    {
        userS.getPartner().addPartner(userT.getId());
        assertTrue(userS.getPartner().getPartners().contains(userT.getId()));
        userS.getPartner().removePartner(userT.getId());
        assertFalse(userS.getPartner().getPartners().contains(userT.getId()));
    }
}
