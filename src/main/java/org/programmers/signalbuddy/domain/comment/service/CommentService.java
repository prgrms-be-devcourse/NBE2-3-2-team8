package org.programmers.signalbuddy.domain.comment.service;

import lombok.RequiredArgsConstructor;
import org.programmers.signalbuddy.domain.comment.repository.CommentRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
}
