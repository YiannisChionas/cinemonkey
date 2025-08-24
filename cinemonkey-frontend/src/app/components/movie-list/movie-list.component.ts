import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { MoviesService } from '../../core/services/movies.service';
import {MatIconModule} from '@angular/material/icon'
import { MatFormFieldModule } from '@angular/material/form-field';

@Component({
  selector: 'app-movie-list',
  standalone: true,
  imports: [CommonModule, RouterModule,MatFormFieldModule,MatIconModule],
  templateUrl: './movie-list.component.html',
  styleUrls: ['./movie-list.component.css']
})
export class MovieListComponent {
  movies: any[] = [];  // Property to store the list of movies

  constructor(private movieService: MoviesService) {}

  ngOnInit(): void {
  console.log('[MovieListComponent] ngOnInit fired');
  this.fetchMovies();
}

fetchMovies(): void {
  console.log('[MovieListComponent] Calling movieService.getMovies()');

  this.movieService.getMovies().subscribe({
    next: (data) => {
      console.log('[MovieListComponent] Movies loaded:', data);
      this.movies = data;
    },
    error: (err) => {
      console.error('[MovieListComponent] Error:', err);
    }
  });
}

  /*movies = [
    {
      id: 1,
      title: 'Monkey Bussiness',
      imageUrl: 'MB.webp',
      description: 'Description of Movie 1'
    },
    {
      id: 2,
      title: 'Gorilla Of Wall Street',
      imageUrl: 'GOFWS.png',
      description: 'Description of Movie 2'
    },
    {
      id: 3,
      title: 'Dancing With Bonobos',
      imageUrl: 'DWB.webp',
      description: 'Description of Movie 2'
    }
    // Add more movie objects
  ]; // Define the movies array
*/
}
