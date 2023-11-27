package at.dertanzbogen.api.persistent;

import at.dertanzbogen.api.domain.main.Notification;
import at.dertanzbogen.api.persistent.Generic.GenericSearch;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NotificationRepository extends MongoRepository<Notification, String>, GenericSearch {

    Page<Notification> findNotificationByCreatorId(String creatorId, Pageable pageable);

    Page<Notification> findAllOrderBySendAt(Pageable pageable);

    Page<Notification> findAllBySendToIdOrCreatorIdOrderBySendAt(String sendToId,String creatorId, Pageable pageable);

    Page<Notification> findAllBySendToIdOrderBySendAt(String sendToId, Pageable pageable);

    Page<Notification> findAllBySendToIdAndAcceptedByIdIsNotContainingOrderBySendAt(String sendToId, String acceptedById, Pageable pageable);
}
