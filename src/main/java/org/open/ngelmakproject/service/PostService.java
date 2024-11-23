package org.open.ngelmakproject.service;

import java.io.IOException;
import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import org.open.ngelmakproject.domain.Attachment;
import org.open.ngelmakproject.domain.Post;
import org.open.ngelmakproject.domain.enumeration.Status;
import org.open.ngelmakproject.repository.PostRepository;
import org.open.ngelmakproject.web.rest.errors.BadRequestAlertException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

/**
 * Service Implementation for managing
 * {@link org.open.ngelmakproject.domain.Post}.
 */
@Service
@Transactional
public class PostService {

    private static final Logger log = LoggerFactory.getLogger(PostService.class);

    private static final String ENTITY_NAME = "post";

    @Autowired
    private PostRepository postRepository;
    @Autowired
    private AttachmentService attachmentService;
    @Autowired
    private NgelmakAccountService ngelmakAccountService;

    /**
     * Save a post.
     *
     * @param post the entity to save.
     * @return the persisted entity.
     */
    public Post save(Post post) {
        // [TODO] we will need to change the default status to match with the fact that
        // some users can create posts that bypass some step validations.
        log.debug("Request to save Post : {}", post);
        post.status(Status.PENDING) // default status is PENDING
                .at(ZonedDateTime.now()) // set the current time
                .account(ngelmakAccountService.findOneByCurrentUser().get()); // set the current connected user as
                                                                              // creater of the post.
        return postRepository.save(post);
    }

    /**
     * Save a post.
     *
     * @param post the entity to save.
     * @return the persisted entity.
     */
    public Post save(Post post, List<Attachment> attachments, List<MultipartFile> files) {
        log.debug("Request to save Post : {}", post);
        post = save(post);
        attachments = attachmentService.save(post, attachments, files);
        post.setAttachments(new HashSet<Attachment>(attachments));
        return post;
    }

    /**
     * Update a post.
     * This function can eventually delete some attachments through the given
     * deletedAttachments variable.
     *
     * @param post the entity to save.
     * @return the persisted entity.
     * @throws IOException
     */
    public Post update(Post post, List<Attachment> attachments, List<Attachment> deletedAttachments,
            List<MultipartFile> files) throws IOException {
        log.debug("Request to update Post : {}", post);
        if (post.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!postRepository.existsById(post.getId())) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }
        post.status(Status.PENDING).lastUpdate(ZonedDateTime.now());
        this.partialUpdate(post);
        attachments = attachmentService.save(post, attachments, files);
        // [WARN] make sure to delete attachments only when all other actions are
        // successfully completed. Since the deleted actions of attachment may have
        // actions that cannot be cancelled, like removing files.
        attachmentService.delete(post, deletedAttachments);
        post.setAttachments(new HashSet<Attachment>(attachments));
        return post;
    }

    /**
     * Partially update a post.
     *
     * @param post the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<Post> partialUpdate(Post post) {
        log.debug("Request to partially update Post : {}", post);

        return postRepository
                .findById(post.getId())
                .map(existingPost -> {
                    if (post.getTitle() != null) {
                        existingPost.setTitle(post.getTitle());
                    }
                    if (post.getSubtitle() != null) {
                        existingPost.setSubtitle(post.getSubtitle());
                    }
                    if (post.getKeywords() != null) {
                        existingPost.setKeywords(post.getKeywords());
                    }
                    if (post.getSubject() != null) {
                        existingPost.setSubject(post.getSubject());
                    }
                    if (post.getAt() != null) {
                        existingPost.setAt(post.getAt());
                    }
                    if (post.getLastUpdate() != null) {
                        existingPost.setLastUpdate(post.getLastUpdate());
                    }
                    if (post.getVisibility() != null) {
                        existingPost.setVisibility(post.getVisibility());
                    }
                    if (post.getContent() != null) {
                        existingPost.setContent(post.getContent());
                    }
                    if (post.getStatus() != null) {
                        existingPost.setStatus(post.getStatus());
                    }

                    return existingPost;
                })
                .map(postRepository::save);
    }

    /**
     * Get all the posts.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<Post> findAll(Pageable pageable) {
        log.debug("Request to get all Posts");
        return postRepository.findAll(pageable);
    }

    /**
     * Get one post by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<Post> findOne(Long id) {
        log.debug("Request to get Post : {}", id);
        return postRepository.findById(id);
    }

    /**
     * Delete the post by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Post : {}", id);
        postRepository.deleteById(id);
    }
}
