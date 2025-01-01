package org.programmers.signalbuddy.global.security;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.programmers.signalbuddy.global.support.ServiceTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

public class AuthTest extends ServiceTest {

    @Autowired
    private WebApplicationContext webApplicationContext;
    private MockMvc mockMvc;

    @BeforeEach
    void setup() {

        mockMvc = MockMvcBuilders
            .webAppContextSetup(webApplicationContext)
            .apply(springSecurity())
            .build();
    }

    @WithMockUser(username = "test@test.com", roles = "USER")
    @DisplayName("인가되지 않은 사용자의 접근")
    @Test
    public void access_failure() throws Exception {
        mockMvc.perform(get("/api/admins/members")).andExpect(status().isForbidden());
    }

    @WithMockUser(username = "admin@test.com", roles = "ADMIN")
    @DisplayName("인가된 사용자의 접근")
    @Test
    public void access_success() throws Exception {
        mockMvc.perform(get("/api/admins/members")).andExpect(status().isOk());
    }
}
