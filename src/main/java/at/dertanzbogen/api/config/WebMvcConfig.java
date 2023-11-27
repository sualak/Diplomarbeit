package at.dertanzbogen.api.config;

import at.dertanzbogen.api.presentation.annotations.AuthenticatedUserResolver;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer
{
    //TODO namen anpassen
    private static final String[] CLASSPATH_RESOURCE_LOCATIONS = {
            "classpath:/static/admin-panel/",
            "classpath:/static/user-panel/",
            "classpath:/static/checkout/",
    };

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(new AuthenticatedUserResolver());
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/**").addResourceLocations(CLASSPATH_RESOURCE_LOCATIONS);
    }
}
