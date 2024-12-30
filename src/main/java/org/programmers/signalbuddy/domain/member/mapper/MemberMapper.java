package org.programmers.signalbuddy.domain.member.mapper;

import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import org.programmers.signalbuddy.domain.admin.dto.AdminMemberResponse;
import org.programmers.signalbuddy.domain.member.entity.Member;
import org.programmers.signalbuddy.domain.member.dto.MemberResponse;

@Mapper
public interface MemberMapper {

    MemberMapper INSTANCE = Mappers.getMapper(MemberMapper.class);

    MemberResponse toDto(Member member);

}
