package org.open.ngelmakproject.web.rest;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import org.open.ngelmakproject.domain.Attachment;
import org.open.ngelmakproject.domain.Post;
import org.open.ngelmakproject.repository.AttachmentRepository;
import org.open.ngelmakproject.service.AttachmentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link org.open.ngelmakproject.domain.Attachment}.
 */
@RestController
@RequestMapping("/api/attachments")
public class AttachmentResource {

    private static final Logger log = LoggerFactory.getLogger(AttachmentResource.class);

    @Value("${ngelmak.clientApp.name}")
    private String applicationName;

    private final AttachmentService attachmentService;

    private final AttachmentRepository attachmentRepository;

    public AttachmentResource(AttachmentService attachmentService, AttachmentRepository attachmentRepository) {
        this.attachmentService = attachmentService;
        this.attachmentRepository = attachmentRepository;
    }

    /**
     * {@code GET  /attachments/post/:id} : get all the attachments.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of attachments in body.
     */
    @GetMapping("/post/{id}")
    public ResponseEntity<List<Attachment>> findByPost(@PathVariable("id") Long id) {
        log.debug("REST request to get Attachments by Post {}", id);
        return ResponseEntity.ok().body(attachmentRepository.findByPost(new Post().id(id)));
    }

    /**
     * {@code GET  /attachments/:id} : get the "id" attachment.
     *
     * @param id the id of the attachment to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the attachment, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Attachment> getAttachment(@PathVariable("id") Long id) {
        log.debug("REST request to get Attachment : {}", id);
        Optional<Attachment> attachment = attachmentService.findOne(id);
        return ResponseUtil.wrapOrNotFound(attachment);
    }

    /**
     * {@code GET  /attachments/:id/resource} : get the "id" attachment.
     *
     * @param id the id of the attachment to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the attachment, or with status {@code 404 (Not Found)}.
     * @throws IOException 
     */
    @GetMapping("/{id}/resource")
    public ResponseEntity<byte[]> getAttachmentResource(@PathVariable("id") Long id) throws IOException {
        log.debug("REST request to get Attachment : {}", id);
        return ResponseUtil.wrapOrNotFound(attachmentService.getResource(id));
    }
}
