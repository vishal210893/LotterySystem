package com.poppula.lottery.system.repository;

import com.poppula.lottery.system.entity.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TicketRepository extends JpaRepository<Ticket, Long> {

}