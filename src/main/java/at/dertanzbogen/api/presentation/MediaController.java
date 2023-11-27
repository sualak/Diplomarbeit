package at.dertanzbogen.api.presentation;

import at.dertanzbogen.api.service.MediaService;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/media")
@RequiredArgsConstructor
public class MediaController
{
    private final MediaService mediaService;

    // On the frontend, we can use the following to display the image:
    // <img src="http://localhost:8080/api/media/1234">

    // HTTP Request:
    // GET /api/media/1234

    // HTTP Response Headers:
    // 200 OK
    // Content-Type: image/jpeg
    // Content-Length: 262601

    @GetMapping("/{mediaId}")
    public ResponseEntity<Resource> downloadMedia(@PathVariable String mediaId)
    {
        var result = mediaService.downloadMedia(mediaId);

        Resource resource = result.getFirst();
        String mimeType = result.getSecond();

        return ResponseEntity.ok()
                // If no Content-Type header is set then application/json is by default
                .contentType(MediaType.parseMediaType(mimeType))
                .body(resource);
    }
}
