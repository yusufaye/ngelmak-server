package org.open.ngelmakproject.repository;

import org.open.ngelmakproject.domain.NkTicket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the NkTicket entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TicketRepository extends JpaRepository<NkTicket, Long> {}
