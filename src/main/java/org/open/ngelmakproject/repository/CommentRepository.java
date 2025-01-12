package org.open.ngelmakproject.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.open.ngelmakproject.domain.Comment;
import org.open.ngelmakproject.domain.Post;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Comment entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
  List<Comment> findByPost(Post post);

  // @Query("""
  //   select new Comment(
  //     c.id,
  //     c.opinion,
  //     c.at,
  //     c.lastUpdate,
  //     c.deletedAt,
  //     c.content,
  //     c.url,
  //     c.post,
  //     c.replayto,
  //     c.account
  //   )
  //   from Comment c
  //   where c.id = :id
  //   """)
  // Optional<Comment> findById(@Param("id") Long id);

  // @Modifying
  // @Query("update Comment c set c.content = :content and c.url = :url where u.id < :id")
  // void update(@Param("id") Long id, @Param("content") String content, @Param("url") String url);
}
