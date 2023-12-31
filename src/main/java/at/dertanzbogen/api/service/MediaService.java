package at.dertanzbogen.api.service;

import at.dertanzbogen.api.domain.main.Media;
import at.dertanzbogen.api.domain.main.error.MediaServiceException;
import at.dertanzbogen.api.presentation.DTOs.Commands;
import at.dertanzbogen.api.presentation.mappers.UploadMediaCommandMapper;
import com.mongodb.Function;
import com.mongodb.client.gridfs.model.GridFSFile;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.data.util.Pair;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;


import static at.dertanzbogen.api.domain.main.Media.toMediaIds;
import static java.lang.String.format;
import static java.util.stream.IntStream.range;
import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;

// What is GridFS?
// ---------------
// https://www.mongodb.com/docs/manual/core/gridfs/
// GridFS is a specification for storing and retrieving files
// that exceed the BSON-document size limit of 16 MB.


// How GridFS works?
// -----------------
// GridFS works by dividing the large file into smaller chunks, typically of size 255 KB,
// and storing each of those chunks as a separate document in MongoDB.

// This means that instead of storing a large file as a single document,
// GridFS creates multiple smaller documents.

// In GridFS, two collections are created to handle this data:
// - fs.files: This collection is used to store the file's metadata (we store filename and mimetype)
// - fs.chunks: This collection contains the actual content of the file, stored in multiple documents.


// Why Media class?
// ---------------
// The Media class is a simple POJO that contains the metadata of a media file.
// It is possible to store any kind of media file in MongoDB GridFS in the fs.files collection.
// However, here are some advantages using a separate collection for media metadata:
// - Flexibility:
//     By storing metadata in a separate domain model,
//     we have full control over its structure and how it's indexed.
//     We are not constrained by the structure of the fs.files collection.
// - Performance:
//     If we frequently query or update metadata without needing to access the associated file data,
//     storing metadata separately can lead to performance improvements.
//     We don't need to query the fs.files collection (which could be quite large) just to get metadata.
// - Separation of concerns:
//    Storing metadata separately can make your application easier to understand and maintain.
//    Fhe fs.files and fs.chunks collections for storing file data, and our domain model for storing metadata.



@Service
@RequiredArgsConstructor
public class MediaService
{
    private final Logger LOGGER = LoggerFactory.getLogger(MediaService.class);

    private final GridFsTemplate gridFsTemplate;
    private static final UploadMediaCommandMapper mapper = UploadMediaCommandMapper.INSTANCE;


    // Save Medias -------------------------------------------------------------

    // Saves all media binaries and their metadata in MongoDB GridFS
    public List<Media> saveMedias(@NonNull MultipartFile[] mediaFiles, @NonNull Commands.UploadMediaCommand[] mediaMetas)
    {
        // We need to rollback all medias in case of an exception
        List<Media> medias = new ArrayList<>(mediaFiles.length);

        try {
            // Frontend must send the same number of media files and media metas
            Assert.isTrue(mediaFiles.length == mediaMetas.length,
                    "mediaFiles and mediaMetas must have the same length");

            LOGGER.info("Saving {} medias", mediaFiles.length);

            range(0, mediaFiles.length).forEach((i) ->
                    medias.add(saveMedia(mediaFiles[i], mediaMetas[i])));

            return medias;

        } catch (Exception e) {
            // Rollback all medias in MongoDB GridFS
            rollback(medias);
            throw new MediaServiceException("Failed to save all medias", e);
        }
    }


    // Saves a single media binary and its metadata in MongoDB GridFS
    public Media saveMedia(@NonNull MultipartFile mediaFile, @NonNull Commands.UploadMediaCommand mediaMeta)
    {
        try {
            // Frontend must send the same filename in mediaMeta and mediaFile
            Assert.isTrue(Objects.equals(mediaFile.getOriginalFilename(), mediaMeta.fileName()),
                    "mediaFile and mediaMeta must have the same filename");

            LOGGER.info("Saving media: {} with size {}", mediaMeta.fileName(), mediaMeta.size());

            // Store the media binary in MongoDB GridFS including MIME type
            ObjectId mediaId = gridFsTemplate.store(mediaFile.getInputStream(),
                    mediaMeta.fileName(), mediaMeta.mimeType());

            return mapper.toMedia(mediaMeta, mediaId.toString());

        } catch (Exception e) {
            throw new MediaServiceException("Failed to save media", e);
        }
    }


