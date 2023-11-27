package at.dertanzbogen.api.domain.main;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.annotation.Version;
import org.springframework.data.mongodb.repository.Update;

import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

@NoArgsConstructor
public abstract class BaseEntity {
    @Id
    private String id = UUID.randomUUID().toString();
    @CreatedDate
    private Instant createdAt = Instant.now();
    @LastModifiedDate
    private Instant updatedAt = Instant.now();
    @Version
    private Long version;
    public String getId() {
        return id;
    }
    public Instant getCreatedAt() {
        return createdAt;
    }
    public Instant getUpdatedAt() {
        return updatedAt;
    }
    public void setId(String id) {
        this.id = id;
    }

    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BaseEntity that = (BaseEntity) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }
}
