package org.programmers.signalbuddy.domain.social.service;

import lombok.RequiredArgsConstructor;
import org.programmers.signalbuddy.domain.social.repository.SocialProviderRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SocialProviderService {

    private final SocialProviderRepository socialProviderRepository;
}
