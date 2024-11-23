package org.open.ngelmakproject.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.io.Serializable;
import java.time.ZonedDateTime;
import org.open.ngelmakproject.domain.enumeration.Accessibility;
import org.open.ngelmakproject.domain.enumeration.Visibility;

/**
 * A Config.
 */
@Entity
@Table(name = "config")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Config implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "last_update")
    private ZonedDateTime lastUpdate;

    @Enumerated(EnumType.STRING)
    @Column(name = "default_accessibility")
    private Accessibility defaultAccessibility;

    @Enumerated(EnumType.STRING)
    @Column(name = "default_visibility")
    private Visibility defaultVisibility;

    @JsonIgnoreProperties(value = { "configuration", "user", "reports", "owners", "comments", "memberships",
            "subscriptions", "posts", "reviews" }, allowSetters = true)
    @OneToOne(fetch = FetchType.LAZY, mappedBy = "configuration")
    private NgelmakAccount ngelmakAccount;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Config id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ZonedDateTime getLastUpdate() {
        return this.lastUpdate;
    }

    public Config lastUpdate(ZonedDateTime lastUpdate) {
        this.setLastUpdate(lastUpdate);
        return this;
    }

    public void setLastUpdate(ZonedDateTime lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    public Accessibility getDefaultAccessibility() {
        return this.defaultAccessibility;
    }

    public Config defaultAccessibility(Accessibility defaultAccessibility) {
        this.setDefaultAccessibility(defaultAccessibility);
        return this;
    }

    public void setDefaultAccessibility(Accessibility defaultAccessibility) {
        this.defaultAccessibility = defaultAccessibility;
    }

    public Visibility getDefaultVisibility() {
        return this.defaultVisibility;
    }

    public Config defaultVisibility(Visibility defaultVisibility) {
        this.setDefaultVisibility(defaultVisibility);
        return this;
    }

    public void setDefaultVisibility(Visibility defaultVisibility) {
        this.defaultVisibility = defaultVisibility;
    }

    public NgelmakAccount getNgelmakAccount() {
        return this.ngelmakAccount;
    }

    public void setNgelmakAccount(NgelmakAccount ngelmakAccount) {
        if (this.ngelmakAccount != null) {
            this.ngelmakAccount.setConfiguration(null);
        }
        if (ngelmakAccount != null) {
            ngelmakAccount.setConfiguration(this);
        }
        this.ngelmakAccount = ngelmakAccount;
    }

    public Config ngelmakAccount(NgelmakAccount ngelmakAccount) {
        this.setNgelmakAccount(ngelmakAccount);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and
    // setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Config)) {
            return false;
        }
        return getId() != null && getId().equals(((Config) o).getId());
    }

    @Override
    public int hashCode() {
        // see
        // https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Config{" +
                "id=" + getId() +
                ", lastUpdate='" + getLastUpdate() + "'" +
                ", defaultAccessibility='" + getDefaultAccessibility() + "'" +
                ", defaultVisibility='" + getDefaultVisibility() + "'" +
                "}";
    }
}
