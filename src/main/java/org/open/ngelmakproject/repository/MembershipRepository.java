package org.open.ngelmakproject.repository;

import java.util.List;
import java.util.Optional;

import org.open.ngelmakproject.domain.NkMembership;
import org.open.ngelmakproject.domain.NkAccount;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the NkMembership entity.
 */
@SuppressWarnings("unused")
@Repository
public interface MembershipRepository extends JpaRepository<NkMembership, Long> {
  Optional<NkMembership> findOneByFollowingAndFollower(NkAccount following, NkAccount follower);
  List<NkMembership> findByFollowing(NkAccount following);
  List<NkMembership> findByFollower(NkAccount follower);
}
