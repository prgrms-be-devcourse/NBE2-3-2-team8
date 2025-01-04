package org.programmers.signalbuddy.global.security.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collection;
import org.programmers.signalbuddy.global.security.basic.CustomUserDetails;
import org.programmers.signalbuddy.global.security.oauth.CustomOAuth2User;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

public class UserAuthenticationFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
        FilterChain filterChain) throws ServletException, IOException {

        Object user = request.getSession().getAttribute("user");

        if (user instanceof CustomUserDetails) {
            CustomUserDetails customUserDetails = (CustomUserDetails) user;
            setAuthentication(customUserDetails, customUserDetails.getAuthorities());

        } else if (user instanceof CustomOAuth2User) {
            CustomOAuth2User customOAuth2User = (CustomOAuth2User) user;
            setAuthentication(customOAuth2User, customOAuth2User.getAuthorities());
        }

        filterChain.doFilter(request, response);
    }

    private void setAuthentication(Object principal,
        Collection<? extends GrantedAuthority> authorities) {
        Authentication authentication = new UsernamePasswordAuthenticationToken(principal, null,
            authorities);
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }
}
