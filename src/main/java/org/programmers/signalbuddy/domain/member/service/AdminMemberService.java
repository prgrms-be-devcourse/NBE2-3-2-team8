package org.programmers.signalbuddy.domain.member.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.programmers.signalbuddy.domain.bookmark.entity.Bookmark;
import org.programmers.signalbuddy.domain.bookmark.entity.dto.AdminBookmarkResponse;
import org.programmers.signalbuddy.domain.bookmark.entity.dto.BookmarkMapper;
import org.programmers.signalbuddy.domain.bookmark.entity.dto.BookmarkResponse;
import org.programmers.signalbuddy.domain.bookmark.repository.BookmarkRepository;
import org.programmers.signalbuddy.domain.member.entity.Member;
import org.programmers.signalbuddy.domain.member.entity.dto.AdminMemberResponse;
import org.programmers.signalbuddy.domain.member.repository.MemberRepository;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

@Service
@RequiredArgsConstructor
@Slf4j
public class AdminMemberService {

    private final MemberRepository memberRepository;
    private final BookmarkRepository bookmarkRepository;

    public List<AdminMemberResponse> getAllMembers() {
        List<Member> lists = memberRepository.findAll();
        List<AdminMemberResponse> responses = new ArrayList<>();

        for (Member member : lists) {
            List<Bookmark> bookmarks = bookmarkRepository.findAllByMember_MemberId(member.getMemberId());
            List<AdminBookmarkResponse> adminBookmarkResponses = BookmarkMapper.INSTANCE.toAdminDto(bookmarks);


            AdminMemberResponse adminMemberResponse = AdminMemberResponse.builder()
                .memberId(member.getMemberId())
                .email(member.getEmail())
                .nickname(member.getNickname())
                .profileImageUrl(member.getProfileImageUrl())
                .role(member.getRole())
                .memberStatus(member.getMemberStatus())
                .bookmarkResponses(adminBookmarkResponses)
                .build();

            responses.add(adminMemberResponse);
        }
        return responses;
    }

}
