import { Injectable,Inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { isPlatformBrowser } from '@angular/common';
import { PLATFORM_ID } from '@angular/core';
import { Showing } from '../../shared/models/showing.model';
import { EnvService } from './env.service';

@Injectable({
  providedIn: 'root'
})
export class ShowingService {
  private apiUrl : string;

  constructor(
    private http: HttpClient,
    private envService : EnvService,
    @Inject(PLATFORM_ID) private platformId: Object
  ) {
    this.apiUrl=this.envService.apiUrl;
  }

  addShowing(showing:Showing):Observable<any>{
    return this.http.post(`${this.apiUrl}/api/showings/save`,showing);
  }

  getShowings():Observable<any>{
    return this.http.get(`${this.apiUrl}/api/showings`)
  }

  getMovieShowings(title:string):Observable<any>{
    return this.http.get(`${this.apiUrl}/api/movies/${title}/showings`)
  }

  deleteShowing(id: number): Observable<any> {
    return this.http.delete(`${this.apiUrl}/api/showings/${id}`);
  }

}