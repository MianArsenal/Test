package com.mian.spring.security.action.helloword.configuration;

import com.mian.spring.security.action.helloword.authentication.handler.CaptchaExceptionHandler;
import com.mian.spring.security.action.helloword.view.CustomViewResolver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationDetailsSource;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.servlet.ViewResolver;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@Configuration
@EnableWebSecurity
public class WebSecurityConfigCsrf extends WebSecurityConfigurerAdapter {

    private final AuthenticationDetailsSource<HttpServletRequest, WebAuthenticationDetails> captchaWebAuthenticationDetailsSource;

    private final AuthenticationProvider captchaAuthenticationProvider;

    public WebSecurityConfigCsrf(@Qualifier("captchaWebAuthenticationDetailsSource") AuthenticationDetailsSource<HttpServletRequest, WebAuthenticationDetails> captchaWebAuthenticationDetailsSource, @Qualifier("captchaAuthenticationProvider") AuthenticationProvider captchaAuthenticationProvider) {
        this.captchaWebAuthenticationDetailsSource = captchaWebAuthenticationDetailsSource;
        this.captchaAuthenticationProvider = captchaAuthenticationProvider;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(this.captchaAuthenticationProvider);

    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers("/admin/api/**").hasRole("ADMIN")
                .antMatchers("/user/api/**").hasRole("USER")
                .antMatchers("/cors/**").hasRole("ADMIN")
                .antMatchers("/csrfAnswer").hasRole("ADMIN")
                .antMatchers("/csrf").hasRole("ADMIN")
                .antMatchers("/app/api/**", "/captcha.jpg", "/login").permitAll()
                .anyRequest().authenticated()
                .and()
                //启用cors支持
                .cors()
                .and()
                .formLogin()
                //应用detail source包装额外信息到user detail，等到provider调用时获得额外信息进行额外验证
                .authenticationDetailsSource(this.captchaWebAuthenticationDetailsSource)
                .loginPage("/login").permitAll()
                .loginProcessingUrl("/login").permitAll()
                .successHandler(new AuthenticationSuccessHandler() {
                    @Override
                    public void onAuthenticationSuccess(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Authentication authentication) throws IOException, ServletException {
                        httpServletResponse.setContentType("application/json;charset=UTF-8");
                        PrintWriter o = httpServletResponse.getWriter();
                        o.write("{\"error code\":\"0\", \"message\":\"登录成功！\"}");
                    }
                })
                .failureHandler(new CaptchaExceptionHandler())
                .permitAll()
//                .and()
//                .csrf().ignoringAntMatchers("/csrfAnswer")
                //当使用csrf时，spring security 的 CsrfFilter 会被修改，只能处理 post 请求，下面配置是为了方便使用 get 请求，但不推荐这样配置
                .and().logout().logoutRequestMatcher(new AntPathRequestMatcher("/logout"));
    }

    @Bean
    public ViewResolver viewResolver() {
        return new CustomViewResolver();
    }
}