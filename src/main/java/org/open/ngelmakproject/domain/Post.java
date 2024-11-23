package org.open.ngelmakproject.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.Set;

import org.open.ngelmakproject.config.Constants;
import org.open.ngelmakproject.domain.enumeration.Status;
import org.open.ngelmakproject.domain.enumeration.Subject;
import org.open.ngelmakproject.domain.enumeration.Visibility;

/**
 * The Post entity.
 * @author A true hipster
 */
@Entity
@Table(name = "post")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Post implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "subtitle")
    private String subtitle;

    @Column(name = "keywords")
    private String keywords;

    @Enumerated(EnumType.STRING)
    @Column(name = "subject")
    private Subject subject;

    @NotNull
    @Column(name = "at", nullable = false)
    private ZonedDateTime at;

    @Column(name = "last_update")
    private ZonedDateTime lastUpdate;

    @Enumerated(EnumType.STRING)
    @Column(name = "visibility")
    private Visibility visibility;

    @Column(name = "content", nullable = false)
    private String content;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private Status status;

    /**
     * a post can be commented multiple times.
     */
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "post")
    @JsonIgnoreProperties(value = { "post" }, allowSetters = true)
    private Set<Attachment> attachments = new HashSet<>();

    /**
     * a post can be signal as going against our policies.
     */
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "postRelated")
    @JsonIgnoreProperties(value = { "reviews", "postRelated", "commentRelated", "accountRelated", "issuedby" }, allowSetters = true)
    private Set<Ticket> reports = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "post")
    @JsonIgnoreProperties(value = { "reports", "comments", "post", "replayto", "account" }, allowSetters = true)
    private Set<Comment> comments = new HashSet<>();

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(
        value = { "configuration", "user", "reports", "owners", "comments", "memberships", "subscriptions", "posts", "reviews" },
        allowSetters = true
    )
    private NgelmakAccount account;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Post id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return this.title;
    }

    public Post title(String title) {
        this.setTitle(title);
        return this;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSubtitle() {
        return this.subtitle;
    }

    public Post subtitle(String subtitle) {
        this.setSubtitle(subtitle);
        return this;
    }

    public void setSubtitle(String subtitle) {
        this.subtitle = subtitle;
    }

    public String getKeywords() {
        return this.keywords;
    }

    public Post keywords(String keywords) {
        this.setKeywords(keywords);
        return this;
    }

    public void setKeywords(String keywords) {
        this.keywords = keywords;
    }

    public Subject getSubject() {
        return this.subject;
    }

    public Post subject(Subject subject) {
        this.setSubject(subject);
        return this;
    }

    public void setSubject(Subject subject) {
        this.subject = subject;
    }

    public ZonedDateTime getAt() {
        return this.at;
    }

    public Post at(ZonedDateTime at) {
        this.setAt(at);
        return this;
    }

    public void setAt(ZonedDateTime at) {
        this.at = at;
    }

    public ZonedDateTime getLastUpdate() {
        return this.lastUpdate;
    }

    public Post lastUpdate(ZonedDateTime lastUpdate) {
        this.setLastUpdate(lastUpdate);
        return this;
    }

    public void setLastUpdate(ZonedDateTime lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    public Visibility getVisibility() {
        return this.visibility;
    }

    public Post visibility(Visibility visibility) {
        this.setVisibility(visibility);
        return this;
    }

    public void setVisibility(Visibility visibility) {
        this.visibility = visibility;
    }

    public String getContent() {
        return this.content;
    }

    public Post content(String content) {
        this.setContent(content);
        return this;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Status getStatus() {
        return this.status;
    }

    public Post status(Status status) {
        this.setStatus(status);
        return this;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Set<Attachment> getAttachments() {
        return this.attachments;
    }

    public void setAttachments(Set<Attachment> attachments) {
        if (this.attachments != null) {
            this.attachments.forEach(i -> i.setPost(null));
        }
        if (attachments != null) {
            attachments.forEach(i -> i.setPost(this));
        }
        this.attachments = attachments;
    }

    public Post attachments(Set<Attachment> attachments) {
        this.setAttachments(attachments);
        return this;
    }

    public Post addAttachment(Attachment attachment) {
        this.attachments.add(attachment);
        attachment.setPost(this);
        return this;
    }

    public Post removeAttachment(Attachment attachment) {
        this.attachments.remove(attachment);
        attachment.setPost(null);
        return this;
    }

    public Set<Ticket> getReports() {
        return this.reports;
    }

    public void setReports(Set<Ticket> tickets) {
        if (this.reports != null) {
            this.reports.forEach(i -> i.setPostRelated(null));
        }
        if (tickets != null) {
            tickets.forEach(i -> i.setPostRelated(this));
        }
        this.reports = tickets;
    }

    public Post reports(Set<Ticket> tickets) {
        this.setReports(tickets);
        return this;
    }

    public Post addReports(Ticket ticket) {
        this.reports.add(ticket);
        ticket.setPostRelated(this);
        return this;
    }

    public Post removeReports(Ticket ticket) {
        this.reports.remove(ticket);
        ticket.setPostRelated(null);
        return this;
    }

    public Set<Comment> getComments() {
        return this.comments;
    }

    public void setComments(Set<Comment> comments) {
        if (this.comments != null) {
            this.comments.forEach(i -> i.setPost(null));
        }
        if (comments != null) {
            comments.forEach(i -> i.setPost(this));
        }
        this.comments = comments;
    }

    public Post comments(Set<Comment> comments) {
        this.setComments(comments);
        return this;
    }

    public Post addComment(Comment comment) {
        this.comments.add(comment);
        comment.setPost(this);
        return this;
    }

    public Post removeComment(Comment comment) {
        this.comments.remove(comment);
        comment.setPost(null);
        return this;
    }

    public NgelmakAccount getAccount() {
        return this.account;
    }

    public void setAccount(NgelmakAccount ngelmakAccount) {
        this.account = ngelmakAccount;
    }

    public Post account(NgelmakAccount ngelmakAccount) {
        this.setAccount(ngelmakAccount);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Post)) {
            return false;
        }
        return getId() != null && getId().equals(((Post) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    /**
     * Path where to save the attachment file will be in the default root, in the default directory for attachments, the account id, and finaly in the folder named by the post id, i.e., resources/upload-dir/attachment-repos/<ngelmak-id>/<post-id>/ngelmak-image.png.
     * e.g., resources/upload-dir/attachment-repos/256/17623/ngelmak-image.png
     * @return the hierarchy directories on which the attachment will be saved.
     */
    public String[] getDirectories() {
        String[] dirs = new String[]{
            Constants.DEFAULT_ATTACHMENT_LOCAL_DIRECTORY, // default directory
            this.getAccount().getId().toString(), // <ngelmak-account-id> as subdir 
            this.getId().toString(), // <post-id> as subdir
        };
        return dirs;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Post{" +
            "id=" + getId() +
            ", title='" + getTitle() + "'" +
            ", subtitle='" + getSubtitle() + "'" +
            ", keywords='" + getKeywords() + "'" +
            ", subject='" + getSubject() + "'" +
            ", at='" + getAt() + "'" +
            ", lastUpdate='" + getLastUpdate() + "'" +
            ", visibility='" + getVisibility() + "'" +
            ", content='" + getContent() + "'" +
            ", status='" + getStatus() + "'" +
            "}";
    }
}
