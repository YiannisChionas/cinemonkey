package com.hua.demo.reservation;

import com.hua.demo.reservation.Reservation;
import com.hua.demo.reservation.ReservationService; // Replace with your actual service
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

import java.io.ByteArrayInputStream;

@RestController
@RequiredArgsConstructor
@Slf4j
public class TicketController {

    private final TicketService ticketService;
    private final ReservationService reservationService; // Assumes you have a service to fetch reservations

    @GetMapping("/api/reservations/{id}/ticket")
    public ResponseEntity<InputStreamResource> downloadTicket(@PathVariable int id) {
        try {
            // Fetch the reservation by ID
            Reservation reservation = reservationService.getReservationById(id);
            if (reservation == null) {
                return ResponseEntity.notFound().build();
            }

            // Generate the ticket PDF
            ByteArrayInputStream pdfStream = ticketService.generateTicket(reservation);

            // Set headers for PDF download
            HttpHeaders headers = new HttpHeaders();
            headers.add("Content-Disposition", "inline; filename=ticket.pdf");

            // Serve the PDF as a downloadable file
            return ResponseEntity.ok()
                    .headers(headers)
                    .contentType(MediaType.APPLICATION_PDF)
                    .body(new InputStreamResource(pdfStream));

        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseEntity.status(500).body(null);
        }
    }
}
