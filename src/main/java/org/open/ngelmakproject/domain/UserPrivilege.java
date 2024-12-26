package org.open.ngelmakproject.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.time.Instant;

import org.open.ngelmakproject.domain.enumeration.GrantStatus;

/**
 * A UserPrivilege.
 */
@Entity
@Table(name = "user_privilege")
@SuppressWarnings("common-java:DuplicatedBlocks")
@NoArgsConstructor
@Getter
@Setter
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
