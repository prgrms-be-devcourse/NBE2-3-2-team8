package org.programmers.signalbuddy.domain.member.service;

import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import org.programmers.signalbuddy.domain.member.entity.Member;
import org.programmers.signalbuddy.domain.member.entity.dto.AdminMemberResponse;
import org.programmers.signalbuddy.domain.member.entity.dto.MemberResponse;

@Mapper
public interface MemberMapper {

    MemberMapper INSTANCE = Mappers.getMapper(MemberMapper.class);

    MemberResponse toDto(Member member);
    AdminMemberResponse toAdminDto(Member member);
    List<AdminMemberResponse> toAdminMembersDto(List<Member> member);
}
