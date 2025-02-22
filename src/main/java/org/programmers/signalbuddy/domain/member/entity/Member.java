package org.programmers.signalbuddy.domain.member.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.programmers.signalbuddy.domain.basetime.BaseTimeEntity;
import org.programmers.signalbuddy.domain.member.dto.MemberUpdateRequest;
import org.programmers.signalbuddy.domain.member.entity.enums.MemberRole;
import org.programmers.signalbuddy.domain.member.entity.enums.MemberStatus;
import org.programmers.signalbuddy.global.dto.CustomUser2Member;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Entity(name = "members")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
@ToString
public class Member extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long memberId;

    @Column(nullable = false)
    private String email;

    private String password;

    @Column(nullable = false)
    private String nickname;

    private String profileImageUrl;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private MemberRole role;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private MemberStatus memberStatus;

    // 관리자인지 확인
    public static boolean isAdmin(Member member) {
        return MemberRole.ADMIN.equals(member.getRole());
    }

    // 요청자와 작성자가 다른 경우
    public static boolean isNotSameMember(CustomUser2Member user, Member member) {
        return !user.getMemberId().equals(member.getMemberId());
    }

    public void updateMember(MemberUpdateRequest request, String encodedPassword, String profileImageUrl) {
        if (request.getEmail() != null) {
            this.email = request.getEmail();
        }
        if (request.getPassword() != null) {
            this.password = encodedPassword;
        }
        if (request.getNickname() != null) {
            this.nickname = request.getNickname();
        }
        if (profileImageUrl != null) {
            this.profileImageUrl = profileImageUrl;
        }
    }

    public void softDelete() {
        this.memberStatus = MemberStatus.WITHDRAWAL;
    }
}
