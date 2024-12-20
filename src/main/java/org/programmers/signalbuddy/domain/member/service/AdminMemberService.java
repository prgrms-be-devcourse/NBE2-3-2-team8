package org.programmers.signalbuddy.domain.member.service;

import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.programmers.signalbuddy.domain.bookmark.entity.Bookmark;
import org.programmers.signalbuddy.domain.bookmark.entity.dto.AdminBookmarkResponse;
import org.programmers.signalbuddy.domain.bookmark.entity.dto.BookmarkMapper;
import org.programmers.signalbuddy.domain.bookmark.repository.BookmarkRepository;
import org.programmers.signalbuddy.domain.member.entity.Member;
import org.programmers.signalbuddy.domain.member.dto.AdminMemberResponse;
import org.programmers.signalbuddy.domain.member.repository.MemberRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class AdminMemberService {

    private final MemberRepository memberRepository;
    private final BookmarkRepository bookmarkRepository;

    public Page<AdminMemberResponse> getAllMembers(Pageable pageable) {
        Page<Member> membersPage = memberRepository.findAll(pageable);
        List<AdminMemberResponse> responses = new ArrayList<>();

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



}
