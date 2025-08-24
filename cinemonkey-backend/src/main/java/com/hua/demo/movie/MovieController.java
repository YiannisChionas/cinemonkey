package com.hua.demo.movie;

import com.hua.demo.exceptions.MovieNotFoundException;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.HashMap;
import java.util.List;

import com.hua.demo.mail.MailController;

@RestController
@RequestMapping("/api/movies")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Movie Management", description = "API to manage movies")
public class MovieController {
    public final MovieService movieService;
    public final MailController mailController;

    @Operation(summary = "Retrieves all movies", description = "Retrieves a list of all movies available in the database")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved list"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "404", description = "No movies found")
    })
    @GetMapping
    public ResponseEntity<List<Movie>> getAllMovies() {
        return ResponseEntity.ok().body(movieService.getAllMovies());
    }

    @Operation(summary = "Retrieves movie with specific title")
    @ApiResponse(
            responseCode = "200",
            description = "Retrieve movie with specific title",
            content = @Content(
                    mediaType = "application/json",
                    examples = @ExampleObject(
                            value = "{\n" +
                                    "  \"title\": \"AVATAR\",\n" +
                                    "  \"poster\": \"http://localhost:8080/posters/BANANAWARS.webp\",\n" +
                                    "  \"director\": \"James Cameron\",\n" +
                                    "  \"duration\": 162,\n" +
                                    "  \"description\": \"A paraplegic Marine dispatched to the moon Pandora on a unique mission becomes torn between following his orders and protecting the world he feels is his home. When his brother is killed in a robbery, paraplegic Marine Jake Sully decides to take his place in a mission on the distant world of Pandora.\",\n" +
                                    "  \"genre\": \"Sci-Fi, Adventure, Action, Fantasy\"\n" +
                                    "}"
                    )
            )
    )
    @GetMapping("/{title}")
    public Object getMovieByTitle(@PathVariable("title") String title) {
        try {
            return ResponseEntity.ok().body(movieService.getMovieByTitle(title));
        } catch (MovieNotFoundException e) {
            log.error("Could not find Movie with title '{}' in the database", title);
            HashMap<String, String> error = new HashMap<>();
            error.put("error", "Movie not found");
            return new ResponseEntity<>(error, null, 404);
        }
    }

    @Operation(summary = "Retrieves showings for movie with specific title", description = "Retrieves showings for movie with specific title")
    @ApiResponse(
            responseCode = "200",
            description = "Retrieves showings for movie with specific title",
            content = @Content(
                    mediaType = "application/json",
                    examples = @ExampleObject(
                            value = "[\n" +
                                    "  {\n" +
                                    "    \"id\": 7,\n" +
                                    "    \"state\": \"SCHEDULED\",\n" +
                                    "    \"showingDate\": \"04/16/2023 20:30:00\",\n" +
                                    "    \"showingRoom\": 1,\n" +
                                    "    \"showingMovie\": {\n" +
                                    "      \"title\": \"AVATAR\",\n" +
                                    "      \"poster\": \"http://localhost:8080/posters/BANANAWARS.webp\",\n" +
                                    "      \"director\": \"James Cameron\",\n" +
                                    "      \"duration\": 162,\n" +
                                    "      \"description\": \"A paraplegic Marine dispatched to the moon Pandora on a unique mission becomes torn between following his orders and protecting the world he feels is his home.\\n\\nWhen his brother is killed in a robbery, paraplegic Marine Jake Sully decides to take his place in a mission on the distant world of Pandora.\",\n" +
                                    "      \"genre\": \"Sci-Fi, Adventure, Action, Fantasy\"\n" +
                                    "    }\n" +
                                    "  }\n" +
                                    "]"

                    )
            )
    )
    @GetMapping("{title}/showings")
    public Object getMovieShowings(@PathVariable("title") String title) {
        try {
            return ResponseEntity.ok().body(movieService.getMovieShowings(title));
        } catch (MovieNotFoundException e) {
            log.error("Could not find movie with title '{}' in the database", title);
            HashMap<String, String> error = new HashMap<>();
            error.put("error", "Movie not found");
            return new ResponseEntity<>(error, null, 404);
        }
    }

    @PostMapping("/save")
    public ResponseEntity<Movie> saveMovie (@RequestBody Movie movie){
        URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/movies/save").toUriString());
        return ResponseEntity.created(uri).body(movieService.saveMovie(movie));
    }

    @DeleteMapping("/{title}")
    public ResponseEntity<?>deleteMovie(@PathVariable("title") String title){
        try {
            mailController.sendTestEmail();
            movieService.deleteMovie(title);
            return ResponseEntity.ok().build();
        } catch (MovieNotFoundException e) {
            log.error("Could not find movie with title '{}' in the database", title);
            HashMap<String, String> error = new HashMap<>();
            error.put("error", "Movie not found");
            return new ResponseEntity<>(error, null, 404);
        }
    }

}
