package alphago.propertysale.config;

import alphago.propertysale.shiro.JwtFilter;
import alphago.propertysale.shiro.MyRealm;
import org.apache.shiro.mgt.DefaultSessionStorageEvaluator;
import org.apache.shiro.mgt.DefaultSubjectDAO;
import org.apache.shiro.realm.Realm;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.filter.DelegatingFilterProxy;

import javax.servlet.Filter;
import java.util.HashMap;
import java.util.Map;

/**
 * @program: propertysale
 * @description: Shiro Configuration
 * @author: XIAO HAN
 * @create: 2020-09-26 22:11
 **/
@Configuration
public class ShiroConfig {

    @Bean
    ShiroFilterFactoryBean shiroFilterFactoryBean(DefaultWebSecurityManager defaultWebSecurityManager , JwtFilter jwtFilter){
        ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
        shiroFilterFactoryBean.setSecurityManager(defaultWebSecurityManager);
        // 添加过滤器
        Map<String , Filter> filterMap = new HashMap<>();
        filterMap.put("jwtFilter" , jwtFilter);
        shiroFilterFactoryBean.setFilters(filterMap);
        // 添加过滤路径
        Map<String , String> filterChain = new HashMap<>();
        filterChain.put("/**" , "jwtFilter");
        shiroFilterFactoryBean.setFilterChainDefinitionMap(filterChain);
        return shiroFilterFactoryBean;
    }

    @Bean
    DefaultWebSecurityManager defaultWebSecurityManager(Realm realm){
        DefaultWebSecurityManager defaultWebSecurityManager = new DefaultWebSecurityManager();
        defaultWebSecurityManager.setRealm(realm);
        DefaultSubjectDAO subjectDAO = new DefaultSubjectDAO();
        DefaultSessionStorageEvaluator defaultSessionStorageEvaluator = new DefaultSessionStorageEvaluator();
        defaultSessionStorageEvaluator.setSessionStorageEnabled(false);
        subjectDAO.setSessionStorageEvaluator(defaultSessionStorageEvaluator);
        defaultWebSecurityManager.setSubjectDAO(subjectDAO);
        return defaultWebSecurityManager;
    }

    @Bean
    Realm realm(){
        MyRealm myRealm = new MyRealm();
        // Enable Cache

        return myRealm;
    }

    @Bean
    public FilterRegistrationBean delegatingFilterProxy(){
        FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean();
        DelegatingFilterProxy proxy = new DelegatingFilterProxy();
        proxy.setTargetFilterLifecycle(true);
        proxy.setTargetBeanName("shiroFilterFactoryBean");
        filterRegistrationBean.setFilter(proxy);
        return filterRegistrationBean;
    }

}