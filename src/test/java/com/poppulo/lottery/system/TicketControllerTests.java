package com.poppulo.lottery.system;

import com.poppulo.lottery.system.controller.TicketController;
import com.poppulo.lottery.system.entity.Ticket;
import com.poppulo.lottery.system.service.LotteryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TicketController.class)
class TicketControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private LotteryService lotteryService;

    @InjectMocks
    private TicketController ticketController;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(ticketController).build();
    }

    @Test
    void createTicket_ShouldReturnCreatedStatus() throws Exception {
        Ticket ticket = new Ticket();
        ticket.setId(1L);
        when(lotteryService.generateTicket(anyInt())).thenReturn(ticket);

        mockMvc.perform(post("/ticket")
                        .param("noOfLines", "5")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(MockMvcResultMatchers.header().string("Location", "/ticket/1"))
                .andExpect(MockMvcResultMatchers.content().string("Ticket created with id 1"));
    }

    @Test
    void getAllTicket_ShouldReturnOkStatus() throws Exception {
        Ticket ticket1 = new Ticket();
        Ticket ticket2 = new Ticket();
        List<Ticket> tickets = Arrays.asList(ticket1, ticket2);
        when(lotteryService.findAllTicket()).thenReturn(tickets);

        mockMvc.perform(get("/ticket")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.length()").value(2));
    }

    @Test
    void getAllTicket_ShouldReturnNotFoundOnException() throws Exception {
        when(lotteryService.findAllTicket()).thenThrow(new RuntimeException("Error"));

        mockMvc.perform(get("/ticket")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void getTicket_ShouldReturnOkStatus() throws Exception {
        Ticket ticket = new Ticket();
        ticket.setId(1L);
        when(lotteryService.getTicket(anyLong())).thenReturn(ticket);

        mockMvc.perform(get("/ticket/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1));
    }

    @Test
    void getTicket_ShouldReturnNotFoundOnException() throws Exception {
        when(lotteryService.getTicket(anyLong())).thenThrow(new RuntimeException("Error"));

        mockMvc.perform(get("/ticket/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void amendTicket_ShouldReturnOkStatus() throws Exception {
        Ticket ticket = new Ticket();
        ticket.setId(1L);
        when(lotteryService.amendTicket(anyLong(), anyInt())).thenReturn(ticket);

        mockMvc.perform(put("/ticket/1")
                        .param("noOfLines", "3")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("Ticket updated for id 1"));
    }

    @Test
    void amendTicket_ShouldReturnBadRequestOnException() throws Exception {
        when(lotteryService.amendTicket(anyLong(), anyInt())).thenThrow(new RuntimeException("Error"));

        mockMvc.perform(put("/ticket/1")
                        .param("noOfLines", "3")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(MockMvcResultMatchers.content().string("Error"));
    }

    @Test
    void checkTicketStatus_ShouldReturnOkStatus() throws Exception {
        when(lotteryService.checkTicketStatus(anyLong())).thenReturn(true);

        mockMvc.perform(put("/ticket/status/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("Status = true"));
    }

    @Test
    void checkTicketStatus_ShouldReturnNotFoundOnException() throws Exception {
        when(lotteryService.checkTicketStatus(anyLong())).thenThrow(new RuntimeException("Error"));

        mockMvc.perform(put("/ticket/status/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

}