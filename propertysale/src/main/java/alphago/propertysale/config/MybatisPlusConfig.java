package alphago.propertysale.config;

import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * @program: propertysale
 * @description: configuration of mybatis plus
 * @author: XIAO HAN
 * @create: 2020-09-26 21:52
 **/
@Configuration
@EnableTransactionManagement
@MapperScan("alphago.propertysale.mapper")
public class MybatisPlusConfig {
        /**
         * 分页
        */
        @Bean
        public PaginationInterceptor paginationInterceptor() {
            return new PaginationInterceptor();
        }
}