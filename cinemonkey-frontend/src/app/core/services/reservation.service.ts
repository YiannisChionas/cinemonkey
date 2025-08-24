import { Inject, Injectable, PLATFORM_ID } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Reservation } from '../../shared/models/reservation.model';
import { EnvService } from './env.service';

@Injectable({
  providedIn: 'root'
})
export class ReservationService {
  private apiUrl : string;

  constructor(
    private http: HttpClient,
    private envService : EnvService,
    @Inject(PLATFORM_ID) private platformId: Object // Inject PLATFORM_ID to check environment
    ) {
      this.apiUrl=this.envService.apiUrl+"/api";
    }

    getReservations():Observable<any>{
      return this.http.get(`${this.apiUrl}/reservations`)
    }

    cancelReservation(id:number|null):Observable<any>{
      return this.http.patch(`${this.apiUrl}/reservations/cancel/${id}`,null)
    }

    requestCancelation(id:number|null):Observable<any>{
      return this.http.patch(`${this.apiUrl}/reservations/requestCancelation/${id}`,null)
    }

    deleteReservation(id:number|null):Observable<any>{
      return this.http.delete(`${this.apiUrl}/reservations/${id}`)
    }

    postReservation(reservedUserSub:string|null,reservedUserEmail:string|null,reservedShowing:number):Observable<any>{
      const reservation = new Reservation(null,null,null)
      return this.http.post(`${this.apiUrl}/reservations/save/${reservedUserSub}-${reservedUserEmail}-${reservedShowing}`,reservation);
    }

    getUserReservations(email:string):Observable<any>{
      return this.http.get(`${this.apiUrl}/users/${email}/reservations`)
    }

}
