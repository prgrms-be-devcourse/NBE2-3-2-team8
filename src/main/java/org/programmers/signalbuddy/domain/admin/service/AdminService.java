package org.programmers.signalbuddy.domain.admin.service;

import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.programmers.signalbuddy.domain.admin.mapper.AdminMapper;
import org.programmers.signalbuddy.domain.bookmark.entity.Bookmark;
import org.programmers.signalbuddy.domain.bookmark.dto.AdminBookmarkResponse;
import org.programmers.signalbuddy.domain.bookmark.mapper.BookmarkMapper;
import org.programmers.signalbuddy.domain.bookmark.repository.BookmarkRepository;
import org.programmers.signalbuddy.domain.bookmark.repository.BookmarkRepositoryCustom;
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
}
