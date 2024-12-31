package org.programmers.signalbuddy.global.security;

import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.programmers.signalbuddy.domain.member.entity.Member;
import org.programmers.signalbuddy.domain.member.entity.enums.MemberRole;
import org.programmers.signalbuddy.domain.member.entity.enums.MemberStatus;
import org.programmers.signalbuddy.domain.member.repository.MemberRepository;
import org.programmers.signalbuddy.global.support.ServiceTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;


public class LoginTest extends ServiceTest {

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;

    private MockMvc mockMvc;
    private Member member;

    @BeforeEach
    void setup() {

        mockMvc = MockMvcBuilders
            .webAppContextSetup(webApplicationContext)
            .apply(springSecurity())
            .build();

        member = Member.builder()
            .email("test@test.com")
            .nickname("test")
            .profileImageUrl("test.jpeg")
            .memberStatus(MemberStatus.ACTIVITY)
            .role(MemberRole.USER)
            .memberId(null)
            .password(bCryptPasswordEncoder.encode("password"))
            .build();

        memberRepository.save(member);
    }

    @AfterEach
    void clean() {
        memberRepository.delete(member);
    }

    @DisplayName("로그인 성공")
    @Test
    public void login_success() throws Exception {

        String email = "test@test.com";
        String password = "password";

        mockMvc.perform(post("/login")
                .param("username", email)
                .param("password", password))
            .andDo(print())
            .andExpect(authenticated().withUsername("test@test.com"));
    }

    @DisplayName("로그인 실패_존재하지 않는 사용자")
    @Test
    public void login_failure() throws Exception {

        String email = "test1234@test.com";
        String password = "password";

        mockMvc.perform(post("/login")
                .param("username", email)
                .param("password", password))
            .andDo(print())
            .andExpect(redirectedUrl("/members/login?error"));
    }

}
