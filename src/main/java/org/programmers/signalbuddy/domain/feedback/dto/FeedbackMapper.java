package org.programmers.signalbuddy.domain.feedback.dto;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import org.programmers.signalbuddy.domain.feedback.entity.Feedback;

@Mapper
public interface FeedbackMapper {

    FeedbackMapper INSTANCE = Mappers.getMapper(FeedbackMapper.class);

    FeedbackResponse toResponse(Feedback feedback);
}
