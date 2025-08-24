import { Routes } from '@angular/router';
import { MovieListComponent } from './components/movie-list/movie-list.component';
import { ManageShowingsComponent } from './features/showings/manage-showings/manage-showings.component';
import { MovieDetailComponent } from './components/movie-detail/movie-detail.component';
import { BookingComponent } from './components/booking/booking.component';
import { LoginComponent } from './features/authorization/login/login.component';
import { ManageReservationsComponent } from './features/reservations/manage-reservations/manage-reservations.component';
import { ManageMoviesComponent } from './features/movies/manage-movies/manage-movies/manage-movies.component';
import { UnauthorisedComponent } from './features/authorization/unauthorised/unauthorised.component';
import { SuccessfullReservationComponent } from './features/reservations/successfull-reservation/successfull-reservation.component';

export const routes: Routes = [
  {
  path: '',
  loadComponent: () => import('./components/movie-list/movie-list.component').then(m => m.MovieListComponent)
}
,
  { path: 'movie/:title', component: MovieDetailComponent},
  { path: 'booking/:id', component: BookingComponent},
  { path: 'manage-showings', component: ManageShowingsComponent},
  { path: 'manage-reservations', component: ManageReservationsComponent},
  { path: 'manage-movies', component: ManageMoviesComponent},
  { path: 'successfull-reservation',component: SuccessfullReservationComponent},
  { path: 'unauthorised', component: UnauthorisedComponent },
  { path: 'login', component: LoginComponent }
];
