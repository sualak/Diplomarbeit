//package at.dertanzbogen.api.domain.main.Forum;
//
//import at.dertanzbogen.api.domain.main.User.User;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//
//public class ForumTest {
//
//    private User userS;
//    private Forum forum;
//
//    @BeforeEach
//    void beforEach()
//    {
//        userS = new User.UserBuilder()
//                .setFirstName("berni")
//                .setLastName("piffel")
//                .setEmail("bernhard.piffel@gmx.at")
//                .setPassword("warcraft12")
//                .setHelper(true)
//                .setLeader(true)
//                .setUserGroup(User.userGroup.STUDENT)
//                .build();
//
//        forum = new Forum();
//    }
//
//    @Test
//    void createForumEntry()
//    {
//        forum.createForumEntry(userS.getId(), "test", "test");
//    }
//
//
//    @Test
//    void deleteForumEntry()
//    {
//        forum.createForumEntry(userS.getId(), "test", "test");
//        assertEquals(1, forum.getForumEntries().size());
//        forum.deleteForumEntry(0);
//        assertEquals(0, forum.getForumEntries().size());
//        forum.createForumEntry(userS.getId(), "test", "test");
//        assertEquals(1, forum.getForumEntries().size());
//        forum.deleteForumEntry(forum.getForumEntries().get(0).getId());
//        assertEquals(0, forum.getForumEntries().size());
//    }
//}
