package at.dertanzbogen.api.domain.main;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.util.List;


public record Media(String id, Instant createdAt, String fileName, String mimeType, int size, int width, int height) {
    public static List<String> toMediaIds(List<Media> medias) {
        return medias.stream().map(Media::id).toList();
    }
}
