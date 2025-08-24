package com.hua.demo.showing;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.hua.demo.movie.Movie;
import com.hua.demo.reservation.Reservation;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

import java.util.ArrayList;
import java.util.Collection;

@Entity(name="showing")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Represents a showing for a movie")
public class Showing {
    public Showing(int id,State state,String showingDate,int showingRoom){
        this.id = id;
        this.state = state;
        this.showingDate = showingDate;
        this.showingRoom = showingRoom;
    }
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "Showing's id, used as unique identifier for the showing", example = "1")
    private int id;
    @Enumerated(EnumType.STRING)
    @Schema(description = "Showing's state, used to show showings current state", example = "SCHEDULED")
    private State state;
    @Schema(description = "Showing's date", example = "03/16/2023 20:30:00")
    private String showingDate;
    @Schema(description = "Showing's room in the cinema", example = "1")
    private int showingRoom;
    @ManyToOne
    @JoinColumn(name = "fk_movie")
    private Movie showingMovie;
    @JsonIgnore
    @Cascade(CascadeType.ALL)
    @OneToMany(mappedBy = "reservedShowing")
    private Collection<Reservation> reservations = new ArrayList<>();

}
