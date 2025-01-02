package org.programmers.signalbuddy.global.config;

import org.programmers.signalbuddy.global.security.filter.UserAuthenticationFilter;
import org.programmers.signalbuddy.global.security.handler.CustomAuthenticationSuccessHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    CustomAuthenticationSuccessHandler customAuthenticationSuccessHandler() {
        return new CustomAuthenticationSuccessHandler();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        // 인가 설정
        http
            .authorizeHttpRequests((auth) -> auth
                // 문서화 api
                .requestMatchers("/",
                    "/swagger-ui/**",
                    "/api-docs/**",
                    "/swagger-resources/**",
                    "/css/**",
                    "/js/**",
                    "/images/**",
                    "/webjars/**").permitAll()
                // 로그인
                .requestMatchers("/members/login").anonymous()
                // 북마크
                .requestMatchers("/api/bookmarks/**").hasRole("USER")
                // 댓글
                .requestMatchers("/api/comments/write").hasAnyRole("USER", "ADMIN")
                .requestMatchers(HttpMethod.PATCH, "/api/comments/").hasAnyRole("USER", "ADMIN")
                .requestMatchers(HttpMethod.DELETE, "/api/comments/").hasAnyRole("USER", "ADMIN")
                .requestMatchers(HttpMethod.GET, "/api/comments/").permitAll()
                // 교차로
                .requestMatchers("/api/crossroads/save").hasRole("ADMIN")
                // 피드백
                .requestMatchers("/api/feedbacks/write").hasAnyRole("USER", "ADMIN")
                .requestMatchers(HttpMethod.PATCH, "/api/feedbacks/").hasAnyRole("USER", "ADMIN")
                .requestMatchers(HttpMethod.DELETE, "/api/feedbacks/").hasAnyRole("USER", "ADMIN")
                .requestMatchers(HttpMethod.GET, "/api/feedbacks/**").permitAll()
                // 회원
                .requestMatchers("/api/admins/**").hasRole("ADMIN")
                .requestMatchers("/api/members/**").hasRole("USER")
                .anyRequest().authenticated()
            );

        // 로그인 관련 설정
        http
            .formLogin((auth) -> auth
                .loginPage("/members/login")
                .loginProcessingUrl("/login")
                // 메인으로 이동하도록 설정
                //.defaultSuccessUrl("/home", true)
                .successHandler(customAuthenticationSuccessHandler())
                .permitAll()
            );

        // 로그아웃 관련 설정
        http
            .logout((auth) -> auth
                .logoutUrl("/logout")
                .logoutSuccessUrl("/login")
                .deleteCookies("JSESSIONID")
                .invalidateHttpSession(true)
                .clearAuthentication(true));

        // 세션 관리 설정
        http
            .sessionManagement(sessionManagement -> {
                // 세션 생성 정책
                sessionManagement.sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED);

                // 중복 로그인 처리
                sessionManagement.maximumSessions(1)
                    .maxSessionsPreventsLogin(true);

                // 세션 고정 공격 방어
                sessionManagement.sessionFixation().changeSessionId();
            });

        // csrf 비활성화
        http.csrf(AbstractHttpConfigurer::disable);

        // 커스텀 필터 추가
        http
            .addFilterBefore(new UserAuthenticationFilter(),
                UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
