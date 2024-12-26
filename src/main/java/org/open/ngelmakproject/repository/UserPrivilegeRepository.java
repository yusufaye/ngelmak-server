package org.open.ngelmakproject.repository;

import java.util.List;
import java.util.Optional;

import org.open.ngelmakproject.domain.User;
import org.open.ngelmakproject.domain.UserPrivilege;
import org.open.ngelmakproject.domain.Privilege;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Privilege entity.
 */
@SuppressWarnings("unused")
@Repository
public interface UserPrivilegeRepository extends JpaRepository<UserPrivilege, Long> {
  boolean existsByPrivilegeAndGrantedTo(Privilege privilege,
      User user);

  Optional<UserPrivilege> findOneByPrivilegeAndGrantedTo(Privilege privilege,
      User user);

  List<UserPrivilege> findByGrantedTo(User user);

  @Query("select u from UserPrivilege u where u.grantedTo.login = ?1")
  List<UserPrivilege> findByGrantedToLogin(String login);
}
