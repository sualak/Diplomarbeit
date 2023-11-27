package at.dertanzbogen.api.domain.main.User;

import at.dertanzbogen.api.domain.main.BaseEntity;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "dummyUser")
@NoArgsConstructor
@ToString
@TypeAlias("dummyUser")
public class dummyUser extends BaseEntity {
    private Email email;

//    public dummyUser(String email) {
//        this.email = new Email(email);
//    }

    public Email getEmail() {
        return email;
    }

    public void setEmail(Email email) {
        this.email = email;
    }

    public static class dummyUserBuilder {
        private dummyUser dummyUser;

        public dummyUserBuilder() {
            dummyUser = new dummyUser();
        }

        public dummyUserBuilder setEmail(Email email) {
            dummyUser.setEmail(email);
            return this;
        }

        public dummyUser build() {
            return dummyUser;
        }
    }
}
