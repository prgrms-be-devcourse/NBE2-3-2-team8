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
import org.programmers.signalbuddy.domain.member.MemberRole;
import org.programmers.signalbuddy.domain.member.dto.MemberUpdateRequest;
import org.programmers.signalbuddy.domain.member.entity.enums.MemberStatus;

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

    public void updateMember(MemberUpdateRequest request) {
        if (request.getEmail() != null) {
            this.email = request.getEmail();
        }
        if (request.getPassword() != null) {
            this.password = request.getPassword(); // TODO: 암호화 적용
        }
        if (request.getNickname() != null) {
            this.nickname = request.getNickname();
        }
        if (request.getProfileImageUrl() != null) {
            this.profileImageUrl = request.getProfileImageUrl();
        }
    }

    public void softDelete() {
        this.memberStatus = MemberStatus.WITHDRAWAL;
    }
}
