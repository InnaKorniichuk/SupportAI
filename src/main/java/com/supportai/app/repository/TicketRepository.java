package com.supportai.app.repository;

import com.supportai.app.model.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface TicketRepository extends JpaRepository<Ticket, Long> {
    List<Ticket> findAllByAssignedAgentId(Long id);

    List<Ticket> findByCustomerId(Long customerId);
}
