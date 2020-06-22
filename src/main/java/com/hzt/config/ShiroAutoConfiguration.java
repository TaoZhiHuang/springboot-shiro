package com.hzt.config;

import at.pollux.thymeleaf.shiro.dialect.ShiroDialect;
import com.hzt.realm.UserRealm;
import org.apache.shiro.authc.credential.CredentialsMatcher;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.servlet.DispatcherServletAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.filter.DelegatingFilterProxy;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
* 
*springboot的shiro配置类
* @Author:hzt
* @Date:9:46 下午 2020/6/16
*/
@Configuration
@EnableConfigurationProperties(ShiroProperties.class)
public class ShiroAutoConfiguration {

    @Autowired
    private  ShiroProperties shiroProperties;

    /**
     * 声明凭证匹配器
     * @return
     */
    @Bean
    public HashedCredentialsMatcher credentialsMatcher(){
        HashedCredentialsMatcher hashedCredentialsMatcher = new HashedCredentialsMatcher();
        hashedCredentialsMatcher.setHashAlgorithmName(shiroProperties.getHashAlgorithmName());
        hashedCredentialsMatcher.setHashIterations(shiroProperties.getHashIterations());
        return hashedCredentialsMatcher;
    }

    /**
     * 创建realm
     * @param credentialsMatcher
     * @return
     */
    @Bean
    public UserRealm realm(CredentialsMatcher credentialsMatcher){
        UserRealm userRealm = new UserRealm();
        userRealm.setCredentialsMatcher(credentialsMatcher);
        return userRealm;
    }

    /**
     * 声明安全管理器
     * @param userRealm
     * @return
     */
    @Bean("securityManager")
    public SecurityManager securityManager(UserRealm userRealm){
        DefaultWebSecurityManager securityManager=new DefaultWebSecurityManager();
        securityManager.setRealm(userRealm);
        return securityManager;
    }

    @Bean("shiroFilter")
    public ShiroFilterFactoryBean shiroFilterFactoryBean(SecurityManager securityManager){
        ShiroFilterFactoryBean bean = new ShiroFilterFactoryBean();
        //注入安全管理器
        bean.setSecurityManager(securityManager);
        //注入登录页面地址
        bean.setLoginUrl(shiroProperties.getLoginUrl());
        //注入未授权的页面
        bean.setUnauthorizedUrl(shiroProperties.getUnauthorizedUrl());
        //注入url过滤器
        Map<String, String> filterChainDefinitionMap = new LinkedHashMap();
        //注入放行地址
        if(shiroProperties.getAnonUrls()!=null&&shiroProperties.getAnonUrls().length>0){
            String[] anonUrls = shiroProperties.getAnonUrls();
            for (String anonUrl : anonUrls) {
                filterChainDefinitionMap.put(anonUrl, "anon");
            }
        }
        //注入登出地址
        if(shiroProperties.getLogoutUrl()!=null){
            filterChainDefinitionMap.put(shiroProperties.getLogoutUrl(), "logout");
        }
        //注入拦截地址
        String[] authcUrls = shiroProperties.getAuthcUrls();
        if(authcUrls!=null&&authcUrls.length>0){
            for (String authcUrl : authcUrls) {
                filterChainDefinitionMap.put(authcUrl, "authc");
            }
        }
        bean.setFilterChainDefinitionMap(filterChainDefinitionMap);
        return bean;
    }

    //注册过滤器
    @Bean
    public FilterRegistrationBean<DelegatingFilterProxy> filterProxyFilterRegistrationBean(){
        //创建注册器
        FilterRegistrationBean<DelegatingFilterProxy> bean=new FilterRegistrationBean<>();
        //创建过滤器
        DelegatingFilterProxy proxy = new DelegatingFilterProxy();
        bean.setFilter(proxy);
        //加入属性
        bean.addInitParameter("targetFilterLifecycle","true");
        bean.addInitParameter("targetBeanName","shiroFilter");
        //放入springMVC
        List<String> servletNames=new ArrayList<>();
        servletNames.add(DispatcherServletAutoConfiguration.DEFAULT_DISPATCHER_SERVLET_BEAN_NAME);
        bean.setServletNames(servletNames);
        return bean;
    }



    /**
     * 这里是为了能在html页面引用shiro标签，必须添加，不然会报错
     */
    @Bean(name = "shiroDialect")
    public ShiroDialect shiroDialect() {
        return new ShiroDialect();
    }



 }
