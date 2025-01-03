package org.programmers.signalbuddy.global.security.oauth;

import lombok.RequiredArgsConstructor;
import org.programmers.signalbuddy.domain.member.entity.Member;
import org.programmers.signalbuddy.domain.member.entity.enums.MemberRole;
import org.programmers.signalbuddy.domain.member.entity.enums.MemberStatus;
import org.programmers.signalbuddy.domain.member.repository.MemberRepository;
import org.programmers.signalbuddy.domain.social.entity.SocialProvider;
import org.programmers.signalbuddy.domain.social.repository.SocialProviderRepository;
import org.programmers.signalbuddy.global.security.oauth.response.GoogleResponse;
import org.programmers.signalbuddy.global.security.oauth.response.KakaoResponse;
import org.programmers.signalbuddy.global.security.oauth.response.NaverResponse;
import org.programmers.signalbuddy.global.security.oauth.response.OAuth2Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private static final Logger log = LoggerFactory.getLogger(CustomOAuth2UserService.class);
    private final MemberRepository memberRepository;
    private final SocialProviderRepository socialProviderRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {

        OAuth2User oAuth2User = super.loadUser(userRequest);
        System.out.println(oAuth2User.getAttributes());

        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        log.info("registrationId: {}", registrationId);
        OAuth2Response oAuth2Response;

        if (registrationId.equals("naver")) {
            oAuth2Response = new NaverResponse(oAuth2User.getAttributes());
        } else if (registrationId.equals("google")) {
            oAuth2Response = new GoogleResponse(oAuth2User.getAttributes());
        } else if (registrationId.equals("kakao")) {
            oAuth2Response = new KakaoResponse(oAuth2User.getAttributes());
        } else {
            oAuth2Response = null;
            return null;
        }

        // 기존 사용자 조회, 없으면 소셜의 이메일과 닉네임을 기반으로 새로운 사용자 생성
        String email = oAuth2Response.getEmail();
        Member saveMember = memberRepository.findByEmail(email)
            .orElseGet(() -> {
                Member newMember = Member.builder()
                    .email(email)
                    .nickname(oAuth2Response.getName())
                    .profileImageUrl("static/images/member/profile-icon.png")
                    .role(MemberRole.USER)
                    .memberStatus(MemberStatus.ACTIVITY)
                    .build();
                memberRepository.save(newMember);
                return newMember;
            });

        // 이미 소셜이 저장되어 있는 경우, 중복 저장하지 않음.
        if(!socialProviderRepository.existsByOauthProviderAndSocialId(oAuth2Response.getProvider(), oAuth2Response.getProviderId())){

            SocialProvider socialProvider = SocialProvider.builder()
                .oauthProvider(oAuth2Response.getProvider())
                .socialId(oAuth2Response.getProviderId())
                .member(saveMember)
                .build();

            socialProviderRepository.save(socialProvider);
        }

        return new CustomOAuth2User(oAuth2Response, saveMember.getMemberId(), email,
            saveMember.getPassword(), saveMember.getProfileImageUrl(), saveMember.getNickname(),
            saveMember.getRole(), saveMember.getMemberStatus());
    }
}
