package com.amt.wechat;

import org.apache.catalina.Context;
import org.apache.catalina.connector.Connector;
import org.apache.tomcat.util.descriptor.web.SecurityCollection;
import org.apache.tomcat.util.descriptor.web.SecurityConstraint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;

@SpringBootApplication
public class WechatApplication extends  SpringBootServletInitializer{
    public static void main(String[] args) {
        SpringApplication.run(WechatApplication.class, args);
    }

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(WechatApplication.class);
    }

    @Profile("pro")
    @Bean
    public Connector connector4Pro(){
        Connector connector=new Connector("org.apache.coyote.http11.Http11AprProtocol");
        connector.setPort(80);
        return connector;
    }

    @Profile("dev")
    @Bean
    public Connector connector4Dev(){
        Connector connector=new Connector("org.apache.coyote.http11.Http11AprProtocol");
        connector.setPort(80);

        connector.setScheme("https");
        connector.setSecure(true);
        connector.setRedirectPort(443);

        // sslProtocol 	指定套接字（Socket）使用的加密/解密协议，默认值为TLS，用户不应该修改这个默认值。
        //connector.setAttribute("sslProtocol", "TLS");
        // ciphers 	指定套接字可用的用于加密的密码清单，多个密码间以逗号（,）分隔。如果此项没有设定，在默认情况下，套接字可以使用任意一个可用的密码。
        return connector;
    }

    @Bean
    public TomcatServletWebServerFactory tomcatServletWebServerFactory(@Autowired Connector connector){
        TomcatServletWebServerFactory tomcat = new TomcatServletWebServerFactory(){
            @Override
            protected void postProcessContext(Context context) {
                SecurityConstraint securityConstraint=new SecurityConstraint();
                securityConstraint.setUserConstraint("CONFIDENTIAL");
                SecurityCollection collection=new SecurityCollection();
                collection.addPattern("/*");
                securityConstraint.addCollection(collection);
                context.addConstraint(securityConstraint);
            }
        };
        tomcat.addAdditionalTomcatConnectors(connector);
        return tomcat;
    }
}
