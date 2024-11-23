package org.open.ngelmakproject.web.rest;

import jakarta.validation.constraints.NotNull;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.open.ngelmakproject.domain.Attachment;
import org.open.ngelmakproject.domain.Post;
import org.open.ngelmakproject.repository.PostRepository;
import org.open.ngelmakproject.service.PostService;
import org.open.ngelmakproject.web.rest.errors.BadRequestAlertException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springdoc.api.annotations.ParameterObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link org.open.ngelmakproject.domain.Post}.
 */
@RestController
@RequestMapping("/api/posts")
public class PostResource {

    private static final Logger log = LoggerFactory.getLogger(PostResource.class);

    private static final String ENTITY_NAME = "post";

    @Value("${ngelmak.clientApp.name}")
    private String applicationName;

    private final PostService postService;

    private final PostRepository postRepository;

    public PostResource(PostService postService, PostRepository postRepository) {
        this.postService = postService;
        this.postRepository = postRepository;
    }

    /**
     * {@code POST  /posts} : Create a new post.
     *
     * @param post the post to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with
     *         body the new post, or with status {@code 400 (Bad Request)} if the
     *         post has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<Post> createPost(@RequestPart Post post, @RequestPart List<Attachment> attachments,
            @RequestPart(required = false) List<MultipartFile> files)
            throws URISyntaxException {
        log.debug("REST request to save Post : {}", post);
        if (post.getId() != null) {
            throw new BadRequestAlertException("A new post cannot already have an ID", ENTITY_NAME, "idexists");
        }
        post = postService.save(post, attachments, files);
        return ResponseEntity.created(new URI("/api/posts/" + post.getId()))
                .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME,
                        post.getId().toString()))
                .body(post);
    }

    /**
     * {@code PUT  /posts/:id} : Updates an existing post.
     *
     * @param id   the id of the post to save.
     * @param post the post to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body
     *         the updated post,
     *         or with status {@code 400 (Bad Request)} if the post is not valid,
     *         or with status {@code 500 (Internal Server Error)} if the post
     *         couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     * @throws IOException
     */
    @PutMapping("")
    public ResponseEntity<Post> updatePost(
            @RequestPart Post post,
            @RequestPart List<Attachment> attachments,
            @RequestPart(required = false) List<Attachment> deletedAttachments,
            @RequestPart(required = false) List<MultipartFile> files)
            throws URISyntaxException, IOException {
        log.debug("REST request to update Post : {}", post);
        post = postService.update(post, attachments, deletedAttachments, files);
        return ResponseEntity.ok()
                .headers(
                        HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, post.getId().toString()))
                .body(post);
    }

    /**
     * {@code PATCH  /posts/:id} : Partial updates given fields of an existing post,
     * field will ignore if it is null
     *
     * @param id   the id of the post to save.
     * @param post the post to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body
     *         the updated post,
     *         or with status {@code 400 (Bad Request)} if the post is not valid,
     *         or with status {@code 404 (Not Found)} if the post is not found,
     *         or with status {@code 500 (Internal Server Error)} if the post
     *         couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Post> partialUpdatePost(
            @PathVariable(value = "id", required = false) final Long id,
            @NotNull @RequestBody Post post) throws URISyntaxException {
        log.debug("REST request to partial update Post partially : {}, {}", id, post);
        if (post.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, post.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!postRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Post> result = postService.partialUpdate(post);

        return ResponseUtil.wrapOrNotFound(
                result,
                HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, post.getId().toString()));
    }

    /**
     * {@code GET  /posts} : get all the posts.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list
     *         of posts in body.
     */
    @GetMapping("")
    public ResponseEntity<List<Post>> getAllPosts(@ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of Posts");
        Page<Post> page = postService.findAll(pageable);
        HttpHeaders headers = PaginationUtil
                .generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /posts/:id} : get the "id" post.
     *
     * @param id the id of the post to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body
     *         the post, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Post> getPost(@PathVariable("id") Long id) {
        log.debug("REST request to get Post : {}", id);
        Optional<Post> post = postService.findOne(id);
        return ResponseUtil.wrapOrNotFound(post);
    }

    /**
     * {@code DELETE  /posts/:id} : delete the "id" post.
     *
     * @param id the id of the post to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePost(@PathVariable("id") Long id) {
        log.debug("REST request to delete Post : {}", id);
        postService.delete(id);
        return ResponseEntity.noContent()
                .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
                .build();
    }
}
