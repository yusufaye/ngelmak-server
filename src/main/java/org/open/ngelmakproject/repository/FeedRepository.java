package org.open.ngelmakproject.repository;

import java.util.List;

import org.open.ngelmakproject.domain.Feed;
import org.open.ngelmakproject.domain.NkAccount;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Feed entity.
 */
@SuppressWarnings("unused")
@Repository
public interface FeedRepository extends JpaRepository<Feed, String> {
  Page<Feed> findByFeedOwner(NkAccount feedOwner, Pageable pageable);
  Page<Feed> findByFeedOwnerIn(List<NkAccount> feedOwners, Pageable pageable);
  @Query("SELECT f FROM Feed f ORDER BY f.post.at")
  Page<Feed> findByOrderByDateDesc(Pageable pageable);
}
