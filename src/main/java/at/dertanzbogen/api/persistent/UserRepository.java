package at.dertanzbogen.api.persistent;

import at.dertanzbogen.api.domain.main.User.Email;
import at.dertanzbogen.api.domain.main.User.Password;
import at.dertanzbogen.api.domain.main.User.User;
import at.dertanzbogen.api.persistent.Generic.GenericSearch;
import org.mapstruct.Named;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends MongoRepository<User, String>,
        GenericSearch, PagingAndSortingRepository<User, String> {

    Optional<User> findByEmailEmail(String email);
    Optional<User> findByEmailAndPassword(Email email, Password password);
    Optional<User> findByEmailEmailAndPasswordPassword(String email, String password);
    Page<User> findAllByIdIn(Collection<String> id, Pageable pageable);
    boolean existsByEmailEmail(String email);
    List<User> findByPersonalFirstNameIgnoreCase(String firstName);
    List<User> findByPersonalLastNameIgnoreCase(String lastName);
    List<User> findByPersonalFirstNameIgnoreCaseAndPersonalLastNameIgnoreCase(String firstName, String lastName);
    List<User> findAllByUserGroup(User.userGroup userGroup);
    List<User> findAllByIsHelperIs(boolean isHelper);
    List<User> findAllByIsLeaderIs(boolean isLeader);
    List<User> findAllByIsHelperIsAndIsLeaderIs(boolean isHelper, boolean isLeader);
}
