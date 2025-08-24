import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';  // To get route parameters
import { MoviesService } from '../../core/services/movies.service';

@Component({
  selector: 'app-booking',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './booking.component.html',
  styleUrls: ['./booking.component.css']
})
export class BookingComponent implements OnInit {
  movieTitle!: string;
  movie: any;
  constructor(
    private route: ActivatedRoute,
    private movieService: MoviesService
  ) {}
  ngOnInit(): void {
    this.route.paramMap.subscribe(params => {
      this.movieTitle = params.get('title')!;
      this.getMovieDetails();
    });
  }

  getMovieDetails(): void {
    this.movieService.getMovieDetails(this.movieTitle).subscribe({
      next: (data) => {
        this.movie = data;
      },
      error: (error) => {
        console.error('Error fetching movie details', error);
      }
    });
  }

}
