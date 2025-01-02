package org.programmers.signalbuddy.global.security.handler;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.programmers.signalbuddy.global.security.CustomUserDetails;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;

public class CustomAuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
        Authentication authentication) throws IOException, ServletException {

        Object principal = authentication.getPrincipal();
        // 임시 경로
        String redirectUrl = "/feedbacks";

        if(principal instanceof  CustomUserDetails) {
            CustomUserDetails customUserDetails = (CustomUserDetails) principal;

            if(customUserDetails.getRole().name().contains("ADMIN")){
                // 임시 경로
                redirectUrl = "/admins/members/list";
            }
            request.getSession().setAttribute("user", customUserDetails);
        }

        request.getSession().setMaxInactiveInterval(3600);
        response.sendRedirect(redirectUrl);

        super.onAuthenticationSuccess(request, response, authentication);
    }
}
