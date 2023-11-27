package at.dertanzbogen.api.domain.main.Forum;

import at.dertanzbogen.api.domain.main.BaseEntity;
import at.dertanzbogen.api.domain.validation.Ensure;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.*;

@NoArgsConstructor
@Document(collection = "forumEntry")
@TypeAlias("forumEntry")
public class ForumEntry extends BaseEntity {

    private String creatorId;
    private String creatorName;
    private String title;
    private String question;
    private List<Answer> answers = new ArrayList<>();
    private boolean isOpen = true;

    public ForumEntry(String creatorName, String title, String question) {
        this.creatorName = Ensure.ensureCreatorValid(creatorName, "Creator");
        this.title = Ensure.ensureTitelValid(title);
        this.question = Ensure.ensureQuestionValid(question);
    }

    public String getCreatorId() {
        return creatorId;
    }

    public String getCreatorName() {
        return creatorName;
    }

    public String getTitle() {
        return title;
    }

    public String getQuestion() {
        return question;
    }

    public List<Answer> getAnswers() {
        return Collections.unmodifiableList(answers);
    }

    public boolean isOpen() {
        return isOpen;
    }

    public void setTitle(String title) {
        this.title = Ensure.ensureTitelValid(title);
        
    }

    public void setQuestion(String question) {
        this.question = Ensure.ensureQuestionValid(question);
        
    }

    public void changeIsOpen() {
        isOpen = !isOpen;
        
    }

    public void addAnswer(String creatorId, String creator, String answer) {
        answers.add(new Answer(creatorId, creator, answer));
        
    }

    public void deleteAnswer(String answerId) {
        answers.removeIf(answer -> answer.getId().equals(answerId));
    }

    public void deleteAnswer(int index) {
        answers.remove(index);
        
    }

    public static class ForumEntryBuilder {
        private ForumEntry forumEntry;

        public ForumEntryBuilder() {
            forumEntry = new ForumEntry();
        }

        public ForumEntryBuilder setCreatorId(String creatorId) {
            forumEntry.creatorId = Ensure.ensureCreatorValid(creatorId, "Creator");
            return this;
        }

        public ForumEntryBuilder setCreatorName(String creator) {
            forumEntry.creatorName = Ensure.ensureCreatorValid(creator, "Creator");
            return this;
        }

        public ForumEntryBuilder setTitel(String titel) {
            forumEntry.setTitle(titel);
            return this;
        }

        public ForumEntryBuilder setQuestion(String question) {
            forumEntry.setQuestion(question);
            return this;
        }

        public ForumEntryBuilder setIsOpen(boolean isOpen) {
            forumEntry.isOpen = isOpen;
            return this;
        }

        public ForumEntryBuilder addAnswer(String creatorId, String creator, String answer) {
            forumEntry.addAnswer(creatorId, creator, answer);
            return this;
        }

        public ForumEntry build() {
            return forumEntry;
        }
    }
}
