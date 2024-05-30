import { Injectable } from '@angular/core';
import {HttpClient, HttpParams} from '@angular/common/http';
import {Observable, throwError} from 'rxjs';
import { environment } from '../../environments/environment';
import {RequestBaseService} from "./request-base.service";
import { Events } from '../models/events.model';
import {AuthenticationService} from "./authentication.service";
import {catchError} from "rxjs/operators";

const API_URL = environment.BASE_URL + '/EVENT-SERVICE' + '/events';

@Injectable({
  providedIn: 'root'
})
export class EventService extends RequestBaseService {

  constructor(authenticationService: AuthenticationService, http: HttpClient) {
    super(authenticationService, http);
  }

  createEvent(event: Events): Observable<Events> {
    return this.http.post<Events>(API_URL, event, {headers: this.getHeaders});
  }

  getAllEvents(): Observable<Events[]> {
    return this.http.get<Events[]>(API_URL, {headers: this.getHeaders});
  }

  getEventById(id: number): Observable<Events> {
    return this.http.get<Events>(`${API_URL}/${id}`);
  }

  updateEvent(id: number | undefined, event: Events): Observable<Events> {
    return this.http.put<Events>(`${API_URL}/${id}`, event, {headers: this.getHeaders});
  }

  deleteEvent(id: number): Observable<void> {
    return this.http.delete<void>(`${API_URL}/${id}`, {headers: this.getHeaders});
  }

  findEventsByVenueID(venueID: number): Observable<Events[]> {
    return this.http.get<Events[]>(`${API_URL}/by-venue/${venueID}`, {headers: this.getHeaders});
  }

  updateEventSeats(id: number, seats: number): Observable<Events> {
    return this.http.patch<Events>(`${API_URL}/${id}/update-seats`, { seats }, {headers: this.getHeaders});
  }

  findAllActiveEvents(): Observable<Events[]> {
    return this.http.get<Events[]>(`${API_URL}/active`);
  }

  validateEventStatus(id: number | undefined): Observable<boolean> {
    return this.http.get<boolean>(`${API_URL}/validate/${id}`, {headers: this.getHeaders});
  }
  checkEventAvailability(venueId: number, startTime: string, endTime: string, date: string): Observable<boolean> {
    let params = new HttpParams();

    if (venueId > 0) {
      params = params.set('venueID', venueId.toString());
    }

    if (startTime) {
      params = params.set('startTime', startTime);
    }

    if (endTime) {
      params = params.set('endTime', endTime);
    }

    if (date) {
      params = params.set('date', date);
    }

    return this.http.get<boolean>(`${API_URL}/check-availability`, { params, headers: this.getHeaders })
      .pipe(
        catchError((error) => {
          console.error('Error checking event availability:', error);
          return throwError(() => new Error('Error checking event availability'));
        })
      );
  }


}
