package com.hua.demo.reservation;

import com.hua.demo.showing.Showing;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Entity(name="reservation")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Represents a person's reservation for the showing of a movie")
public class Reservation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "Reservation's id, used as unique identifier for the reservation", example = "1")
    private int Id;
    @ManyToOne
    @JoinColumn(name = "fk_showing")
    private Showing reservedShowing;
    private String userSub; // Από το token (sub) του Keycloak
    private String userEmail;
    @Enumerated(EnumType.STRING)
    @Schema(description = "Reservation's state")
    private ReservationState reservationState;
}
