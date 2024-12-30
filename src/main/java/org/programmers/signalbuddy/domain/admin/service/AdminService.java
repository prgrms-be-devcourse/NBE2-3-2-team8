package org.programmers.signalbuddy.domain.admin.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.programmers.signalbuddy.domain.bookmark.entity.Bookmark;
import org.programmers.signalbuddy.domain.bookmark.dto.AdminBookmarkResponse;
import org.programmers.signalbuddy.domain.bookmark.mapper.BookmarkMapper;
import org.programmers.signalbuddy.domain.bookmark.repository.BookmarkRepository;
import org.programmers.signalbuddy.domain.member.entity.Member;
import org.programmers.signalbuddy.domain.admin.dto.AdminMemberResponse;
import org.programmers.signalbuddy.domain.member.exception.MemberErrorCode;
import org.programmers.signalbuddy.domain.member.repository.MemberRepository;
import org.programmers.signalbuddy.global.exception.BusinessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class AdminService {

    private final MemberRepository memberRepository;
    private final BookmarkRepository bookmarkRepository;

    public Page<AdminMemberResponse> getAllMembers(Pageable pageable) {
        Page<Member> membersPage = memberRepository.findAll(pageable);

        Page<AdminMemberResponse> adminMemberResponses = membersPage.map(member -> {
            List<Bookmark> bookmarks = bookmarkRepository.findAllByMember_MemberId(member.getMemberId());
            List<AdminBookmarkResponse> adminBookmarkResponses = BookmarkMapper.INSTANCE.toAdminDto(bookmarks);

            return AdminMemberResponse.builder()
                .memberId(member.getMemberId())
                .email(member.getEmail())
                .nickname(member.getNickname())
                .profileImageUrl(member.getProfileImageUrl())
                .role(member.getRole())
                .memberStatus(member.getMemberStatus())
                .bookmarkResponses(adminBookmarkResponses)
                .build();
        });

        return adminMemberResponses;
    }

    public AdminMemberResponse getMember(Long id) {
        Member member = memberRepository.findById(id).orElseThrow(()-> new BusinessException(
            MemberErrorCode.NOT_FOUND_MEMBER));
        List<Bookmark> bookmarks = bookmarkRepository.findAllByMember_MemberId(member.getMemberId());
        List<AdminBookmarkResponse> adminBookmarkResponses = BookmarkMapper.INSTANCE.toAdminDto(bookmarks);

        AdminMemberResponse response = AdminMemberResponse.builder()
            .memberId(member.getMemberId())
            .email(member.getEmail())
            .nickname(member.getNickname())
            .profileImageUrl(member.getProfileImageUrl())
            .role(member.getRole())
            .memberStatus(member.getMemberStatus())
            .bookmarkResponses(adminBookmarkResponses)
            .build();

        return response;
    }



}
