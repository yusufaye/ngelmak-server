package org.open.ngelmakproject.domain;

import java.io.Serializable;
import java.time.Instant;

import org.open.ngelmakproject.domain.enumeration.GrantStatus;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * A UserPrivilege.
 */
@Entity
@Table(name = "nk_user_privilege")
@Data
public class UserPrivilege implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "grant_status", nullable = false)
    private GrantStatus grantStatus;

    @NotNull
    @Column(name = "date", nullable = false)
    private Instant date;

    @NotNull
    @Column(name = "last_updated_date", nullable = false)
    private Instant lastUpdatedDate;

    @Column(name = "comment")
    private String comment;

    @NotNull
    @ManyToOne(optional = false)
    @JsonIgnoreProperties(value = { "ngelmakAccountPrivileges" }, allowSetters = true)
    private Privilege privilege;

    @ManyToOne(optional = true)
    @JsonIgnoreProperties(value = { "authorities" }, allowSetters = true)
    private User lastUpdatedBy;

    @NotNull
    @ManyToOne(optional = false, cascade = CascadeType.REMOVE)
    @JsonIgnoreProperties(value = { "authorities" }, allowSetters = true)
    private User grantedTo;
}
