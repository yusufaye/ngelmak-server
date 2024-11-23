package org.open.ngelmakproject.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.Set;
import org.open.ngelmakproject.domain.enumeration.Status;

/**
 * A Review.
 */
@Entity
@Table(name = "review")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Review implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "at", nullable = false)
    private ZonedDateTime at;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private Status status;

    /**
     * number of minutes to wait before timeout.
     */
    @NotNull
    @Column(name = "timeout", nullable = false)
    private Integer timeout;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "replyto")
    @JsonIgnoreProperties(value = { "reviews", "account", "ticket", "replyto" }, allowSetters = true)
    private Set<Review> reviews = new HashSet<>();

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(
        value = { "configuration", "user", "reports", "owners", "comments", "memberships", "subscriptions", "posts", "reviews" },
        allowSetters = true
    )
    private NgelmakAccount account;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "reviews", "postRelated", "commentRelated", "accountRelated", "issuedby" }, allowSetters = true)
    private Ticket ticket;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "reviews", "account", "ticket", "replyto" }, allowSetters = true)
    private Review replyto;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Review id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ZonedDateTime getAt() {
        return this.at;
    }

    public Review at(ZonedDateTime at) {
        this.setAt(at);
        return this;
    }

    public void setAt(ZonedDateTime at) {
        this.at = at;
    }

    public Status getStatus() {
        return this.status;
    }

    public Review status(Status status) {
        this.setStatus(status);
        return this;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Integer getTimeout() {
        return this.timeout;
    }

    public Review timeout(Integer timeout) {
        this.setTimeout(timeout);
        return this;
    }

    public void setTimeout(Integer timeout) {
        this.timeout = timeout;
    }

    public Set<Review> getReviews() {
        return this.reviews;
    }

    public void setReviews(Set<Review> reviews) {
        if (this.reviews != null) {
            this.reviews.forEach(i -> i.setReplyto(null));
        }
        if (reviews != null) {
            reviews.forEach(i -> i.setReplyto(this));
        }
        this.reviews = reviews;
    }

    public Review reviews(Set<Review> reviews) {
        this.setReviews(reviews);
        return this;
    }

    public Review addReview(Review review) {
        this.reviews.add(review);
        review.setReplyto(this);
        return this;
    }

    public Review removeReview(Review review) {
        this.reviews.remove(review);
        review.setReplyto(null);
        return this;
    }

    public NgelmakAccount getAccount() {
        return this.account;
    }

    public void setAccount(NgelmakAccount ngelmakAccount) {
        this.account = ngelmakAccount;
    }

    public Review account(NgelmakAccount ngelmakAccount) {
        this.setAccount(ngelmakAccount);
        return this;
    }

    public Ticket getTicket() {
        return this.ticket;
    }

    public void setTicket(Ticket ticket) {
        this.ticket = ticket;
    }

    public Review ticket(Ticket ticket) {
        this.setTicket(ticket);
        return this;
    }

    public Review getReplyto() {
        return this.replyto;
    }

    public void setReplyto(Review review) {
        this.replyto = review;
    }

    public Review replyto(Review review) {
        this.setReplyto(review);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Review)) {
            return false;
        }
        return getId() != null && getId().equals(((Review) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Review{" +
            "id=" + getId() +
            ", at='" + getAt() + "'" +
            ", status='" + getStatus() + "'" +
            ", timeout=" + getTimeout() +
            "}";
    }
}
