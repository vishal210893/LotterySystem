package com.poppula.poppula_test.controller;

import com.poppula.poppula_test.entity.Ticket;
import com.poppula.poppula_test.service.LotteryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

@RestController
@RequestMapping("/ticket")
@Slf4j
public class TicketController {

    @Autowired
    private LotteryService lotteryService;

    @PostMapping(produces = MediaType.TEXT_PLAIN_VALUE)
    public ResponseEntity<String> createTicket(@RequestParam int noOfLines) throws URISyntaxException {
        Ticket ticket = lotteryService.generateTicket(noOfLines);
        URI location = new URI("/ticket/" + ticket.getId());
        return ResponseEntity.created(location).body("Ticket created with id " + ticket.getId());
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Ticket>> getAllTicket() {
        try {
            List<Ticket> ticket = lotteryService.findAllTicket();
            return ResponseEntity.ok().body(ticket);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Ticket> getTicket(@PathVariable Long id) {
        try {
            Ticket ticket = lotteryService.getTicket(id);
            return ResponseEntity.ok(ticket);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping(value = "/{id}", produces = MediaType.TEXT_PLAIN_VALUE)
    public ResponseEntity<String> amendTicket(@PathVariable Long id, @RequestParam int noOfLines) {
        try {
            Ticket ticket = lotteryService.amendTicket(id, noOfLines);
            return ResponseEntity.ok().body("Ticket updated for id " + ticket.getId());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping(value = "/status/{id}", produces = MediaType.TEXT_PLAIN_VALUE)
    public ResponseEntity<String> checkTicketStatus(@PathVariable Long id) {
        try {
            final boolean status = lotteryService.checkTicketStatus(id);
            return ResponseEntity.ok().body("Status = " + status);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return ResponseEntity.notFound().build();
        }
    }

}