package org.open.ngelmakproject.repository;

import java.util.List;
import java.util.Optional;

import org.open.ngelmakproject.domain.Membership;
import org.open.ngelmakproject.domain.NkAccount;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Membership entity.
 */
@SuppressWarnings("unused")
@Repository
public interface MembershipRepository extends JpaRepository<Membership, Long> {
  Optional<Membership> findOneByFollowingAndFollower(NkAccount following, NkAccount follower);
  List<Membership> findByFollowing(NkAccount following);
  List<Membership> findByFollower(NkAccount follower);
}
