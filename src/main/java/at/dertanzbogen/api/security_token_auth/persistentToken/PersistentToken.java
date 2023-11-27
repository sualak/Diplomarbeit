package at.dertanzbogen.api.security_token_auth.persistentToken;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;

@NoArgsConstructor
@Setter
@Getter
@TypeAlias("persistent_token")
@Document(collection = "persistent_logins")
public class PersistentToken
{
    @Id
    // The series identifier (as `_id` in the database)
    private String series;

    // The username of the user
    private String username;

    // TODO Hash Token with Keccak-256
    // The token value hashed using Keccak-256
    private String token;

    // The date when the token was last used.
    private Instant lastUsed;
}
