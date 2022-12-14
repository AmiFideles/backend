package com.example.demo.security.config;

import com.example.demo.jwt.JwtHelper;
import com.example.demo.security.AccessTokenEntryPoint;
import com.example.demo.security.filter.AccessTokenFilter;
import com.example.demo.security.provider.JwtAuthenticationProvider;
import com.example.demo.security.userDetails.UserDetailsServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@RequiredArgsConstructor
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig {
    private final UserDetailsServiceImpl userService;
    private final AccessTokenEntryPoint accessTokenEntryPoint;
    private final JwtHelper jwtHelper;


    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity httpSecurity) throws Exception {
        AuthenticationManagerBuilder authenticationManagerBuilder =
                httpSecurity.getSharedObject(AuthenticationManagerBuilder.class);
        authenticationManagerBuilder.authenticationProvider(jwtAuthenticationProvider());
        authenticationManagerBuilder.userDetailsService(userService);
        return authenticationManagerBuilder.build();
    }

    @Bean
    public AccessTokenFilter accessTokenFilter() {
        return new AccessTokenFilter();
    }

/*    @Bean
  //  @Override
    protected AuthenticationManager authenticationManager() throws Exception {
        return super.authenticationManager();
    }*/


    @Bean
    public JwtAuthenticationProvider jwtAuthenticationProvider() {
        return new JwtAuthenticationProvider(jwtHelper, userService);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

/*    //@Override
    @Bean
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(jwtAuthenticationProvider())
                .userDetailsService(userService)
                .passwordEncoder(passwordEncoder());
    }*/

    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider() {
        val provider = new DaoAuthenticationProvider();
        provider.setPasswordEncoder(passwordEncoder());
        provider.setUserDetailsService(userService);
        return provider;
    }

    @Bean
    @Order(1)
    protected SecurityFilterChain registerFilterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
                .requestMatchers()
                .antMatchers("/api/auth/signup",
                        "/api/auth/logout", "/api/auth/logout-all", "/api/auth/logout-all", "/api/auth/access-token", "/api/auth/refresh-token").and()
                .csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests().anyRequest().permitAll()
                .and().build();
    }
// TODO authorize http request
    @Bean
    @Order(2)
    protected SecurityFilterChain loginFilterChain(AccessTokenEntryPoint accessTokenEntryPoint, HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .antMatcher("/api/auth/login")
                .csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .httpBasic().authenticationEntryPoint(accessTokenEntryPoint)
                .and()
                .authorizeRequests().anyRequest().authenticated();
        return httpSecurity.build();


/*                .requestMatchers()
                .antMatchers("api/auth/login").and()
                .csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .httpBasic().authenticationEntryPoint(accessTokenEntryPoint).and()
                .authorizeRequests().anyRequest().authenticated();*/
       // return httpSecurity.build();

    }

    @Bean
    @Order(3)
    protected SecurityFilterChain apiFilterChain(AccessTokenEntryPoint accessTokenEntryPoint, HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests()
                .antMatchers("/api/auth/signup", "/api/auth/logout", "/api/auth/logout-all", "/api/auth/logout-all").permitAll()
                .antMatchers("/api/users/test").hasAnyAuthority(ADMIN)
                .antMatchers("/", "/error", "/csrf", "/swagger-ui.html", "/swagger-ui/**", "/v3/api-docs", "/v3/api-docs/**").permitAll()
                .anyRequest().authenticated()
                .and()
                .addFilterBefore(accessTokenFilter(), UsernamePasswordAuthenticationFilter.class)
                .exceptionHandling().authenticationEntryPoint(accessTokenEntryPoint);
        return httpSecurity.build();
/*        httpSecurity
                .antMatcher("/api/users/**")
                .csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .addFilterBefore(accessTokenFilter(), UsernamePasswordAuthenticationFilter.class)
                .exceptionHandling().authenticationEntryPoint(accessTokenEntryPoint);
        return httpSecurity.build();*/
    }
/*    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.cors().and().csrf().disable()
                .exceptionHandling().authenticationEntryPoint(accessTokenEntryPoint).and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
                .antMatcher("/api/auth/login").httpBasic().and()
                .authorizeRequests().antMatchers("/api/auth/signup").permitAll()
                .antMatchers("/api/auth/logout").permitAll()
                .antMatchers("/api/users/test").hasAnyAuthority(ADMIN)
                .antMatchers("/", "/error", "/csrf", "/swagger-ui.html", "/swagger-ui/**", "/v3/api-docs", "/v3/api-docs/**").permitAll()
                .anyRequest().authenticated();
        http.addFilterBefore(accessTokenFilter(), UsernamePasswordAuthenticationFilter.class);
    }*/

    public static final String ADMIN = "ADMIN";
    public static final String USER = "USER";
}
