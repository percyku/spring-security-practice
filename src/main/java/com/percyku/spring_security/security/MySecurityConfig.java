package com.percyku.spring_security.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.csrf.CsrfTokenRequestAttributeHandler;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableWebSecurity
public class MySecurityConfig {

    @Value("${frontend.url.domain}")
    private String domain;
    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Autowired
    private CustomLogoutSuccessHandler customLogoutSuccessHandler;

    @Autowired
    private CustomAuthenticationEntryPoint authenticationEntryPoint;


    @Autowired
    private CustomOAuth2LoginSuccessHandler customOAuth2LoginSuccessHandler;

    @Autowired
    private CustomOAuth2LoginFailHandler customOAuth2LoginFailHandler;


    @Bean
    public DaoAuthenticationProvider authenticationProvider(MyUserDetailService userService) {
        DaoAuthenticationProvider auth = new DaoAuthenticationProvider();
        auth.setUserDetailsService(userService); //set the custom user details service
        auth.setPasswordEncoder(passwordEncoder()); //set the password encoder - bcrypt
        return auth;
    }


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http)throws Exception{
        return http
                .httpBasic(Customizer.withDefaults())
                .formLogin(Customizer.withDefaults())
                // 設定 Session 的創建機制
                .sessionManagement(
                        session ->  session.sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
                )
                // 設定 CSRF 保護
//                .csrf( csrf ->csrf.disable())
                .csrf(csrf -> csrf
                        .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
                        .csrfTokenRequestHandler(createCsrfHandler())
                        .ignoringRequestMatchers("/registerUser","/loginUser")
                )
                // 設定 CORS 跨域
                //.cors(cors -> cors.disable())
                .cors(cors -> cors.configurationSource(createCorsConfig()))
                // 設定 api 的權限控制
                .authorizeHttpRequests(request -> request
                                .requestMatchers("/registerUser","/login").permitAll()
                                .requestMatchers("/loginUser","/logoutUser","/updateUser","/checkUserStatus").authenticated()
                                .anyRequest().denyAll()
                )
                .exceptionHandling(exceptionHandling -> exceptionHandling.authenticationEntryPoint(authenticationEntryPoint))

                .oauth2Login(oath2 -> {
                    oath2
                            .loginPage("/login")
                            .successHandler(customOAuth2LoginSuccessHandler)
                            .failureHandler(customOAuth2LoginFailHandler);
                })
                .logout(logout -> logout
                        .logoutUrl("/logoutUser").logoutSuccessHandler(customLogoutSuccessHandler).invalidateHttpSession(true)
                        .deleteCookies("JSESSIONID")
                        .deleteCookies("XSRF-TOKEN")
                )

                .build();
    }

    private CsrfTokenRequestAttributeHandler createCsrfHandler() {
        CsrfTokenRequestAttributeHandler csrfHandler = new CsrfTokenRequestAttributeHandler();
        csrfHandler.setCsrfRequestAttributeName(null);

        return csrfHandler;
    }


    private CorsConfigurationSource createCorsConfig(){
        CorsConfiguration config= new CorsConfiguration();
//        config.setAllowedOrigins(List.of("*"));
//        config.setAllowedOrigins(List.of("http://127.0.0.1:5500","null"));
//        config.setAllowedOrigins(List.of("http://localhost:3000","http://localhost:5173","null"));
        config.setAllowedOrigins(List.of(domain,"null"));
        config.setAllowedHeaders(List.of("*"));
        config.setAllowedMethods(List.of("*"));
        config.setAllowCredentials(true);
        config.setMaxAge(3600L);
        UrlBasedCorsConfigurationSource source =new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**",config);

        return source;

    }


//
//
//    @Bean
//    public AuthenticationFailureHandler authenticationFailureHandler(){
//        SimpleUrlAuthenticationFailureHandler handler =new SimpleUrlAuthenticationFailureHandler();
//        System.out.println("AuthenticationFailureHandler");;
//        handler.setDefaultFailureUrl("http://localhost:5173/web-layout-training-vite/pages/index.html"+"?id=a");
//        return handler;
//    }


}
