package com.hzt.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
*
*
* @Author:hzt
* @Date:9:49 下午 2020/6/16
*/
@ConfigurationProperties(prefix = "shiro")
@Data
public class ShiroProperties {
    //加密方法
    private String hashAlgorithmName="md5";
    //散列次数
    private Integer hashIterations=2;
    //登录路径
    private String loginUrl;
    //未授权的跳转页面
    private String unauthorizedUrl;
    //放行路径
    private String [] anonUrls;
    //登出路径
    private String  logoutUrl;
    //需要认证的url
    private String [] authcUrls;
}
