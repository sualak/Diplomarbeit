package at.dertanzbogen.api.domain.main;

import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static at.dertanzbogen.api.domain.validation.Ensure.*;
@NoArgsConstructor
@Document(collection = "notifications")
public class Notification extends BaseEntity {
    private String creatorId;
    private String eventId;
    private Set<String> sendToId = new HashSet<>();
    private Set<String> readById = new HashSet<>();
    private Set<String> acceptedById = new HashSet<>();
    private int acceptAmount;
    private String content;
    private Instant sendAt;
    private NotificationType notificationType;

    public Notification(String creatorId, int acceptAmount, String content, NotificationType notificationType, String... sendToId) {
        this.creatorId = ensureCreatorValid(creatorId, "Creator");
        this.acceptAmount = acceptAmountValid(acceptAmount, "acceptAmount");
        this.content = ensureNotificationContentValid(content, "Notification Content");
        this.notificationType = notificationType;
        Arrays.stream(sendToId).forEach(this::addToSendTo);
    }

    public String getCreatorId() {
        return creatorId;
    }

    public Set<String> getSendToId() {
        return sendToId;
    }

    public Set<String> getReadById() {
        return readById;
    }

    public Set<String> getAcceptedById() {
        return acceptedById;
    }

    public int getAcceptAmount() {
        return acceptAmount;
    }

    public String getContent() {
        return content;
    }

    public Instant getSendAt() {
        return sendAt;
    }

    public String getEventId() {
        return eventId;
    }

    public NotificationType getNotificationType() {
        return notificationType;
    }

    public void setCreatorId(String creatorId) {
        this.creatorId = ensureCreatorValid(creatorId, "Creator");

    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    public void setAcceptAmount(int acceptAmount) {
        this.acceptAmount = acceptAmountValid(acceptAmount, "acceptAmount");
        
    }

    //mayBeFinal
    public void setContent(String content) {
        this.content = ensureNotificationContentValid(content, "Notification Content");
        readById.clear();
        acceptedById.clear();
        
    }

    public void addToSendTo(String user) {
        sendToId.add(ensureCreatorValid(user, "creator"));
        
    }

    public void addToReadBy(String user) {
        readById.add(ensureCreatorValid(user, "User"));
        
    }

    public void addToAcceptedBy(String user) {
        acceptedById.add(ensureAcceptedByValid(user, acceptAmount, acceptedById.size()));
        
    }

    public void setSendAt(Instant sendAt) {
        this.sendAt = ensureSendDateValid(sendAt);
        
    }

    public void removeFromSendTo(String user) {
        sendToId.remove(user);

    }

    public static class NotificationBuilder
    {
        private Notification notification = new Notification();

        public NotificationBuilder setCreatorId(String creatorId) {
            notification.setCreatorId(creatorId);
            return this;
        }

        public NotificationBuilder setAcceptAmount(int acceptAmount) {
            notification.setAcceptAmount(acceptAmount);
            return this;
        }

        public NotificationBuilder setContent(String content) {
            notification.setContent(content);
            return this;
        }

        public NotificationBuilder addToSendTo(String creator) {
            notification.addToSendTo(creator);
            return this;
        }

        public NotificationBuilder addToReadBy(String user) {
            notification.addToReadBy(user);
            return this;
        }

        public NotificationBuilder addToAcceptedBy(String user) {
            notification.addToAcceptedBy(user);
            return this;
        }

        public NotificationBuilder setSendTo(List<String> sendTo) {
            sendTo.forEach(notification::addToSendTo);
            return this;
        }

        public NotificationBuilder setNotificationType(String notificationType) {
            notification.notificationType = NotificationType.valueOf(notificationType);
            return this;
        }

        public NotificationBuilder setSendAt(Instant sendAt) {
            notification.setSendAt(sendAt);
            return this;
        }

        public NotificationBuilder setEventId(String eventId) {
            notification.setEventId(eventId);
            return this;
        }

        public Notification build() {
            return notification;
        }
    }

    public enum NotificationType {
        FRIEND_REQUEST,
        INFO,
        CANCEL_REQUEST,
        ADMIN_MESSAGE,
        HELPER_REQUEST,
    }
}
