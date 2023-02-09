package com.example.test.config;


import com.example.test.Service.AccountService;
import com.example.test.Service.impl.AuthService;
import com.example.test.enetiy.Account;
import com.example.test.repo.AccountRepository;
import com.example.test.repo.RedisTokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.thymeleaf.extras.springsecurity5.dialect.SpringSecurityDialect;
import org.thymeleaf.spring5.SpringTemplateEngine;
import org.thymeleaf.templateresolver.ITemplateResolver;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@Configuration
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Resource
    AuthService service;

    @Resource
    RedisTokenRepository repository;

    @Resource
    AccountRepository accountRepository;

    @Resource
    AccountService accountService;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.
                authorizeRequests()
                .antMatchers("/login","/static/**","/register","/forgot-pws")
                .permitAll()
                .antMatchers("/page/user/**", "/api/user/**").hasRole("user")
                .antMatchers("/page/admin/**", "/api/admin/**").hasRole("admin")
                .and()
                .formLogin()
                .loginPage("/login")
                .loginProcessingUrl("/doLogin")
                .successHandler(this::onAuthenticationSuccess)
                .and()
                .logout()
                .logoutUrl("/logout")
                .logoutSuccessUrl("/login")
                .and()
                .rememberMe()
                .tokenRepository(repository)
                .and()
                .csrf()
                .disable();

    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth
                .userDetailsService(service)
                .passwordEncoder(new BCryptPasswordEncoder());
    }

    private void onAuthenticationSuccess(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Authentication authentication) throws IOException, ServletException {
        HttpSession session = httpServletRequest.getSession();
        Account user = accountRepository.findAccountByUsername(authentication.getName());
        session.setAttribute("user", user);

        if (user.getRole().equals("admin")) {
            httpServletResponse.sendRedirect("page/admin/index");
        } else {
            if(accountService.findUser(session).getAccountDetail()==null){
                httpServletResponse.sendRedirect("page/user/addaccount_detail");
            }else {
                httpServletResponse.sendRedirect("page/user/index");
            }
        }
    }

    //配置模板引擎Bean  Thymeleaf的Security拓展
    @Bean
    public SpringTemplateEngine springTemplateEngine(@Autowired ITemplateResolver resolver){
        SpringTemplateEngine engine = new SpringTemplateEngine();
        engine.setTemplateResolver(resolver);
        engine.addDialect(new SpringSecurityDialect());   //添加针对于SpringSecurity的方言
        return engine;
    }
}
