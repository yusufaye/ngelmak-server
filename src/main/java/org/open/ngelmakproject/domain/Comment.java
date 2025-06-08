package org.open.ngelmakproject.domain;

import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

import org.open.ngelmakproject.domain.enumeration.Opinion;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonIncludeProperties;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;

/**
 * A Comment.
 */
@Entity
@Table(name = "nk_comment")
@JsonIgnoreProperties(ignoreUnknown = true)
public class Comment implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "opinion")
    private Opinion opinion;

    @NotNull
    @Column(name = "at", nullable = false)
    private Instant at;

    @Column(name = "last_update")
    private Instant lastUpdate;

    @Column(name = "deleted_at")
    private Instant deletedAt;

    @Column(name = "content", columnDefinition = "TEXT", nullable = false)
    private String content;

    @Column(name = "url", nullable = true)
    private String url;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @NotNull
    @JsonIncludeProperties(value = { "id" })
    private Post post;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.DETACH)
    @JsonIncludeProperties(value = { "id" })
    private Comment replayto;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @NotNull
    @JsonIncludeProperties(value = { "id" })
    private NkAccount account;

    /**
     * a ticket can be related to a abusive comment.
     */
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "commentRelated")
    @JsonIgnore
    private Set<Ticket> reports = new HashSet<>();

    /**
     * a comment can have multiple subcomments (reply), each issued by one user.
     */
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "replayto")
    @JsonIgnore
    private Set<Comment> comments = new HashSet<>();

    public Comment() {
    }

    public Comment(Long id,
            Opinion opinion,
            Instant at,
            Instant lastUpdate,
            Instant deletedAt,
            String content,
            String url,
            Post post,
            Comment replayto,
            NkAccount account) {
        this.id = id;
        this.opinion = opinion;
        this.at = at;
        this.lastUpdate = lastUpdate;
        this.deletedAt = deletedAt;
        this.content = content;
        this.url = url;
        this.post = post;
        this.replayto = replayto;
        this.account = account;
    }

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

    public Instant getAt() {
        return this.at;
    }

    public Comment at(Instant at) {
        this.setAt(at);
        return this;
    }

    public void setAt(Instant at) {
        this.at = at;
    }

    public Instant getLastUpdate() {
        return this.lastUpdate;
    }

    public Comment lastUpdate(Instant lastUpdate) {
        this.setLastUpdate(lastUpdate);
        return this;
    }

    public void setLastUpdate(Instant lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    public Instant getDeleteAt() {
        return this.deletedAt;
    }

    public Comment deletedAt(Instant deletedAt) {
        this.setDeleteAt(deletedAt);
        return this;
    }

    public void setDeleteAt(Instant deletedAt) {
        this.deletedAt = deletedAt;
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

    public boolean hasUrl() {
        return (url != null) && !url.isEmpty();
    }

    public String getUrl() {
        return this.url;
    }

    public Comment url(String url) {
        this.setUrl(url);
        return this;
    }

    public void setUrl(String url) {
        this.url = url;
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

    public NkAccount getAccount() {
        return this.account;
    }

    public void setAccount(NkAccount nkAccount) {
        this.account = nkAccount;
    }

    public Comment account(NkAccount nkAccount) {
        this.setAccount(nkAccount);
        return this;
    }

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
                ", url='" + getUrl() + "'" +
                "}";
    }
}
