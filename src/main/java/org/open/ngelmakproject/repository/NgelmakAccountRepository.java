package org.open.ngelmakproject.repository;

import java.lang.StackWalker.Option;

import org.open.ngelmakproject.domain.NgelmakAccount;
import org.open.ngelmakproject.domain.User;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;


/**
 * Spring Data JPA repository for the NgelmakAccount entity.
 */
@SuppressWarnings("unused")
@Repository
public interface NgelmakAccountRepository extends JpaRepository<NgelmakAccount, Long> {
    Optional<NgelmakAccount> findByUser(User user);
}
