package org.open.ngelmakproject.service;

import java.util.Optional;
import org.open.ngelmakproject.domain.Comment;
import org.open.ngelmakproject.repository.CommentRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link org.open.ngelmakproject.domain.Comment}.
 */
@Service
@Transactional
public class CommentService {

    private static final Logger log = LoggerFactory.getLogger(CommentService.class);

    private final CommentRepository commentRepository;

    public CommentService(CommentRepository commentRepository) {
        this.commentRepository = commentRepository;
    }

    /**
     * Save a comment.
     *
     * @param comment the entity to save.
     * @return the persisted entity.
     */
    public Comment save(Comment comment) {
        log.debug("Request to save Comment : {}", comment);
        return commentRepository.save(comment);
    }

    /**
     * Update a comment.
     *
     * @param comment the entity to save.
     * @return the persisted entity.
     */
    public Comment update(Comment comment) {
        log.debug("Request to update Comment : {}", comment);
        return commentRepository.save(comment);
    }

    /**
     * Partially update a comment.
     *
     * @param comment the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<Comment> partialUpdate(Comment comment) {
        log.debug("Request to partially update Comment : {}", comment);

        return commentRepository
            .findById(comment.getId())
            .map(existingComment -> {
                if (comment.getOpinion() != null) {
                    existingComment.setOpinion(comment.getOpinion());
                }
                if (comment.getAt() != null) {
                    existingComment.setAt(comment.getAt());
                }
                if (comment.getLastUpdate() != null) {
                    existingComment.setLastUpdate(comment.getLastUpdate());
                }
                if (comment.getContent() != null) {
                    existingComment.setContent(comment.getContent());
                }

                return existingComment;
            })
            .map(commentRepository::save);
    }

    /**
     * Get all the comments.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<Comment> findAll(Pageable pageable) {
        log.debug("Request to get all Comments");
        return commentRepository.findAll(pageable);
    }

    /**
     * Get one comment by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<Comment> findOne(Long id) {
        log.debug("Request to get Comment : {}", id);
        return commentRepository.findById(id);
    }

    /**
     * Delete the comment by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Comment : {}", id);
        commentRepository.deleteById(id);
    }
}
