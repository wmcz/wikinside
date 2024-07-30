package cz.wikimedia.stats;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;

import java.util.List;

@Configuration(proxyBeanMethods = false)
public class HttpMessageConverterConfig {
    @Bean
    public HttpMessageConverter<Object> converter() {
        MappingJackson2HttpMessageConverter htmlTextConverter = new MappingJackson2HttpMessageConverter();
        htmlTextConverter.setSupportedMediaTypes(List.of(MediaType.TEXT_HTML));
        htmlTextConverter.getSupportedMediaTypes().forEach(System.out::println);
        return htmlTextConverter;
    }
}