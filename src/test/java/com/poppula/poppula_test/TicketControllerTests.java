package com.poppula.poppula_test;

import com.poppula.poppula_test.controller.TicketController;
import com.poppula.poppula_test.entity.Ticket;
import com.poppula.poppula_test.service.LotteryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.Mockito.*;

@WebMvcTest(TicketController.class)
public class TicketControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @Mock
    private LotteryService lotteryService;

    @InjectMocks
    private TicketController ticketController;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(ticketController).build();
    }

    @Test
    public void testCreateTicket() throws Exception {
        Ticket ticket = new Ticket();
        ticket.setId(1L);
        when(lotteryService.generateTicket(anyInt())).thenReturn(ticket);

        mockMvc.perform(MockMvcRequestBuilders.post("/ticket")
                        .param("noOfLines", "5"))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.header().string("Location", "/ticket/1"));
    }

    @Test
    public void testGetTicket() throws Exception {
        Ticket ticket = new Ticket();
        ticket.setId(1L);
        when(lotteryService.getTicket(1L)).thenReturn(ticket);

        mockMvc.perform(MockMvcRequestBuilders.get("/ticket/1"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1));
    }

    @Test
    public void testAmendTicket_ShouldReturnOkStatus() throws Exception {
        Ticket ticket = new Ticket();
        ticket.setId(1L);
        when(lotteryService.amendTicket(anyLong(), anyInt())).thenReturn(ticket);

        mockMvc.perform(MockMvcRequestBuilders.post("/ticket/1")
                        .param("noOfLines", "3"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1));
    }

    @Test
    public void testCheckTicketStatus() throws Exception {

        doNothing().when(lotteryService).checkTicketStatus(1L);
        mockMvc.perform(MockMvcRequestBuilders.post("/ticket/status/1"))
                .andExpect(MockMvcResultMatchers.status().isNoContent());
    }

}
