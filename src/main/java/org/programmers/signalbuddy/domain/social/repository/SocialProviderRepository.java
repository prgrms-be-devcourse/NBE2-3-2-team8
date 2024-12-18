package org.programmers.signalbuddy.domain.social.repository;

import org.programmers.signalbuddy.domain.social.entity.SocialProvider;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SocialProviderRepository extends JpaRepository<SocialProvider, Long> {

}
