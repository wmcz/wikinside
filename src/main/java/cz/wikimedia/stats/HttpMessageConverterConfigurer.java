package cz.wikimedia.stats;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
public class HttpMessageConverterConfigurer implements WebMvcConfigurer {
    @Override
    public void extendMessageConverters(List<HttpMessageConverter<?>> converters) {
        MappingJackson2HttpMessageConverter htmlTextConverter = new MappingJackson2HttpMessageConverter();
        htmlTextConverter.setSupportedMediaTypes(List.of(MediaType.TEXT_HTML));
        converters.add(htmlTextConverter);
    }
}