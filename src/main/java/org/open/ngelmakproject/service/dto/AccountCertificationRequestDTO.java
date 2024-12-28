package org.open.ngelmakproject.service.dto;

import java.io.Serializable;

import org.open.ngelmakproject.domain.User;
import org.open.ngelmakproject.domain.enumeration.DocType;

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
    private DocType docType;
    @NotBlank
    private String docId;

    public AccountCertificationRequestDTO(User user) {
        this.docId = user.getDocId();
        this.docType = user.getDocType();
    }

}
