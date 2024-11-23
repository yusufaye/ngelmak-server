package org.open.ngelmakproject.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.Set;
import org.open.ngelmakproject.domain.enumeration.TicketType;

/**
 * A Ticket.
 */
@Entity
@Table(name = "ticket")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Ticket implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    /**
     * main title of the ticket
     */
    @NotNull
    @Size(min = 50, max = 200)
    @Column(name = "object", length = 200, nullable = false)
    private String object;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    private TicketType type;

    @NotNull
    @Column(name = "at", nullable = false)
    private ZonedDateTime at;

    @Column(name = "closed")
    private Boolean closed;

    @Column(name = "content")
    private String content;

    /**
     * a review is either related to a ticket or is a reply to another review.
     */
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "ticket")
    @JsonIgnoreProperties(value = { "reviews", "account", "ticket", "replyto" }, allowSetters = true)
    private Set<Review> reviews = new HashSet<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "attachments", "reports", "comments", "account" }, allowSetters = true)
    private Post postRelated;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "reports", "comments", "post", "replayto", "account" }, allowSetters = true)
    private Comment commentRelated;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(
        value = { "configuration", "user", "reports", "owners", "comments", "memberships", "subscriptions", "posts", "reviews" },
        allowSetters = true
    )
    private NgelmakAccount accountRelated;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(
        value = { "configuration", "user", "reports", "owners", "comments", "memberships", "subscriptions", "posts", "reviews" },
        allowSetters = true
    )
    private NgelmakAccount issuedby;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Ticket id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getObject() {
        return this.object;
    }

    public Ticket object(String object) {
        this.setObject(object);
        return this;
    }

    public void setObject(String object) {
        this.object = object;
    }

    public TicketType getType() {
        return this.type;
    }

    public Ticket type(TicketType type) {
        this.setType(type);
        return this;
    }

    public void setType(TicketType type) {
        this.type = type;
    }

    public ZonedDateTime getAt() {
        return this.at;
    }

    public Ticket at(ZonedDateTime at) {
        this.setAt(at);
        return this;
    }

    public void setAt(ZonedDateTime at) {
        this.at = at;
    }

    public Boolean getClosed() {
        return this.closed;
    }

    public Ticket closed(Boolean closed) {
        this.setClosed(closed);
        return this;
    }

    public void setClosed(Boolean closed) {
        this.closed = closed;
    }

    public String getContent() {
        return this.content;
    }

    public Ticket content(String content) {
        this.setContent(content);
        return this;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Set<Review> getReviews() {
        return this.reviews;
    }

    public void setReviews(Set<Review> reviews) {
        if (this.reviews != null) {
            this.reviews.forEach(i -> i.setTicket(null));
        }
        if (reviews != null) {
            reviews.forEach(i -> i.setTicket(this));
        }
        this.reviews = reviews;
    }

    public Ticket reviews(Set<Review> reviews) {
        this.setReviews(reviews);
        return this;
    }

    public Ticket addReview(Review review) {
        this.reviews.add(review);
        review.setTicket(this);
        return this;
    }

    public Ticket removeReview(Review review) {
        this.reviews.remove(review);
        review.setTicket(null);
        return this;
    }

    public Post getPostRelated() {
        return this.postRelated;
    }

    public void setPostRelated(Post post) {
        this.postRelated = post;
    }

    public Ticket postRelated(Post post) {
        this.setPostRelated(post);
        return this;
    }

    public Comment getCommentRelated() {
        return this.commentRelated;
    }

    public void setCommentRelated(Comment comment) {
        this.commentRelated = comment;
    }

    public Ticket commentRelated(Comment comment) {
        this.setCommentRelated(comment);
        return this;
    }

    public NgelmakAccount getAccountRelated() {
        return this.accountRelated;
    }

    public void setAccountRelated(NgelmakAccount ngelmakAccount) {
        this.accountRelated = ngelmakAccount;
    }

    public Ticket accountRelated(NgelmakAccount ngelmakAccount) {
        this.setAccountRelated(ngelmakAccount);
        return this;
    }

    public NgelmakAccount getIssuedby() {
        return this.issuedby;
    }

    public void setIssuedby(NgelmakAccount ngelmakAccount) {
        this.issuedby = ngelmakAccount;
    }

    public Ticket issuedby(NgelmakAccount ngelmakAccount) {
        this.setIssuedby(ngelmakAccount);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Ticket)) {
            return false;
        }
        return getId() != null && getId().equals(((Ticket) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Ticket{" +
            "id=" + getId() +
            ", object='" + getObject() + "'" +
            ", type='" + getType() + "'" +
            ", at='" + getAt() + "'" +
            ", closed='" + getClosed() + "'" +
            ", content='" + getContent() + "'" +
            "}";
    }
}
