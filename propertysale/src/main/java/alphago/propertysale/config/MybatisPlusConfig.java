package alphago.propertysale.config;

import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * @program: propertysale
 * @description: Configuration of mybatis plus
 *               Enable PaginationInterceptor.
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