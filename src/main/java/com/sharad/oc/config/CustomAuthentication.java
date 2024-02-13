package com.sharad.oc.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import java.io.IOException;

public class CustomAuthentication extends BasicAuthenticationFilter {


    public CustomAuthentication(AuthenticationManager authenticationManager) {
        super(authenticationManager);
    }

    protected void onSuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, Authentication authResult) throws IOException {
        response.sendRedirect("/about");
    }
}