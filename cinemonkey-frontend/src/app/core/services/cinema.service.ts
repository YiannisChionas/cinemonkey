import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { EnvService } from './env.service';

@Injectable({
  providedIn: 'root'
})
export class CinemaService {
  private apiUrl:string;

  constructor(
    private http: HttpClient,
    private envService : EnvService
  ) {
    this.apiUrl=this.envService.apiUrl;
  }

  getMovies(): Observable<any> {
    return this.http.get(`${this.apiUrl}/movies`);
  }

  getMovieById(id: string): Observable<any> {
    return this.http.get(`${this.apiUrl}/movies/${id}`);
  }

  bookTicket(movieId: string, bookingDetails: any): Observable<any> {
    return this.http.post(`${this.apiUrl}/movies/${movieId}/book`, bookingDetails);
  }
}
