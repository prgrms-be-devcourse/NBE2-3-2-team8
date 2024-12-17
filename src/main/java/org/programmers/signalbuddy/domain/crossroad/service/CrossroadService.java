package org.programmers.signalbuddy.domain.crossroad.service;

import lombok.RequiredArgsConstructor;
import org.programmers.signalbuddy.domain.crossroad.repository.CrossroadRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CrossroadService {

    private final CrossroadRepository crossroadRepository;

}
