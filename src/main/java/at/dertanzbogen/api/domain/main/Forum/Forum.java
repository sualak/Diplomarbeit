//package at.dertanzbogen.api.domain.main.Forum;
//
//import java.util.ArrayList;
//import java.util.Collections;
//import java.util.List;
//
//
//
//public class Forum {
//    private List<ForumEntry> forumEntries = new ArrayList<>();
//
//    public void createForumEntry(String creator, String titel, String question) {
//        forumEntries.add(new ForumEntry(creator, titel, question));
//    }
//
//    public List<ForumEntry> getForumEntries() {
//        return Collections.unmodifiableList(forumEntries);
//    }
//
//    public void deleteForumEntry(String forumEntry) {
//        forumEntries.removeIf(entry -> entry.getId().equals(forumEntry));
//    }
//
//    public void deleteForumEntry(int index) {
//        forumEntries.remove(index);
//    }
//}
