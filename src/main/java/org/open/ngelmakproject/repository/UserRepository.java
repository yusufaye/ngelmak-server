package org.open.ngelmakproject.repository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import org.open.ngelmakproject.domain.User;
import org.open.ngelmakproject.domain.enumeration.CertificationStatus;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the {@link User} entity.
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    String USERS_BY_LOGIN_CACHE = "usersByLogin";

    String USERS_BY_EMAIL_CACHE = "usersByEmail";
    Optional<User> findOneByActivationKey(String activationKey);
    List<User> findAllByActivatedIsFalseAndActivationKeyIsNotNullAndCreatedDateBefore(Instant dateTime);
    Optional<User> findOneByResetKey(String resetKey);
    Optional<User> findOneByEmailIgnoreCase(String email);
    Optional<User> findOneByLogin(String login);
    Optional<User> findOneByOfficialDocIdentification(String officialDocIdentification);
    Optional<User> findOneByOfficialDocIdentificationAndCertificationStatusIn(String officialDocIdentification, CertificationStatus[] status);
    Optional<User> findOneByLoginAndCertificationStatus(String officialDocIdentification, CertificationStatus status);

    @EntityGraph(attributePaths = "authorities")
    Optional<User> findOneWithAuthoritiesByLogin(String login);

    @EntityGraph(attributePaths = "authorities")
    Optional<User> findOneWithAuthoritiesByEmailIgnoreCase(String email);

    Page<User> findAllByIdNotNullAndActivatedIsTrue(Pageable pageable);
}
