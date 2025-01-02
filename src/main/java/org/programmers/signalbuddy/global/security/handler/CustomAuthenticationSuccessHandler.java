package org.programmers.signalbuddy.global.security.handler;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.programmers.signalbuddy.global.security.basic.CustomUserDetails;
import org.programmers.signalbuddy.global.security.oauth.CustomOAuth2User;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;

public class CustomAuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
        Authentication authentication) throws IOException, ServletException {

        Object principal = authentication.getPrincipal();
        // 임시 경로
        String redirectUrl = "/feedbacks";

        if (principal instanceof CustomUserDetails) {
            CustomUserDetails customUserDetails = (CustomUserDetails) principal;

            if (customUserDetails.getRole().name().contains("ADMIN")) {
                // 임시 경로
                redirectUrl = "/admins/members/list";
            }
            request.getSession().setAttribute("user", customUserDetails);
        } else if (principal instanceof CustomOAuth2User) {
            CustomOAuth2User customOAuth2User = (CustomOAuth2User) principal;
            request.getSession().setAttribute("user", customOAuth2User);
        }

        request.getSession().setMaxInactiveInterval(3600);
        response.sendRedirect(redirectUrl);

        super.onAuthenticationSuccess(request, response, authentication);
    }
}
