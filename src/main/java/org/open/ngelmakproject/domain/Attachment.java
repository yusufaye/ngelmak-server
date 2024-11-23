package org.open.ngelmakproject.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.ZonedDateTime;

import org.open.ngelmakproject.domain.enumeration.AttachmentCategory;

/**
 * A Attachment.
 */
@Entity
@Table(name = "attachment")
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

    @Column(name = "size")
    private Long size;

    @Column(name = "duration")
    private Integer duration;

    @Column(name = "url")
    private String url;

    @Column(name = "content")
    private String content;

    @NotNull
    @Column(name = "type", nullable = false)
    private String type;

    @Column(name = "deleted_at")
    private ZonedDateTime deletedAt;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "attachments", "reports", "comments", "account" }, allowSetters = true)
    private Post post;

    // jhipster-needle-entity-add-field - JHipster will add fields here

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

    public String getContent() {
        return this.content;
    }

    public Attachment content(String content) {
        this.setContent(content);
        return this;
    }

    public void setContent(String content) {
        this.content = content;
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

    public ZonedDateTime getDeletedAt() {
        return this.deletedAt;
    }

    public Attachment deletedAt(ZonedDateTime deletedAt) {
        this.deletedAt = deletedAt;
        return this;
    }

    public void setDeletedAt(ZonedDateTime deletedAt) {
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

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

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
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Attachment{" +
            "id=" + getId() +
            ", type='" + getType() + "'" +
            ", content='" + getContent() + "'" +
            ", type='" + getType() + "'" +
            "}";
    }
}
