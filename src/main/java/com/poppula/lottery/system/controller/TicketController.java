package com.poppula.lottery.system.controller;

import com.poppula.lottery.system.entity.Ticket;
import com.poppula.lottery.system.service.LotteryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Ticket", description = "Ticket management APIs")
public class TicketController {

    @Autowired
    private LotteryService lotteryService;

    @Operation(summary = "Create a new ticket", description = "Creates a new ticket with the specified number of lines")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Ticket created successfully",
                    content = @Content(mediaType = "text/plain", schema = @Schema(implementation = String.class)))
    })
    @PostMapping(produces = MediaType.TEXT_PLAIN_VALUE)
    public ResponseEntity<String> createTicket(
            @Parameter(description = "Number of lines for the ticket") @RequestParam int noOfLines) throws URISyntaxException {
        Ticket ticket = lotteryService.generateTicket(noOfLines);
        URI location = new URI("/ticket/" + ticket.getId());
        return ResponseEntity.created(location).body("Ticket created with id " + ticket.getId());
    }

    @Operation(summary = "Get all tickets", description = "Retrieves a list of all tickets")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved list of tickets",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Ticket.class))),
            @ApiResponse(responseCode = "404", description = "No tickets found")
    })
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

    @Operation(summary = "Get a ticket by ID", description = "Retrieves a ticket by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved the ticket",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Ticket.class))),
            @ApiResponse(responseCode = "404", description = "Ticket not found")
    })
    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Ticket> getTicket(
            @Parameter(description = "ID of the ticket to be retrieved") @PathVariable Long id) {
        try {
            Ticket ticket = lotteryService.getTicket(id);
            return ResponseEntity.ok(ticket);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Amend a ticket", description = "Amends an existing ticket with a new number of lines")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Ticket successfully updated",
                    content = @Content(mediaType = "text/plain", schema = @Schema(implementation = String.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input")
    })
    @PutMapping(value = "/{id}", produces = MediaType.TEXT_PLAIN_VALUE)
    public ResponseEntity<String> amendTicket(
            @Parameter(description = "ID of the ticket to be amended") @PathVariable Long id,
            @Parameter(description = "New number of lines for the ticket") @RequestParam int noOfLines) {
        try {
            Ticket ticket = lotteryService.amendTicket(id, noOfLines);
            return ResponseEntity.ok().body("Ticket updated for id " + ticket.getId());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @Operation(summary = "Check ticket status", description = "Checks the status of a ticket by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved ticket status",
                    content = @Content(mediaType = "text/plain", schema = @Schema(implementation = String.class))),
            @ApiResponse(responseCode = "404", description = "Ticket not found")
    })
    @PutMapping(value = "/status/{id}", produces = MediaType.TEXT_PLAIN_VALUE)
    public ResponseEntity<String> checkTicketStatus(
            @Parameter(description = "ID of the ticket to check status") @PathVariable Long id) {
        try {
            final boolean status = lotteryService.checkTicketStatus(id);
            return ResponseEntity.ok().body("Status = " + status);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return ResponseEntity.notFound().build();
        }
    }
}