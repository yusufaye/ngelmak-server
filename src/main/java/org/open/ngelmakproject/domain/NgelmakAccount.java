package org.open.ngelmakproject.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.Set;
import org.open.ngelmakproject.domain.enumeration.Accessibility;

/**
 * The Compte entity.
 * @author A true hipster
 */
@Entity
@Table(name = "ngelmak_account")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class NgelmakAccount implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @NotBlank
    @Column(name = "name")
    private String name;

    @NotNull
    @NotBlank
    @Column(name = "description")
    private String description;

    @Column(name = "foreground_picture")
    private String foregroundPicture;

    @Column(name = "background_picture")
    private String backgroundPicture;

    @Enumerated(EnumType.STRING)
    @Column(name = "visibility")
    private Accessibility visibility;

    @Column(name = "created_at")
    private ZonedDateTime createdAt;

    /**
     * a default configuration can be set for visibility of posts and their eventual attachments.
     */
    @JsonIgnoreProperties(value = { "ngelmakAccount" }, allowSetters = true)
    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @NotNull
    @JoinColumn(unique = true)
    private Config configuration;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(unique = true)
    private User user;

    /**
     * a ticket could be also related to a an account.
     */
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "accountRelated")
    @JsonIgnoreProperties(value = { "reviews", "postRelated", "commentRelated", "accountRelated", "issuedby" }, allowSetters = true)
    private Set<Ticket> reports = new HashSet<>();

    /**
     * must be is issued by a user account.
     */
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "issuedby")
    @JsonIgnoreProperties(value = { "reviews", "postRelated", "commentRelated", "accountRelated", "issuedby" }, allowSetters = true)
    private Set<Ticket> owners = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "account")
    @JsonIgnoreProperties(value = { "reports", "comments", "post", "replayto", "account" }, allowSetters = true)
    private Set<Comment> comments = new HashSet<>();

    /**
     * any user can subscribe to any other user's account which my eventually have any subscriber
     */
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "account")
    @JsonIgnoreProperties(value = { "account", "subscriber" }, allowSetters = true)
    private Set<Membership> memberships = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "subscriber")
    @JsonIgnoreProperties(value = { "account", "subscriber" }, allowSetters = true)
    private Set<Membership> subscriptions = new HashSet<>();

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "account")
    @JsonIgnoreProperties(value = { "attachments", "reports", "comments", "account" }, allowSetters = true)
    private Set<Post> posts = new HashSet<>();

    /**
     * a review is done by a user
     */
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "account")
    @JsonIgnoreProperties(value = { "reviews", "account", "ticket", "replyto" }, allowSetters = true)
    private Set<Review> reviews = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public NgelmakAccount id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public NgelmakAccount name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return this.description;
    }

    public NgelmakAccount description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getForegroundPicture() {
        return this.foregroundPicture;
    }

    public NgelmakAccount foregroundPicture(String foregroundPicture) {
        this.setForegroundPicture(foregroundPicture);
        return this;
    }

    public void setForegroundPicture(String foregroundPicture) {
        this.foregroundPicture = foregroundPicture;
    }

    public String getBackgroundPicture() {
        return this.backgroundPicture;
    }

    public NgelmakAccount backgroundPicture(String backgroundPicture) {
        this.setBackgroundPicture(backgroundPicture);
        return this;
    }

    public void setBackgroundPicture(String backgroundPicture) {
        this.backgroundPicture = backgroundPicture;
    }

    public Accessibility getVisibility() {
        return this.visibility;
    }

    public NgelmakAccount visibility(Accessibility visibility) {
        this.setVisibility(visibility);
        return this;
    }

    public void setVisibility(Accessibility visibility) {
        this.visibility = visibility;
    }

    public ZonedDateTime getCreatedAt() {
        return this.createdAt;
    }

    public NgelmakAccount createdAt(ZonedDateTime createdAt) {
        this.setCreatedAt(createdAt);
        return this;
    }

    public void setCreatedAt(ZonedDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public Config getConfiguration() {
        return this.configuration;
    }

    public void setConfiguration(Config config) {
        this.configuration = config;
    }

    public NgelmakAccount configuration(Config config) {
        this.setConfiguration(config);
        return this;
    }

    public User getUser() {
        return this.user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public NgelmakAccount user(User user) {
        this.setUser(user);
        return this;
    }

    public Set<Ticket> getReports() {
        return this.reports;
    }

    public void setReports(Set<Ticket> tickets) {
        if (this.reports != null) {
            this.reports.forEach(i -> i.setAccountRelated(null));
        }
        if (tickets != null) {
            tickets.forEach(i -> i.setAccountRelated(this));
        }
        this.reports = tickets;
    }

    public NgelmakAccount reports(Set<Ticket> tickets) {
        this.setReports(tickets);
        return this;
    }

    public NgelmakAccount addReports(Ticket ticket) {
        this.reports.add(ticket);
        ticket.setAccountRelated(this);
        return this;
    }

    public NgelmakAccount removeReports(Ticket ticket) {
        this.reports.remove(ticket);
        ticket.setAccountRelated(null);
        return this;
    }

    public Set<Ticket> getOwners() {
        return this.owners;
    }

    public void setOwners(Set<Ticket> tickets) {
        if (this.owners != null) {
            this.owners.forEach(i -> i.setIssuedby(null));
        }
        if (tickets != null) {
            tickets.forEach(i -> i.setIssuedby(this));
        }
        this.owners = tickets;
    }

    public NgelmakAccount owners(Set<Ticket> tickets) {
        this.setOwners(tickets);
        return this;
    }

    public NgelmakAccount addOwners(Ticket ticket) {
        this.owners.add(ticket);
        ticket.setIssuedby(this);
        return this;
    }

    public NgelmakAccount removeOwners(Ticket ticket) {
        this.owners.remove(ticket);
        ticket.setIssuedby(null);
        return this;
    }

    public Set<Comment> getComments() {
        return this.comments;
    }

    public void setComments(Set<Comment> comments) {
        if (this.comments != null) {
            this.comments.forEach(i -> i.setAccount(null));
        }
        if (comments != null) {
            comments.forEach(i -> i.setAccount(this));
        }
        this.comments = comments;
    }

    public NgelmakAccount comments(Set<Comment> comments) {
        this.setComments(comments);
        return this;
    }

    public NgelmakAccount addComment(Comment comment) {
        this.comments.add(comment);
        comment.setAccount(this);
        return this;
    }

    public NgelmakAccount removeComment(Comment comment) {
        this.comments.remove(comment);
        comment.setAccount(null);
        return this;
    }

    public Set<Membership> getMemberships() {
        return this.memberships;
    }

    public void setMemberships(Set<Membership> memberships) {
        if (this.memberships != null) {
            this.memberships.forEach(i -> i.setAccount(null));
        }
        if (memberships != null) {
            memberships.forEach(i -> i.setAccount(this));
        }
        this.memberships = memberships;
    }

    public NgelmakAccount memberships(Set<Membership> memberships) {
        this.setMemberships(memberships);
        return this;
    }

    public NgelmakAccount addMemberships(Membership membership) {
        this.memberships.add(membership);
        membership.setAccount(this);
        return this;
    }

    public NgelmakAccount removeMemberships(Membership membership) {
        this.memberships.remove(membership);
        membership.setAccount(null);
        return this;
    }

    public Set<Membership> getSubscriptions() {
        return this.subscriptions;
    }

    public void setSubscriptions(Set<Membership> memberships) {
        if (this.subscriptions != null) {
            this.subscriptions.forEach(i -> i.setSubscriber(null));
        }
        if (memberships != null) {
            memberships.forEach(i -> i.setSubscriber(this));
        }
        this.subscriptions = memberships;
    }

    public NgelmakAccount subscriptions(Set<Membership> memberships) {
        this.setSubscriptions(memberships);
        return this;
    }

    public NgelmakAccount addSubscriptions(Membership membership) {
        this.subscriptions.add(membership);
        membership.setSubscriber(this);
        return this;
    }

    public NgelmakAccount removeSubscriptions(Membership membership) {
        this.subscriptions.remove(membership);
        membership.setSubscriber(null);
        return this;
    }

    public Set<Post> getPosts() {
        return this.posts;
    }

    public void setPosts(Set<Post> posts) {
        if (this.posts != null) {
            this.posts.forEach(i -> i.setAccount(null));
        }
        if (posts != null) {
            posts.forEach(i -> i.setAccount(this));
        }
        this.posts = posts;
    }

    public NgelmakAccount posts(Set<Post> posts) {
        this.setPosts(posts);
        return this;
    }

    public NgelmakAccount addPost(Post post) {
        this.posts.add(post);
        post.setAccount(this);
        return this;
    }

    public NgelmakAccount removePost(Post post) {
        this.posts.remove(post);
        post.setAccount(null);
        return this;
    }

    public Set<Review> getReviews() {
        return this.reviews;
    }

    public void setReviews(Set<Review> reviews) {
        if (this.reviews != null) {
            this.reviews.forEach(i -> i.setAccount(null));
        }
        if (reviews != null) {
            reviews.forEach(i -> i.setAccount(this));
        }
        this.reviews = reviews;
    }

    public NgelmakAccount reviews(Set<Review> reviews) {
        this.setReviews(reviews);
        return this;
    }

    public NgelmakAccount addReview(Review review) {
        this.reviews.add(review);
        review.setAccount(this);
        return this;
    }

    public NgelmakAccount removeReview(Review review) {
        this.reviews.remove(review);
        review.setAccount(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof NgelmakAccount)) {
            return false;
        }
        return getId() != null && getId().equals(((NgelmakAccount) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "NgelmakAccount{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", description='" + getDescription() + "'" +
            ", foregroundPicture='" + getForegroundPicture() + "'" +
            ", backgroundPicture='" + getBackgroundPicture() + "'" +
            ", visibility='" + getVisibility() + "'" +
            ", createdAt='" + getCreatedAt() + "'" +
            "}";
    }
}
