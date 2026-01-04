package com.percyku.spring_security.security;

import com.percyku.spring_security.dao.UserRepository;
import com.percyku.spring_security.model.User;
import com.percyku.spring_security.model.UserRole;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.*;

@Component
public class CustomOAuth2LoginSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {
//public class OAuth2LoginSuccessHandler  extends SimpleUrlAuthenticationSuccessHandler {


    @Autowired
    UserRepository userRepository;


    @Value("${frontend.url.domain}")
    private String domain;

    @Value("${frontend.url.detail}")
    private String urlDetail;
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws ServletException, IOException {

        OAuth2AuthenticationToken oAuth2AuthenticationToken =(OAuth2AuthenticationToken) authentication;
        String id= "";



        if("github".equals(oAuth2AuthenticationToken.getAuthorizedClientRegistrationId()) ||
                "google".equals(oAuth2AuthenticationToken.getAuthorizedClientRegistrationId())){

            DefaultOAuth2User principal = (DefaultOAuth2User) authentication.getPrincipal();
            Map<String, Object> attributes = principal.getAttributes();
//            attributes.forEach((key, value) -> {
//                System.out.println(key + "=" + value + " ");
//            });


            String email = attributes.getOrDefault("email", "").toString();
            String name = attributes.getOrDefault("name", "").toString();

            if("github".equals(oAuth2AuthenticationToken.getAuthorizedClientRegistrationId())){
                id=attributes.getOrDefault("id", "").toString();
            }else if("google".equals(oAuth2AuthenticationToken.getAuthorizedClientRegistrationId())){
                id=attributes.getOrDefault("sub", "").toString();
            }





            Optional<User> optionalUser =userRepository.findUserWithRoleByEmail(email);

            if(!optionalUser.isPresent()){
                User tmpUser =new User(name,"",email,"","");
                userRepository.save(tmpUser);


                DefaultOAuth2User newUser = new DefaultOAuth2User(List.of(new SimpleGrantedAuthority(tmpUser.getEmail())),
                        attributes, "id");
                Authentication securityAuth = new OAuth2AuthenticationToken(newUser, List.of(new SimpleGrantedAuthority(tmpUser.getEmail())),
                        oAuth2AuthenticationToken.getAuthorizedClientRegistrationId());
                SecurityContextHolder.getContext().setAuthentication(securityAuth);
            }else{
                User existUser = optionalUser.get();


                for (UserRole userRole : existUser.getUser_role()) {
                    DefaultOAuth2User newUser = new DefaultOAuth2User(List.of(new SimpleGrantedAuthority(userRole.getRole().getName())),
                            attributes, "id");
                    Authentication securityAuth = new OAuth2AuthenticationToken(newUser, List.of(new SimpleGrantedAuthority(userRole.getRole().getName())),
                            oAuth2AuthenticationToken.getAuthorizedClientRegistrationId());
                    SecurityContextHolder.getContext().setAuthentication(securityAuth);
                }
            }




        }

        this.setAlwaysUseDefaultTargetUrl(true);
        //domain :ex: http://localhost:5173/
        //urlDetail :ex: web-layout-XXX/XXX/XXX/page.html
        this.setDefaultTargetUrl(domain+urlDetail+"?id="+id);
        super.onAuthenticationSuccess(request, response, authentication);
    }

}