package org.open.ngelmakproject.domain;

import java.io.Serializable;
import java.time.Instant;

import org.open.ngelmakproject.domain.enumeration.AttachmentCategory;

import com.fasterxml.jackson.annotation.JsonIncludeProperties;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;

/**
 * A Attachment.
 */
@Entity
@Table(name = "nk_attachment")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Attachment implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "category", nullable = false)
    private AttachmentCategory category;

    @NotNull
    @Column(name = "position", nullable = false)
    private Integer position;

    @Column(name = "filename")
    private String filename;

    @Column(name = "caption")
    private String caption;

    @Column(name = "size")
    private Long size;

    @Column(name = "duration")
    private Integer duration;

    @Column(name = "url")
    private String url;

    @Column(name = "poster_url")
    private String posterUrl;

    @Column(name = "text_content", length = 2000, nullable = true)
    private String textContent;

    @NotNull
    @Column(name = "type", nullable = false)
    private String type;

    @Column(name = "deleted_at")
    private Instant deletedAt;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIncludeProperties(value = { "id" })
    private Post post;

    public Long getId() {
        return this.id;
    }

    public Attachment id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public AttachmentCategory getCategory() {
        return this.category;
    }

    public Attachment category(AttachmentCategory category) {
        this.setCategory(category);
        return this;
    }

    public void setCategory(AttachmentCategory category) {
        this.category = category;
    }

    public Integer getPosition() {
        return this.position;
    }

    public Attachment position(Integer position) {
        this.position = position;
        return this;
    }

    public void setPosition(Integer position) {
        this.position = position;
    }

    public String getCaption() {
        return this.caption;
    }

    public Attachment caption(String caption) {
        this.caption = caption;
        return this;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    public String getFilename() {
        return this.filename;
    }

    public Attachment filename(String filename) {
        this.filename = filename;
        return this;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getTextContent() {
        return this.textContent;
    }

    public Attachment textContent(String textContent) {
        this.setTextContent(textContent);
        return this;
    }

    public void setTextContent(String textContent) {
        this.textContent = textContent;
    }

    public String getType() {
        return this.type;
    }

    public Attachment type(String type) {
        this.type = type;
        return this;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Long getSize() {
        return this.size;
    }

    public Attachment size(Long size) {
        this.size = size;
        return this;
    }

    public void setSize(Long size) {
        this.size = size;
    }

    public Integer getDuration() {
        return this.duration;
    }

    public Attachment duration(Integer duration) {
        this.duration = duration;
        return this;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    public String getUrl() {
        return this.url;
    }

    public Attachment url(String url) {
        this.url = url;
        return this;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getPosterUrl() {
        return this.posterUrl;
    }

    public Attachment posterUrl(String posterUrl) {
        this.posterUrl = posterUrl;
        return this;
    }

    public void setPosterUrl(String posterUrl) {
        this.posterUrl = posterUrl;
    }

    public Instant getDeletedAt() {
        return this.deletedAt;
    }

    public Attachment deletedAt(Instant deletedAt) {
        this.deletedAt = deletedAt;
        return this;
    }

    public void setDeletedAt(Instant deletedAt) {
        this.deletedAt = deletedAt;
    }

    public Post getPost() {
        return this.post;
    }

    public void setPost(Post post) {
        this.post = post;
    }

    public Attachment post(Post post) {
        this.setPost(post);
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Attachment)) {
            return false;
        }
        return getId() != null && getId().equals(((Attachment) o).getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Attachment{" +
                "id=" + getId() +
                ", type='" + getType() + "'" +
                ", caption='" + getCaption() + "'" +
                ", content='" + getTextContent() + "'" +
                ", type='" + getType() + "'" +
                "}";
    }
}
