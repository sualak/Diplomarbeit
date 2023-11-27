package at.dertanzbogen.api.security_token_auth.persistentToken;

import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.web.authentication.rememberme.PersistentRememberMeToken;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
import org.springframework.stereotype.Repository;

import java.time.Duration;
import java.time.Instant;
import java.util.Date;
import java.util.Optional;

import static org.springframework.data.mongodb.core.query.Criteria.where;

@Repository
@RequiredArgsConstructor
public class MongoPersistentTokenRepository implements PersistentTokenRepository
{
    private final MongoTemplate mongoTemplate;

    @Override
    public void createNewToken(PersistentRememberMeToken token)
    {
        PersistentToken persistentToken = new PersistentToken();
        persistentToken.setUsername(token.getUsername());
        persistentToken.setSeries(token.getSeries());
        persistentToken.setToken(token.getTokenValue());
        persistentToken.setLastUsed(token.getDate().toInstant());

        mongoTemplate.save(persistentToken);
    }


    @Override
    public void updateToken(String series, String tokenValue, Date lastUsed)
    {
        Query query = new Query(where("_id").is(series));
        Update update = new Update().set("token", tokenValue).set("lastUsed", lastUsed.toInstant());
        mongoTemplate.updateFirst(query, update, PersistentToken.class);
    }


    @Override
    public PersistentRememberMeToken getTokenForSeries(String seriesId)
    {
        Query query = new Query(where("_id").is(seriesId));
        return Optional.ofNullable(mongoTemplate.findOne(query, PersistentToken.class))
            .map(this::toPersistentRememberMeToken)
            .orElse(null);
    }


    @Override
    public void removeUserTokens(String username)
    {
        Query query = new Query(where("username").is(username));
        mongoTemplate.remove(query, PersistentToken.class);
    }


    // This method is called every day at midnight
    @Scheduled(cron = "0 0 0 * * ?")
    public void deleteExpiredTokens()
    {
        Instant oneMonthAgo = Instant.now().minus(Duration.ofDays(30));
        Query query = new Query(where("lastUsed").lt(oneMonthAgo));
        mongoTemplate.remove(query, PersistentToken.class);
    }

    private PersistentRememberMeToken toPersistentRememberMeToken(PersistentToken persistentLogin)
    {
        return new PersistentRememberMeToken(
            persistentLogin.getUsername(),
            persistentLogin.getSeries(),
            persistentLogin.getToken(),
            Date.from(persistentLogin.getLastUsed())
        );
    }
}
