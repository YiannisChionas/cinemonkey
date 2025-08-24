import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ActivatedRoute, Router, RouterModule } from '@angular/router';
import { MoviesService } from '../../core/services/movies.service';
import { Showing } from '../../shared/models/showing.model';
import { ShowingService } from '../../core/services/showing.service';
import { Movie } from '../../shared/models/movie.model';
import { ReservationService } from '../../core/services/reservation.service';


@Component({
  selector: 'app-movie-detail',
  standalone: true,
  imports: [CommonModule, RouterModule],  // Import RouterModule here
  templateUrl: './movie-detail.component.html',
  styleUrls: ['./movie-detail.component.css']
})
export class MovieDetailComponent implements OnInit{
  movie: Movie = new Movie("test","test","test",1,"test","test");
  showings: any[] = [];  // Property to store the list of users
  showing: Showing = new Showing(1,"test","test",1,this.movie);
  movieTitle!: string;
  
  constructor(
    private route: ActivatedRoute,
    private movieService: MoviesService,
    private showingService: ShowingService,
    private reservationService: ReservationService,
    private router: Router
    
  ) {}
  ngOnInit(): void {
    this.route.paramMap.subscribe(params => {
      this.movieTitle = params.get('title')!;
      this.getMovieDetails();
    });
  }

  fetchMovieShowings(title:string):void{
    this.showingService.getMovieShowings(title).subscribe(
      (data) => {
        this.showings = data;
      },
      (error) => {
        console.log('Error fetching showings', error);
      }
    );
  }

  postReservation(reservedUserSub:string|null,reservedUserEmail:string|null,reservedShowing:number){
    this.reservationService.postReservation(reservedUserSub,reservedUserEmail,reservedShowing).subscribe({
      next: (res) => {
        console.log(res),
        this.fetchMovieShowings(this.movie.title);
      },
      error: (err) => console.error(err)
    });

  }

  onClick(showingId:string){
    this.postReservation(localStorage.getItem('userSub'),localStorage.getItem('username'),parseInt(showingId));
    this.router.navigate(['/successfull-reservation']);
  }

  getMovieDetails(): void {
    this.movieService.getMovieDetails(this.movieTitle).subscribe({
      next: (data) => {
        this.movie = data;
        this.fetchMovieShowings(this.movie.title)
      },
      error: (error) => {
        console.error('Error fetching movie details', error);
      }
    });
  }

}
