package org.open.ngelmakproject.repository;

import java.lang.StackWalker.Option;

import org.open.ngelmakproject.domain.NkAccount;
import org.open.ngelmakproject.domain.NkPost;
import org.open.ngelmakproject.domain.User;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

/**
 * Spring Data JPA repository for the NkAccount entity.
 */
@SuppressWarnings("unused")
@Repository
public interface NkAccountRepository extends JpaRepository<NkAccount, Long> {
    Optional<NkAccount> findOneByUser(User user);
}
