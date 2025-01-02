package org.programmers.signalbuddy.domain.admin.service;

import jakarta.transaction.Transactional;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.programmers.signalbuddy.domain.admin.dto.AdminJoinRequest;
import org.programmers.signalbuddy.domain.admin.dto.WithdrawalMemberResponse;
import org.programmers.signalbuddy.domain.admin.mapper.AdminMapper;
import org.programmers.signalbuddy.domain.bookmark.dto.AdminBookmarkResponse;
import org.programmers.signalbuddy.domain.bookmark.repository.BookmarkRepository;
import org.programmers.signalbuddy.domain.member.dto.MemberResponse;
import org.programmers.signalbuddy.domain.member.entity.Member;
import org.programmers.signalbuddy.domain.admin.dto.AdminMemberResponse;
import org.programmers.signalbuddy.domain.member.entity.enums.MemberRole;
import org.programmers.signalbuddy.domain.member.entity.enums.MemberStatus;
import org.programmers.signalbuddy.domain.member.exception.MemberErrorCode;
import org.programmers.signalbuddy.domain.member.mapper.MemberMapper;
import org.programmers.signalbuddy.domain.member.repository.MemberRepository;
import org.programmers.signalbuddy.global.exception.BusinessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class AdminService {

    private final MemberRepository memberRepository;
    private final BookmarkRepository bookmarkRepository;
    private BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();

    public Page<AdminMemberResponse> getAllMembers(Pageable pageable) {
        Page<Member> membersPage = memberRepository.findAll(pageable);

        Page<AdminMemberResponse> adminMemberResponses = membersPage.map(member -> {

            List<AdminBookmarkResponse> adminBookmarkResponses = bookmarkRepository.findBookmarkByMember(
                member.getMemberId());
            AdminMemberResponse response = AdminMapper.INSTANCE.toAdminMemberResponse(member,
                adminBookmarkResponses);

            return response;
        });

        return adminMemberResponses;
    }

    public AdminMemberResponse getMember(Long id) {
        Member member = memberRepository.findById(id).orElseThrow(() -> new BusinessException(
            MemberErrorCode.NOT_FOUND_MEMBER));

        List<AdminBookmarkResponse> adminBookmarkResponses = bookmarkRepository.findBookmarkByMember(
            member.getMemberId());

        AdminMemberResponse response = AdminMapper.INSTANCE.toAdminMemberResponse(member,
            adminBookmarkResponses);

        return response;
    }

    public Page<WithdrawalMemberResponse> getAllWithdrawalMembers(Pageable pageable) {
        Page<WithdrawalMemberResponse> membersPage = memberRepository.findAllWithdrawMembers(pageable);

       return membersPage;
    }

    @Transactional
    public MemberResponse joinAdminMember(AdminJoinRequest adminJoinRequest) {

        // 이미 존재하는 사용자인지 확인
        if (memberRepository.existsByEmail(adminJoinRequest.getEmail())) {
            throw new BusinessException(MemberErrorCode.ALREADY_EXIST_EMAIL);
        }

        Member joinMember = Member.builder()
            .email(adminJoinRequest.getEmail())
            .password(bCryptPasswordEncoder.encode(adminJoinRequest.getPassword()))
            .nickname("관리자")
            .memberStatus(MemberStatus.ACTIVITY)
            .role(MemberRole.ADMIN)
            .build();

        memberRepository.save(joinMember);
        return MemberMapper.INSTANCE.toDto(joinMember);
    }
}
