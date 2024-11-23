package org.open.ngelmakproject.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.Set;
import org.open.ngelmakproject.domain.enumeration.Opinion;

/**
 * A Comment.
 */
@Entity
@Table(name = "comment")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Comment implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "opinion", nullable = false)
    private Opinion opinion;

    @NotNull
    @Column(name = "at", nullable = false)
    private ZonedDateTime at;

    @Column(name = "last_update")
    private ZonedDateTime lastUpdate;

    @Column(name = "content", nullable = false)
    private String content;

    /**
     * a ticket can be related to a abusive comment.
     */
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "commentRelated")
    @JsonIgnoreProperties(value = { "reviews", "postRelated", "commentRelated", "accountRelated", "issuedby" }, allowSetters = true)
    private Set<Ticket> reports = new HashSet<>();

    /**
     * a comment can have multiple subcomments (reply), each issued by one user.
     */
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "replayto")
    @JsonIgnoreProperties(value = { "reports", "comments", "post", "replayto", "account" }, allowSetters = true)
    private Set<Comment> comments = new HashSet<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "attachments", "reports", "comments", "account" }, allowSetters = true)
    private Post post;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "reports", "comments", "post", "replayto", "account" }, allowSetters = true)
    private Comment replayto;

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

    public Comment id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Opinion getOpinion() {
        return this.opinion;
    }

    public Comment opinion(Opinion opinion) {
        this.setOpinion(opinion);
        return this;
    }

    public void setOpinion(Opinion opinion) {
        this.opinion = opinion;
    }

    public ZonedDateTime getAt() {
        return this.at;
    }

    public Comment at(ZonedDateTime at) {
        this.setAt(at);
        return this;
    }

    public void setAt(ZonedDateTime at) {
        this.at = at;
    }

    public ZonedDateTime getLastUpdate() {
        return this.lastUpdate;
    }

    public Comment lastUpdate(ZonedDateTime lastUpdate) {
        this.setLastUpdate(lastUpdate);
        return this;
    }

    public void setLastUpdate(ZonedDateTime lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    public String getContent() {
        return this.content;
    }

    public Comment content(String content) {
        this.setContent(content);
        return this;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Set<Ticket> getReports() {
        return this.reports;
    }

    public void setReports(Set<Ticket> tickets) {
        if (this.reports != null) {
            this.reports.forEach(i -> i.setCommentRelated(null));
        }
        if (tickets != null) {
            tickets.forEach(i -> i.setCommentRelated(this));
        }
        this.reports = tickets;
    }

    public Comment reports(Set<Ticket> tickets) {
        this.setReports(tickets);
        return this;
    }

    public Comment addReports(Ticket ticket) {
        this.reports.add(ticket);
        ticket.setCommentRelated(this);
        return this;
    }

    public Comment removeReports(Ticket ticket) {
        this.reports.remove(ticket);
        ticket.setCommentRelated(null);
        return this;
    }

    public Set<Comment> getComments() {
        return this.comments;
    }

    public void setComments(Set<Comment> comments) {
        if (this.comments != null) {
            this.comments.forEach(i -> i.setReplayto(null));
        }
        if (comments != null) {
            comments.forEach(i -> i.setReplayto(this));
        }
        this.comments = comments;
    }

    public Comment comments(Set<Comment> comments) {
        this.setComments(comments);
        return this;
    }

    public Comment addComment(Comment comment) {
        this.comments.add(comment);
        comment.setReplayto(this);
        return this;
    }

    public Comment removeComment(Comment comment) {
        this.comments.remove(comment);
        comment.setReplayto(null);
        return this;
    }

    public Post getPost() {
        return this.post;
    }

    public void setPost(Post post) {
        this.post = post;
    }

    public Comment post(Post post) {
        this.setPost(post);
        return this;
    }

    public Comment getReplayto() {
        return this.replayto;
    }

    public void setReplayto(Comment comment) {
        this.replayto = comment;
    }

    public Comment replayto(Comment comment) {
        this.setReplayto(comment);
        return this;
    }

    public NgelmakAccount getAccount() {
        return this.account;
    }

    public void setAccount(NgelmakAccount ngelmakAccount) {
        this.account = ngelmakAccount;
    }

    public Comment account(NgelmakAccount ngelmakAccount) {
        this.setAccount(ngelmakAccount);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Comment)) {
            return false;
        }
        return getId() != null && getId().equals(((Comment) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Comment{" +
            "id=" + getId() +
            ", opinion='" + getOpinion() + "'" +
            ", at='" + getAt() + "'" +
            ", lastUpdate='" + getLastUpdate() + "'" +
            ", content='" + getContent() + "'" +
            "}";
    }
}
