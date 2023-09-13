package com.goit.security.config;

import com.goit.security.service.CustomOidcUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.LogoutConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import javax.sql.DataSource;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(
    prePostEnabled = true,
    securedEnabled = true,
    jsr250Enabled = true)
public class WebSecConfig {
  private final DataSource dataSource;
  private final CustomOidcUserService customOidcUserService;

  public WebSecConfig(DataSource dataSource, CustomOidcUserService customOidcUserService) {
    this.dataSource = dataSource;
    this.customOidcUserService = customOidcUserService;
  }

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    http.csrf(AbstractHttpConfigurer::disable)
        .authorizeHttpRequests(requests ->
            requests
                .requestMatchers("/").permitAll()
                .requestMatchers("/public/**").permitAll()
                .requestMatchers("/user/**").permitAll()
                .anyRequest().authenticated()
        )
        .formLogin(form -> form
            .loginPage("/login")
            .permitAll())
        .logout(LogoutConfigurer::permitAll)
        .oauth2Login(oauth2Login -> oauth2Login
            .userInfoEndpoint(userInfo -> userInfo.oidcUserService(customOidcUserService))
        );

    return http.build();
  }

 @Autowired
  public void configAuthentication(AuthenticationManagerBuilder auth) throws Exception {
    auth.jdbcAuthentication().passwordEncoder(new BCryptPasswordEncoder())
        .dataSource(dataSource)
        .usersByUsernameQuery("select username, password, enabled from users where username=?")
        .authoritiesByUsernameQuery("select username, role from users where username=?")
    ;
  }
}
