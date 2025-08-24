package com.hua.demo.movie;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.hua.demo.showing.Showing;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

import java.util.ArrayList;
import java.util.Collection;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Represents a movie")
public class Movie {
    public Movie(String title, String poster, String director, int duration, String description, String genre) {
        this.title = title;
        this.poster = poster;
        this.director = director;
        this.duration = duration;
        this.description = description;
        this.genre = genre;
        this.showings = new ArrayList<>();
    }
    @Id
    @Schema(description = "Movie's title, used as unique identifier for the movie", example = "AVATAR")
    private String title;
    @Schema(description = "Url for the poster of the movie, served by tomcat", example = "http://localhost:8080/posters/BANANAWARS.webp")
    private String poster;
    @Schema(description = "Movie's director", example = "James Cameron")
    private String director;
    @Schema(description = "Movie's duration", example = "162")
    private int duration;
    @Column(columnDefinition = "TEXT")
    @Schema(description = "Movie's description", example = "A paraplegic Marine dispatched to the moon Pandora on a unique mission becomes torn between following his orders and protecting the world he feels is his home.\\n\\nWhen his brother is killed in a robbery, paraplegic Marine Jake Sully decides to take his place in a mission on the distant world of Pandora.")
    private String description;
    @Schema(description = "Movie's genre", example = "Sci-Fi, Adventure, Action, Fantasy")
    private String genre;
    @JsonIgnore
    @Cascade(CascadeType.ALL)
    @OneToMany(mappedBy = "showingMovie")
    private Collection<Showing> showings = new ArrayList<>();

}
