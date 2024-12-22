package org.open.ngelmakproject.service.dto;

import java.io.Serializable;

import org.open.ngelmakproject.domain.Privilege;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * A DTO representing a user, with only the public attributes.
 */
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UserPrivilegeDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    @NotNull
    private String login;

    @NotNull
    private Privilege privilege;
    
    @NotNull
    @NotBlank
    private String comment;
}
