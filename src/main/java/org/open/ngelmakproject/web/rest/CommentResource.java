package org.open.ngelmakproject.web.rest;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

import org.open.ngelmakproject.domain.Comment;
import org.open.ngelmakproject.domain.Post;
import org.open.ngelmakproject.repository.CommentRepository;
import org.open.ngelmakproject.service.CommentService;
import org.open.ngelmakproject.web.rest.errors.BadRequestAlertException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springdoc.api.annotations.ParameterObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link org.open.ngelmakproject.domain.Comment}.
 */
@RestController
@RequestMapping("/api/comments")
public class CommentResource {

    private static final Logger log = LoggerFactory.getLogger(CommentResource.class);

    private static final String ENTITY_NAME = "comment";

    @Value("${ngelmak.clientApp.name}")
    private String applicationName;

    private final CommentService commentService;

    private final CommentRepository commentRepository;

    public CommentResource(CommentService commentService, CommentRepository commentRepository) {
        this.commentService = commentService;
        this.commentRepository = commentRepository;
    }

    /**
     * {@code POST  /comments} : Create a new comment.
     *
     * @param comment the comment to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with
     *         body the new comment, or with status {@code 400 (Bad Request)} if the
     *         comment has already an ID.
     * @throws URISyntaxException    if the Location URI syntax is incorrect.
     * @throws MalformedURLException
     */
    @PostMapping("")
    public ResponseEntity<Comment> createComment(@RequestPart Comment comment,
            @RequestPart(required = false) MultipartFile file)
            throws URISyntaxException, MalformedURLException {
        log.debug("REST request to save Comment : {}", comment);
        if (comment.getId() != null) {
            throw new BadRequestAlertException("A new comment cannot already have an ID", ENTITY_NAME, "idexists");
        }
        comment = commentService.save(comment, file);
        return ResponseEntity.created(new URI("/api/comments/" + comment.getId()))
                .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME,
                        comment.getId().toString()))
                .body(comment);
    }

    /**
     * {@code PUT  /comments} : Updates an existing comment.
     *
     * @param comment the comment to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body
     *         the updated comment,
     *         or with status {@code 400 (Bad Request)} if the comment is not valid,
     *         or with status {@code 500 (Internal Server Error)} if the comment
     *         couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("")
    public ResponseEntity<Comment> updateComment(
        @RequestPart Comment comment,
        @RequestPart(required = false) MultipartFile file) throws URISyntaxException {
        log.debug("REST request to update Comment : {}", comment);
        if (comment.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }

        comment = commentService.update(comment, file);
        return ResponseEntity.ok()
                .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME,
                        comment.getId().toString()))
                .body(comment);
    }

    /**
     * {@code GET  /comments} : get all the comments.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list
     *         of comments in body.
     */
    @GetMapping("")
    public ResponseEntity<List<Comment>> getAllComments(@ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of Comments");
        Page<Comment> page = commentService.findAll(pageable);
        HttpHeaders headers = PaginationUtil
                .generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /comments/post/:id} : get all the comments for a given post id.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list
     *         of comments in body.
     */
    @GetMapping("/post/{id}")
    public ResponseEntity<List<Comment>> getAllComments(@PathVariable Long id) {
        log.debug("REST request to get Comments of Post : {}", id);
        List<Comment> comments = commentRepository.findByPost(new Post().id(id));
        return ResponseEntity.ok().body(comments);
    }

    /**
     * {@code GET  /comments/:id} : get the "id" comment.
     *
     * @param id the id of the comment to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body
     *         the comment, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Comment> getComment(@PathVariable("id") Long id) {
        log.debug("REST request to get Comment : {}", id);
        Optional<Comment> comment = commentService.findOne(id);
        return ResponseUtil.wrapOrNotFound(comment);
    }

    /**
     * {@code DELETE  /comments/:id} : delete the "id" comment.
     *
     * @param id the id of the comment to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteComment(@PathVariable("id") Long id) {
        log.debug("REST request to delete Comment : {}", id);
        commentService.delete(id);
        return ResponseEntity.noContent()
                .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
                .build();
    }
}
