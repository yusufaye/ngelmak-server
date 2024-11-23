package org.open.ngelmakproject.service.dto;

import java.io.Serializable;
import java.io.ObjectInputFilter.Config;
import java.time.ZonedDateTime;

import org.open.ngelmakproject.domain.enumeration.Accessibility;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PostDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    @NotNull
    private String name;

    private String foregroundPicture;

    private String backgroundPicture;

    private Accessibility visibility;

    private ZonedDateTime createdAt;

}
