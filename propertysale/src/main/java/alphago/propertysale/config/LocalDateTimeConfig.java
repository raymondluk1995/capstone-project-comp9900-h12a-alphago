package alphago.propertysale.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.util.StringUtils;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

/**
 * @program: propertysale
 * @description:
 * @author: XIAO HAN
 * @create: 2020-10-25 21:06
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