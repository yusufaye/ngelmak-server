package org.open.ngelmakproject.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.ZonedDateTime;

/**
 * A Membership.
 */
@Entity
@Table(name = "membership")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Membership implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "at")
    private ZonedDateTime at;

    @Column(name = "activate_notification")
    private Boolean activateNotification;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(
        value = { "configuration", "user", "reports", "owners", "comments", "memberships", "subscriptions", "posts", "reviews" },
        allowSetters = true
    )
    private NgelmakAccount account;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(
        value = { "configuration", "user", "reports", "owners", "comments", "memberships", "subscriptions", "posts", "reviews" },
        allowSetters = true
    )
    private NgelmakAccount subscriber;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Membership id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ZonedDateTime getAt() {
        return this.at;
    }

    public Membership at(ZonedDateTime at) {
        this.setAt(at);
        return this;
    }

    public void setAt(ZonedDateTime at) {
        this.at = at;
    }

    public Boolean getActivateNotification() {
        return this.activateNotification;
    }

    public Membership activateNotification(Boolean activateNotification) {
        this.setActivateNotification(activateNotification);
        return this;
    }

    public void setActivateNotification(Boolean activateNotification) {
        this.activateNotification = activateNotification;
    }

    public NgelmakAccount getAccount() {
        return this.account;
    }

    public void setAccount(NgelmakAccount ngelmakAccount) {
        this.account = ngelmakAccount;
    }

    public Membership account(NgelmakAccount ngelmakAccount) {
        this.setAccount(ngelmakAccount);
        return this;
    }

    public NgelmakAccount getSubscriber() {
        return this.subscriber;
    }

    public void setSubscriber(NgelmakAccount ngelmakAccount) {
        this.subscriber = ngelmakAccount;
    }

    public Membership subscriber(NgelmakAccount ngelmakAccount) {
        this.setSubscriber(ngelmakAccount);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Membership)) {
            return false;
        }
        return getId() != null && getId().equals(((Membership) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Membership{" +
            "id=" + getId() +
            ", at='" + getAt() + "'" +
            ", activateNotification='" + getActivateNotification() + "'" +
            "}";
    }
}
