package org.open.ngelmakproject.domain;

import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

import org.open.ngelmakproject.domain.enumeration.Accessibility;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/**
 * The Compte entity.
 */
@Entity
@Table(name = "nk_account")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class NkAccount implements Serializable {

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
    private Instant createdAt;

    /**
     * a default configuration can be set for visibility of posts and their eventual
     * attachments.
     */
    @OneToOne(fetch = FetchType.EAGER, optional = true)
    @JoinColumn(unique = true)
    private Config configuration;

    @OneToOne
    @JoinColumn(unique = true)
    @JsonIgnore
    private User user;

    /**
     * a ticket could be also related to a an account.
     */
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "accountRelated")
    @JsonIgnore
    private Set<Ticket> reports = new HashSet<>();

    /**
     * must be is issued by a user account.
     */
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "issuedby")
    @JsonIgnore
    private Set<Ticket> owners = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "account")
    @JsonIgnore
    private Set<Comment> comments = new HashSet<>();

    /**
     * any user can subscribe to any other user's account which my eventually have
     * any subscriber
     */
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "account")
    @JsonIgnore
    private Set<Membership> memberships = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "subscriber")
    @JsonIgnore
    private Set<Membership> subscriptions = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "account")
    @JsonIgnore
    private Set<Post> posts = new HashSet<>();

    /**
     * a review is done by a user
     */
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "account")
    @JsonIgnore
    private Set<Review> reviews = new HashSet<>();

    public Long getId() {
        return this.id;
    }

    public NkAccount id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public NkAccount name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return this.description;
    }

    public NkAccount description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getForegroundPicture() {
        return this.foregroundPicture;
    }

    public NkAccount foregroundPicture(String foregroundPicture) {
        this.setForegroundPicture(foregroundPicture);
        return this;
    }

    public void setForegroundPicture(String foregroundPicture) {
        this.foregroundPicture = foregroundPicture;
    }

    public String getBackgroundPicture() {
        return this.backgroundPicture;
    }

    public NkAccount backgroundPicture(String backgroundPicture) {
        this.setBackgroundPicture(backgroundPicture);
        return this;
    }

    public void setBackgroundPicture(String backgroundPicture) {
        this.backgroundPicture = backgroundPicture;
    }

    public Accessibility getVisibility() {
        return this.visibility;
    }

    public NkAccount visibility(Accessibility visibility) {
        this.setVisibility(visibility);
        return this;
    }

    public void setVisibility(Accessibility visibility) {
        this.visibility = visibility;
    }

    public Instant getCreatedAt() {
        return this.createdAt;
    }

    public NkAccount createdAt(Instant createdAt) {
        this.setCreatedAt(createdAt);
        return this;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Config getConfiguration() {
        return this.configuration;
    }

    public void setConfiguration(Config config) {
        this.configuration = config;
    }

    public NkAccount configuration(Config config) {
        this.setConfiguration(config);
        return this;
    }

    public User getUser() {
        return this.user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public NkAccount user(User user) {
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

    public NkAccount reports(Set<Ticket> tickets) {
        this.setReports(tickets);
        return this;
    }

    public NkAccount addReports(Ticket ticket) {
        this.reports.add(ticket);
        ticket.setAccountRelated(this);
        return this;
    }

    public NkAccount removeReports(Ticket ticket) {
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

    public NkAccount owners(Set<Ticket> tickets) {
        this.setOwners(tickets);
        return this;
    }

    public NkAccount addOwners(Ticket ticket) {
        this.owners.add(ticket);
        ticket.setIssuedby(this);
        return this;
    }

    public NkAccount removeOwners(Ticket ticket) {
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

    public NkAccount comments(Set<Comment> comments) {
        this.setComments(comments);
        return this;
    }

    public NkAccount addComment(Comment comment) {
        this.comments.add(comment);
        comment.setAccount(this);
        return this;
    }

    public NkAccount removeComment(Comment comment) {
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

    public NkAccount memberships(Set<Membership> memberships) {
        this.setMemberships(memberships);
        return this;
    }

    public NkAccount addMemberships(Membership membership) {
        this.memberships.add(membership);
        membership.setAccount(this);
        return this;
    }

    public NkAccount removeMemberships(Membership membership) {
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

    public NkAccount subscriptions(Set<Membership> memberships) {
        this.setSubscriptions(memberships);
        return this;
    }

    public NkAccount addSubscriptions(Membership membership) {
        this.subscriptions.add(membership);
        membership.setSubscriber(this);
        return this;
    }

    public NkAccount removeSubscriptions(Membership membership) {
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

    public NkAccount posts(Set<Post> posts) {
        this.setPosts(posts);
        return this;
    }

    public NkAccount addPost(Post post) {
        this.posts.add(post);
        post.setAccount(this);
        return this;
    }

    public NkAccount removePost(Post post) {
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

    public NkAccount reviews(Set<Review> reviews) {
        this.setReviews(reviews);
        return this;
    }

    public NkAccount addReview(Review review) {
        this.reviews.add(review);
        review.setAccount(this);
        return this;
    }

    public NkAccount removeReview(Review review) {
        this.reviews.remove(review);
        review.setAccount(null);
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof NkAccount)) {
            return false;
        }
        return getId() != null && getId().equals(((NkAccount) o).getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "NkAccount{" +
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