    // Download Media ----------------------------------------------------------


    // This method now returns a Pair object containing both the Resource and the MIME type
    public Pair<Resource, String> downloadMedia(@NonNull String mediaId)
    {
        try {
            LOGGER.info("Downloading media: {}", mediaId);

            GridFSFile file = getGridFsFile(mediaId);
            String mimeType = getMimeType(file);

            return Pair.of(gridFsTemplate.getResource(file), mimeType);

        } catch (Exception e) {
            throw new MediaServiceException("Failed to download media", e);
        }
    }


    // Delete Medias -----------------------------------------------------------

    // Deletes all medias from MongoDB GridFS in a _bulk_operation_
    public void deleteMedias(@NonNull List<Media> medias)
    {
        deleteMediaIds(toMediaIds(medias));
    }


    // Deletes all mediaIds from MongoDB GridFS in a _bulk_operation_
    public void deleteMediaIds(@NonNull List<String> mediaIds)
    {
        try {
            LOGGER.info("Deleting {} medias", mediaIds.size());
            mediaIds.forEach(mediaId -> LOGGER.info("Deleting media: {}", mediaId));

            gridFsTemplate.delete(query(where("_id").in(mediaIds)));

        } catch (Exception e) {
            throw new MediaServiceException("Failed to delete medias", e);
        }
    }


    // Deletes a single media from MongoDB GridFS
    public void deleteMedia(@NonNull String mediaId)
    {
        try {
            LOGGER.info("Deleting media: {}", mediaId);
            gridFsTemplate.delete(new Query(where("_id").is(mediaId)));
        } catch (Exception e) {
            throw new MediaServiceException("Failed to delete media", e);
        }
    }


    // Get GridFs File ---------------------------------------------------------

    // Returns a single GridFSFile from MongoDB associated with the mediaId
    public GridFSFile getGridFsFile(@NonNull String mediaId)
    {
        try {
            GridFSFile file = gridFsTemplate.findOne(query(where("_id").is(mediaId)));
            Assert.notNull(file, () -> format("Media not found: %s", mediaId));
            return file;

        } catch (Exception e) {
            throw new MediaServiceException("Failed to get GridFsFile", e);
        }
    }


    // Transaction Helper ------------------------------------------------------

    // This is a helper method to execute a transaction with a list of medias.
    // e.g. save a product along with its medias in a single transaction.
    public <T> T saveMediasWithTransaction(
            MultipartFile[] mediaFiles, Commands.UploadMediaCommand[] mediaMetas,
            Function<List<Media>, T> transaction)
    {
        List<Media> medias = null;
        try {
            medias = saveMedias(mediaFiles, mediaMetas);
            return transaction.apply(medias);
        } catch (Exception e) {
            if (medias != null) rollback(medias);
            throw new MediaServiceException("Media Transaction failed", e);
        }
    }


    // This is a helper method to execute a transaction with a single media.
    // e.g. save a user along with its profile picture in a single transaction.
    public <T> T saveMediaWithTransaction(
            MultipartFile mediaFiles, Commands.UploadMediaCommand mediaMetas,
            Function<Media, T> transaction)
    {
        Media media = null;
        try {
            media = saveMedia(mediaFiles, mediaMetas);
            return transaction.apply(media);
        } catch (Exception e) {
            if (media != null) rollback(List.of(media));
            throw new MediaServiceException("Media Transaction failed", e);
        }
    }


    // Rollback Medias ---------------------------------------------------------

    public void rollback(List<Media> medias)
    {
        LOGGER.warn("Rollback {} medias", medias.size());
        deleteMedias(medias);
    }



    // Mime Type ---------------------------------------------------------------

    // Returns the MIME type of GridFSFile, this is stored in the metadata.
    // We check if the metadata exists and the _contentType key exists otherwise we return the default MIME type.
    private String getMimeType(GridFSFile file)
    {
        return Optional.ofNullable(file.getMetadata()) // could be null
                .map(metadata -> metadata.get("_contentType")) // could be null
                .map(Object::toString)
                .orElse("application/octet-stream");
    }
}
