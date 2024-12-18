package org.programmers.signalbuddy.domain.like.service;

import lombok.RequiredArgsConstructor;
import org.programmers.signalbuddy.domain.like.repository.LikeRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LikeService {

    private final LikeRepository likeRepository;
}
