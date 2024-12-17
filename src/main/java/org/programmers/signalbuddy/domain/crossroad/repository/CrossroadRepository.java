package org.programmers.signalbuddy.domain.crossroad.repository;

import org.programmers.signalbuddy.domain.crossroad.Crossroad;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CrossroadRepository extends JpaRepository<Crossroad, Long> {

}
