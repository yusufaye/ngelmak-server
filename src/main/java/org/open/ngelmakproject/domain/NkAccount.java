package org.open.ngelmakproject.domain;

import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

import org.open.ngelmakproject.domain.enumeration.Accessibility;

import com.fasterxml.jackson.annotation.JsonIgnore;

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

    // @NotNull
    // @NotBlank
    @Column(name = "identifier", length = 30, unique = true)
    private String identifier;

    @NotNull
    @NotBlank
    @Column(name = "name", length = 100, unique = true)
    private String name;

    @Column(name = "description", length = 1000)
    private String description;

    /**
     * Avatar or profile image.
     */
    @Column(name = "avatar")
    private String avatar;

    /**
     * Background image url.
     */
    @Column(name = "banner")
    private String banner;

    @Enumerated(EnumType.STRING)
    @Column(name = "visibility")
    private Accessibility visibility;

    @Column(name = "at")
    private Instant at;

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
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "follower")
    @JsonIgnore
    private Set<Membership> memberships = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "following")
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

    public String getIdentifier() {
        return this.identifier;
    }

    public NkAccount identifier(String identifier) {
        this.setIdentifier(identifier);
        return this;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
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

    public String getAvatar() {
        return this.avatar;
    }

    public NkAccount avatar(String avatar) {
        this.setAvatar(avatar);
        return this;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getBanner() {
        return this.banner;
    }

    public NkAccount banner(String banner) {
        this.setBanner(banner);
        return this;
    }

    public void setBanner(String banner) {
        this.banner = banner;
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

    public Instant getAt() {
        return this.at;
    }

    public NkAccount at(Instant at) {
        this.setAt(at);
        return this;
    }

    public void setAt(Instant at) {
        this.at = at;
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
            this.memberships.forEach(i -> i.setFollower(null));
        }
        if (memberships != null) {
            memberships.forEach(i -> i.setFollower(this));
        }
        this.memberships = memberships;
    }

    public NkAccount memberships(Set<Membership> memberships) {
        this.setMemberships(memberships);
        return this;
    }

    public NkAccount addMemberships(Membership membership) {
        this.memberships.add(membership);
        membership.setFollower(this);
        return this;
    }

    public NkAccount removeMemberships(Membership membership) {
        this.memberships.remove(membership);
        membership.setFollower(null);
        return this;
    }

    public Set<Membership> getSubscriptions() {
        return this.subscriptions;
    }

    public void setSubscriptions(Set<Membership> memberships) {
        if (this.subscriptions != null) {
            this.subscriptions.forEach(i -> i.setFollower(null));
        }
        if (memberships != null) {
            memberships.forEach(i -> i.setFollower(this));
        }
        this.subscriptions = memberships;
    }

    public NkAccount subscriptions(Set<Membership> memberships) {
        this.setSubscriptions(memberships);
        return this;
    }

    public NkAccount addSubscriptions(Membership membership) {
        this.subscriptions.add(membership);
        membership.setFollower(this);
        return this;
    }

    public NkAccount removeSubscriptions(Membership membership) {
        this.subscriptions.remove(membership);
        membership.setFollower(null);
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
                ", identifier='" + getIdentifier() + "'" +
                ", name='" + getName() + "'" +
                ", description='" + getDescription() + "'" +
                ", avatar='" + getAvatar() + "'" +
                ", banner='" + getBanner() + "'" +
                ", visibility='" + getVisibility() + "'" +
                ", at='" + getAt() + "'" +
                "}";
    }
}
