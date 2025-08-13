package org.open.ngelmakproject.repository;

import java.util.List;
import java.util.Optional;

import org.open.ngelmakproject.domain.NkPrivilege;
import org.open.ngelmakproject.domain.User;
import org.open.ngelmakproject.domain.UserPrivilege;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Privilege entity.
 */
@SuppressWarnings("unused")
@Repository
public interface UserPrivilegeRepository extends JpaRepository<UserPrivilege, Long> {
  boolean existsByPrivilegeAndGrantedTo(NkPrivilege privilege, User user);

  Optional<UserPrivilege> findOneByPrivilegeAndGrantedTo(NkPrivilege privilege, User user);

  List<UserPrivilege> findByGrantedTo(User user);

  @Query("SELECT u FROM UserPrivilege u WHERE u.grantedTo.login = ?1")
  List<UserPrivilege> findByGrantedToLogin(String login);
}
