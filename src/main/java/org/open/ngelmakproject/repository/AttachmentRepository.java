package org.open.ngelmakproject.repository;

import java.util.List;

import org.open.ngelmakproject.domain.Attachment;
import org.open.ngelmakproject.domain.Post;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Attachment entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AttachmentRepository extends JpaRepository<Attachment, Long> {
  List<Attachment> findByPost(Post post);
}
