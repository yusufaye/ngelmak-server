package org.open.ngelmakproject.service;

import java.io.IOException;
import java.net.URL;
import java.time.Instant;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
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
import org.springframework.util.StringUtils;
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
    public List<Attachment> save(Post post, List<Attachment> attachments, List<MultipartFile> files,
            List<MultipartFile> posters) {
        log.debug("Request to save Attachment : {}", attachments);
        Attachment attachment;
        MultipartFile file;
        MultipartFile poster;
        URL url = null;
        URL posterUrl = null;
        String filename = null;
        String[] dirs = { "media", "attachments" }; // path where to save the attachment file.
        LocalDate date = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        String format = date.format(formatter);
        for (int i = 0; i < attachments.size(); i++) {
            attachment = attachments.get(i);
            file = files.get(i);
            poster = posters.get(i);
            if (file != null) {
                filename = String.format("Ngelmak-%s-%s-%s",
                        StringUtils.capitalize(attachment.getCategory().toString()), format,
                        StringUtils.capitalize(file.getOriginalFilename()));
                url = fileStorageService.store(file, true, filename, dirs);
                attachment.size(file.getSize())
                        .url(url.toString());
            }
            if (poster != null) {
                filename = String.format("Ngelmak-Poster-%s-%s-%s",
                        StringUtils.capitalize(AttachmentCategory.IMAGE.toString()), format,
                        StringUtils.capitalize(file.getOriginalFilename().replaceFirst(".[a-zA-Z0-9]+$", ".png")));
                posterUrl = fileStorageService.store(poster, true, filename, dirs);
                attachment.setUrl(posterUrl.toString());
            }
            attachment.setPost(post);
        }
        return attachmentRepository.saveAll(attachments);
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
        Instant now = Instant.now();
        if (!post.getStatus().equals(Status.PENDING)) {
            // Mark the attachment as to be deleted by the cron.
            attachments = attachmentRepository.findAllById(attachments.stream().map(Attachment::getId).toList())
                    .stream().map(existingAttachement -> existingAttachement.deletedAt(now)).toList();
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
                fileStorageService.delete(attachment.getUrl());
                fileStorageService.delete(attachment.getPosterUrl());
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
