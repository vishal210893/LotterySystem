package com.poppula.poppula_test.service;

import com.poppula.poppula_test.entity.Line;
import com.poppula.poppula_test.entity.Ticket;
import com.poppula.poppula_test.repository.TicketRepository;
import com.poppula.poppula_test.util.PoppuloUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class LotteryService {


    private static final String TICKET_NOT_FOUND = "Ticket not found";

    private static final Random rand = new Random();

    private final TicketRepository ticketRepository;

    public Ticket generateTicket(int n) {
        Ticket ticket = new Ticket();
        List<Line> lines = new ArrayList<>();

        for (int i = 0; i < n; i++) {
            List<Integer> numbers = Arrays.asList(rand.nextInt(3), rand.nextInt(3), rand.nextInt(3));
            Line line = new Line();
            line.setNumbers(numbers);
            line.setResult(PoppuloUtil.calculateResult(numbers));
            lines.add(line);
        }

        ticket.setLines(lines);
        return ticketRepository.save(ticket);
    }


    public List<Ticket> findAllTicket() {
        final List<Ticket> ticketList = ticketRepository.findAll();
        return ticketList.stream().map(ticket -> sortTicket(ticket)).toList();
    }

    private Ticket sortTicket(Ticket ticket) {
        final List<Line> lines = ticket.getLines();
        lines.sort((o1, o2) -> {
            int val;
            if (o1.getResult() == o2.getResult()) {
                val = 0;
            } else {
                val = o1.getResult() > o2.getResult() ? 1 : -1;
            }
            return val;
        });
        return ticket;
    }


    public Ticket getTicket(Long id) {
        final Ticket ticket = ticketRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException(TICKET_NOT_FOUND));
        return sortTicket(ticket);
    }

    public Ticket amendTicket(Long id, int n) {

        Ticket ticket = ticketRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException(TICKET_NOT_FOUND));

        if (ticket.isStatusChecked()) {
            throw new IllegalStateException("Ticket status already checked");
        }

        List<Line> lines = ticket.getLines();
        for (int i = 0; i < n; i++) {
            List<Integer> numbers = Arrays.asList(rand.nextInt(3), rand.nextInt(3), rand.nextInt(3));
            Line line = new Line();
            line.setNumbers(numbers);
            line.setResult(PoppuloUtil.calculateResult(numbers));
            lines.add(line);
        }

        ticket.setLines(lines);
        return ticketRepository.save(ticket);
    }

    public boolean checkTicketStatus(Long id) {
        Ticket ticket = ticketRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException(TICKET_NOT_FOUND));

        if (ticket.isStatusChecked()) {
            return ticket.isStatusChecked();
        }

        ticket.setStatusChecked(true);
        ticket.getLines().sort(Comparator.comparingInt(Line::getResult).reversed());
        ticketRepository.save(ticket);
        return !ticket.isStatusChecked();
    }

}