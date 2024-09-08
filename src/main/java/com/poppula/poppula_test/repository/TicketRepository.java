package com.poppula.poppula_test.repository;

import com.poppula.poppula_test.entity.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TicketRepository extends JpaRepository<Ticket, Long> {

}