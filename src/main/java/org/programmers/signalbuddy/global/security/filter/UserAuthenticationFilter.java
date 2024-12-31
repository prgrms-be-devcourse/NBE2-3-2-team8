package org.programmers.signalbuddy.global.security.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;
import org.programmers.signalbuddy.global.security.CustomUserDetails;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

public class UserAuthenticationFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
        FilterChain filterChain) throws ServletException, IOException {

        CustomUserDetails userDetails = (CustomUserDetails) request.getSession()
            .getAttribute("user");
        if (userDetails != null) {

            GrantedAuthority authority = new SimpleGrantedAuthority(
                userDetails.getAuthorities().toString());
            Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails,
                null,
                Collections.singleton(authority));
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }
        filterChain.doFilter(request, response);
    }
}
