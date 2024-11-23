package org.open.ngelmakproject.service;

import java.io.IOException;
import java.nio.file.Path;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;

import org.open.ngelmakproject.domain.Attachment;
import org.open.ngelmakproject.domain.Post;
import org.open.ngelmakproject.domain.enumeration.AttachmentCategory;
import org.open.ngelmakproject.domain.enumeration.Status;
import org.open.ngelmakproject.repository.AttachmentRepository;
import org.open.ngelmakproject.service.storage.FileStorageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

/**
 * Service Implementation for managing
 * {@link org.open.ngelmakproject.domain.Attachment}.
 */
@Service
@Transactional
public class AttachmentService {

    private static final Logger log = LoggerFactory.getLogger(AttachmentService.class);

    @Autowired
    private AttachmentRepository attachmentRepository;
    @Autowired
    private FileStorageService fileStorageService;

    /**
     * Save a attachment.
     *
     * @param attachment the entity to save.
     * @return the persisted entity.
     */
    public Attachment save(Attachment attachment) {
        log.debug("Request to save Attachment : {}", attachment);
        return attachmentRepository.save(attachment);
    }

    /**
     * Save a attachment.
     *
     * @param attachment the entity to save.
     * @return the persisted entity.
     */
    public List<Attachment> save(Post post, List<Attachment> attachments, List<MultipartFile> files) {
        log.debug("Request to save Attachment : {}", attachments);
        MultipartFile file;
        Path path = null;
        int file_index = 0;
        String[] dirs = post.getDirectories(); // path where to save the attachment file.
        for (Attachment attachment : attachments) {
            attachment.setPost(post);
            if (!attachment.getCategory().equals(AttachmentCategory.TEXT)) {
                file = files.get(file_index++);
                path = fileStorageService.store(file, attachment.getFilename(), dirs);
                attachment.size(file.getSize())
                        .url(path.toString());
            }
        }
        return attachmentRepository.saveAll(attachments);
    }

    /**
     * Update a attachment.
     *
     * @param attachment the entity to save.
     * @return the persisted entity.
     */
    public Attachment update(Attachment attachment) {
        log.debug("Request to update Attachment : {}", attachment);
        return attachmentRepository.save(attachment);
    }

    /**
     * Partially update a attachment.
     *
     * @param attachment the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<Attachment> partialUpdate(Attachment attachment) {
        log.debug("Request to partially update Attachment : {}", attachment);

        return attachmentRepository
                .findById(attachment.getId())
                .map(existingAttachment -> {
                    if (attachment.getType() != null) {
                        existingAttachment.setType(attachment.getType());
                    }
                    if (attachment.getContent() != null) {
                        existingAttachment.setContent(attachment.getContent());
                    }
                    if (attachment.getType() != null) {
                        existingAttachment.setType(attachment.getType());
                    }

                    return existingAttachment;
                })
                .map(attachmentRepository::save);
    }

    /**
     * Get all the attachments.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<Attachment> findAll(Pageable pageable) {
        log.debug("Request to get all Attachments");
        return attachmentRepository.findAll(pageable);
    }

    /**
     * Get one attachment by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<Attachment> findOne(Long id) {
        log.debug("Request to get Attachment : {}", id);
        return attachmentRepository.findById(id);
    }

    /**
     * Get attachment's resource by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     * @throws IOException
     */
    @Transactional(readOnly = true)
    public Optional<byte[]> getResource(Long id) throws IOException {
        log.debug("Request to get the actual resource of Attachment : {}", id);
        Optional<Attachment> optional = attachmentRepository.findById(id);
        if (optional.isEmpty()) {
            return Optional.empty();
        }
        Attachment attachment = optional.get();
        Resource resource = fileStorageService.loadAsResource(attachment.getUrl());
        return Optional.of(resource.getContentAsByteArray());
    }

    /**
     * Delete given attachments and there files if exist.
     * The deleting process in the first place marks items as deleted by putting the
     * datetime on which they have been delete. Later, a crontab goes through all
     * that have expired to permenently delete them from the system and the
     * database.
     * This helps for a rollback.
     *
     * @throws IOException
     */
    public void delete(Post post, List<Attachment> attachments) throws IOException {
        log.debug("Request to delete Attachment : {}", attachments);
        ZonedDateTime now = ZonedDateTime.now();
        if (!post.getStatus().equals(Status.PENDING)) {
            attachments.forEach(e -> e.deletedAt(now)); // mark all attachments as deleted.
            attachmentRepository.saveAll(attachments);
        } else {
            // [WARN] This action cannot be cancelled.
            this.deletePermenently(attachments);
        }
    }

    public void deletePermenently(List<Attachment> attachments) throws IOException {
        log.debug("Request to delete Attachment : {}", attachments);
        for (Attachment attachment : attachments) {
            if (!attachment.getCategory().equals(AttachmentCategory.TEXT)) {
                fileStorageService.deleteFile(attachment.getUrl());
            }
        }
        attachmentRepository.deleteAll(attachments);
    }

    /**
     * Delete the attachment by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Attachment : {}", id);
        attachmentRepository.deleteById(id);
    }

}
