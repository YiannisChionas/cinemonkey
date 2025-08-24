import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
import { tap } from 'rxjs/operators';
import { Movie } from '../../shared/models/movie.model';
import { EnvService } from './env.service';

@Injectable({
  providedIn: 'root'
})
export class MoviesService {
  private moviesUrl:string;
  constructor(
    private http: HttpClient,
    private envService : EnvService
  ) {
    this.moviesUrl=this.envService.apiUrl+"/api/movies";
  }

  getMovieDetails(title: string): Observable<any> {
    return this.http.get<any>(`${this.moviesUrl}/${title}`);
  }

  getMovies(): Observable<any> {
    console.log('[MoviesService] Sending GET request to', this.moviesUrl);
    return this.http.get(this.moviesUrl);
  }

  addMovie(movie:Movie):Observable<any>{
    return this.http.post(`${this.moviesUrl}/save`,movie);
  }

  deleteMovie(title:string):Observable<any>{
    return this.http.delete(`${this.moviesUrl}/${title}`)
  }

}
