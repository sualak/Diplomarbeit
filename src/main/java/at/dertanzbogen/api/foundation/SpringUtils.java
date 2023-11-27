package at.dertanzbogen.api.foundation;

import at.dertanzbogen.api.domain.main.BaseEntity;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.Serializable;
import java.net.URI;

public class SpringUtils {

    public static <T> T getBean(ApplicationContext context, Class<T> beanClass) {
        return context.getBean(beanClass);
    }

    public static boolean hasBean(ApplicationContext context, Class<?> beanClass) {
        return context.getBeanNamesForType(beanClass).length > 0;
    }

    public static <T> T getBeanOrNull(ApplicationContext context, Class<T> beanClass)
    {
        try {
            return context.getBean(beanClass);
        } catch (BeansException e) {
            return null;
        }
    }

    public static <PK extends Serializable> URI getLocationUri(PK id)
    {
        return ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(id)
                .toUri();
    }

    public static <T extends BaseEntity> URI getLocationUri(T domain)
    {
        return getLocationUri(domain.getId());
    }

    public static MultipartFile[] getMultipart(MultipartFile[] mediaFiles)
    {
        // Spring will inject null if no media files are provided
        if (mediaFiles == null) mediaFiles = new MultipartFile[0];
        return mediaFiles;
    }
}
