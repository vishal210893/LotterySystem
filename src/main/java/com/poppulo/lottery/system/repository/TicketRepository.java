package com.poppulo.lottery.system.repository;

import com.poppulo.lottery.system.entity.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TicketRepository extends JpaRepository<Ticket, Long> {

}