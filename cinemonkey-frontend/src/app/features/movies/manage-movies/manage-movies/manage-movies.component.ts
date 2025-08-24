import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { Movie } from '../../../../shared/models/movie.model';
import { MoviesService } from '../../../../core/services/movies.service';

@Component({
  selector: 'app-manage-movies',
  standalone: true,
  imports: [CommonModule,ReactiveFormsModule],
  templateUrl: './manage-movies.component.html',
  styleUrl: './manage-movies.component.css'
})
export class ManageMoviesComponent {
  movies: any[] = [];  // Property to store the list of movies
  movieForm!: FormGroup;
  movie: Movie = new Movie("test","test","test",1,"test","test");

  constructor(private movieService: MoviesService,private fb: FormBuilder) {}

  ngOnInit():void{
    this.fetchMovies();
    this.movieForm = this.fb.group({
      titleInput: ['', [Validators.required]],
      posterInput: ['', Validators.required],
      directorInput: ['', Validators.required],
      durationInput: ['', Validators.required],
    });
  }

  fetchMovies():void{
    this.movieService.getMovies().subscribe(
      (data) => {
        this.movies = data;
      },
      (error) => {
        console.log('Error fetching users', error);
      }
    );
  }

  onSubmit(): void {
    if (this.movieForm.valid) {
      const formData = this.movieForm.value;
      this.movie = formData;
      console.log(this.movie);
      this.movieService.addMovie(this.movie).subscribe({
        next: (res) => {
          console.log(res),
          this.fetchMovies();
        },
        error: (err) => console.error(err)
      })
    }
  }

  deleteMovie(title:string):void{
    this.movieService.deleteMovie(title).subscribe({
      next: (res) => {
        console.log('Movie deleted:', res),
        this.fetchMovies();
      },
      error: (err) => console.error(err)
    })
  }

}

