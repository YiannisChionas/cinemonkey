package com.hua.demo.reservation;

import com.hua.demo.exceptions.ReservationNotFoundException;
import com.hua.demo.exceptions.ShowingNotFoundException;
import com.hua.demo.mail.MailController;
import com.hua.demo.showing.ShowingRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.pdfbox.io.IOUtils;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class ReservationServiceImpl implements ReservationService{

    public final ReservationRepository reservationRepository;
    public final ShowingRepository showingRepository;
    public final TicketService ticketService;
    public final MailController mailController;

    @Override
    public Reservation getReservationById(int id) {
        log.info("Fetching reservation with id: {}", id);
        return reservationRepository.findById(id).orElseThrow(() -> new ShowingNotFoundException(id));
    }

    @Override
    public List<Reservation> getAllReservations() {
        log.info("Fetching all reservations");
        return reservationRepository.findAll();
    }

    @Override
    public Reservation saveReservation(String userSub,String userEmail, int id) throws Exception {
        if (userSub.isEmpty()){
            throw new NullPointerException("userSub not provided");
        }
        if (userEmail.isEmpty()){
            throw new NullPointerException("userSub not provided");
        }
        Reservation reservation = new Reservation();
        reservation.setUserSub(userSub);
        reservation.setUserEmail(userEmail);
        reservation.setReservedShowing(showingRepository.findById(id).orElseThrow(()->new ShowingNotFoundException(id)));
        reservation.setReservationState(ReservationState.ACTIVE);
        Reservation savedReservation = saveReservation(reservation);
        byte[] pdfBytes = IOUtils.toByteArray(ticketService.generateTicket(savedReservation));
        mailController.sendTicketEmail(userEmail, pdfBytes);
        return savedReservation;
    }

    @Override
    public Reservation saveReservation(Reservation reservation) {
        log.info("Saving new reservation with id '{}' to the database", reservation.getId());
        return reservationRepository.save(reservation);
    }

    @Override
    public Reservation editReservation(Reservation reservation) {
        //TODO
        return null;
    }

    @Override
    public void deleteReservation(int id) {
        log.info("Deleting reservation with id  '{}' from the database", id);
        reservationRepository.findById(id).orElseThrow(()-> new ReservationNotFoundException(id));
        reservationRepository.deleteById(id);
    }

    @Override
    public Reservation cancelReservation(int id) {
        log.info("Cancelling reservation with id '{}'",id);
        Reservation reservation = reservationRepository.findById(id).orElseThrow(()-> new ReservationNotFoundException(id));
        reservation.setReservationState(ReservationState.CANCELLED);
        return null;
    }

    @Override
    public Reservation requestCancelation(int id) {
        log.info("Cancelling reservation with id '{}'",id);
        Reservation reservation = reservationRepository.findById(id).orElseThrow(()-> new ReservationNotFoundException(id));
        reservation.setReservationState(ReservationState.CANCELATION_REQUESTED);
        return null;
    }
}
