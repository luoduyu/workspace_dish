package com.amt.wechat.domain.config;

import com.amt.wechat.service.poi.IPOIUserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.annotation.Resource;

/**
 * Copyright (c) 2018 by CANSHU
 *
 * @author adu Create on 2018-12-28 17:46
 * @version 1.0
 */
@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {
    private @Resource IPOIUserService poiUserService;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // 所有地址不设防，可任意访问：
        //http.authorizeRequests().anyRequest().permitAll().and().csrf().disable();

        http.authorizeRequests().anyRequest().authenticated()
                .and().formLogin().and().httpBasic().and().csrf().disable();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(poiUserService).passwordEncoder(new BCryptPasswordEncoder());
    }

    @Bean
    @Override
    public UserDetailsService userDetailsServiceBean() throws Exception {
        return poiUserService;
    }
}