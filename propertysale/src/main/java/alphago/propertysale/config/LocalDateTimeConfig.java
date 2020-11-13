package alphago.propertysale.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.util.StringUtils;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

/**
 * This configuration is the convertor to change
 * the Timestamp from Front-end to the LocalDateTime class
 **/
@Configuration
public class LocalDateTimeConfig {

    @Bean
    public Converter<String, LocalDateTime> LocalDateTimeConverter(){
        return new Converter<String, LocalDateTime>() {
            @Override
            public LocalDateTime convert(String source) {
                System.out.println(source);
                if (StringUtils.isEmpty(source)) {
                    return null;
                }
                return Instant
                        .ofEpochMilli(Long.parseLong(source))
                        .atZone(ZoneOffset.systemDefault())
                        .toLocalDateTime();
            }
        };
    }

}