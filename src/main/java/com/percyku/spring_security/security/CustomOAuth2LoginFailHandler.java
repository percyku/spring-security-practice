package com.percyku.spring_security.security;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class CustomOAuth2LoginFailHandler extends SimpleUrlAuthenticationFailureHandler {
    @Value("${frontend.url.domain}")
    private String domain;

    @Value("${frontend.url.detail}")
    private String urlDetail;
    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        //domain :ex: http://localhost:5173/
        //urlDetail :ex: web-layout-XXX/XXX/XXX/page.html
        this.setDefaultFailureUrl(domain+urlDetail+"?errorMsg=login fail");
        super.onAuthenticationFailure(request, response, exception);
    }
}
