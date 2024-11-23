package org.open.ngelmakproject.repository;

import org.open.ngelmakproject.domain.Membership;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Membership entity.
 */
@SuppressWarnings("unused")
@Repository
public interface MembershipRepository extends JpaRepository<Membership, Long> {}
