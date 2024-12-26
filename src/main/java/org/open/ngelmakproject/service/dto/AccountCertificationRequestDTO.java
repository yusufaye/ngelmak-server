package org.open.ngelmakproject.service.dto;

import java.io.Serializable;

import org.open.ngelmakproject.domain.User;
import org.open.ngelmakproject.domain.enumeration.OfficialDocType;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * A DTO representing a account certification required data - document type and
 * document identification.
 */
@NoArgsConstructor
@Setter
@Getter
public class AccountCertificationRequestDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotNull
    private OfficialDocType officialDocType;
    @NotBlank
    private String officialDocIdentification;

    public AccountCertificationRequestDTO(User user) {
        this.officialDocIdentification = user.getOfficialDocIdentification();
        this.officialDocType = user.getOfficialDocType();
    }

}
