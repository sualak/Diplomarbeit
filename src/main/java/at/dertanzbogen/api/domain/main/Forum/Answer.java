package at.dertanzbogen.api.domain.main.Forum;

import at.dertanzbogen.api.domain.main.BaseEntity;
import at.dertanzbogen.api.domain.validation.Ensure;
import lombok.NoArgsConstructor;


@NoArgsConstructor
public class Answer extends BaseEntity {
    private String creatorName;
    private String creatorId;
    private String answer;

    public Answer(String creatorId, String creatorName, String answer) {
        this.creatorId = Ensure.ensureCreatorValid(creatorId, "creator");
        this.creatorName = Ensure.ensureCreatorValid(creatorName, "creator");
        this.answer = Ensure.ensureAnswerValid(answer);
    }

    public String getCreatorId() {
        return creatorId;
    }

    public String getAnswer() {
        return answer;
    }

    public String getCreatorName() {
        return creatorName;
    }

    public void setAnswer(String answer) {
        this.answer = Ensure.ensureAnswerValid(answer);
        
    }
}
