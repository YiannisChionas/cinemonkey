package com.hua.demo.reservation;

import java.util.List;

public interface ReservationService {

    Reservation getReservationById(int id);
    List<Reservation> getAllReservations();
    Reservation saveReservation(String userSub,String userEmail,int id) throws Exception;
    Reservation saveReservation(Reservation reservation);
    Reservation editReservation(Reservation reservation);
    Reservation cancelReservation(int id);
    Reservation requestCancelation(int id);
    void deleteReservation(int id);
}

