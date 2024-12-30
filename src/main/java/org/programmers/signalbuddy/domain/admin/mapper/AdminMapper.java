package org.programmers.signalbuddy.domain.admin.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import org.programmers.signalbuddy.domain.admin.dto.AdminMemberResponse;
import org.programmers.signalbuddy.domain.member.entity.Member;

@Mapper
public interface AdminMapper {
    AdminMapper INSTANCE = Mappers.getMapper(AdminMapper.class);
    AdminMemberResponse toAdminDto(Member member);
}
