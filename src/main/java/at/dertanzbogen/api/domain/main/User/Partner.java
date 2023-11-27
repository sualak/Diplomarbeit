package at.dertanzbogen.api.domain.main.User;

import at.dertanzbogen.api.domain.main.Notification;
import at.dertanzbogen.api.domain.validation.Ensure;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;


@NoArgsConstructor
@ToString
public class Partner {
    private String self;
    private Set<String> partners = new HashSet<>();

    public Partner(String self) {
        this.self = self;
    }

    public String getSelf() {
        return self;
    }

    public Set<String> getPartners() {
        return Collections.unmodifiableSet(partners);
    }


    public Notification sendPartnerRequest(String receiver, String transmitterName) {
        return new Notification(self,
                1,
                "Partneranfrage von " + transmitterName,
                Notification.NotificationType.FRIEND_REQUEST,
                Ensure.sendPartnerRequestValid(self, receiver, partners));
    }

    public void addPartner(String user) {
        partners.add(Ensure.addPartnerValid(self, user));
    }

    public void removePartner(String user) {
        partners.remove(user);
    }

}
