import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';
import { Venue } from '../models/venue.model';
import {RequestBaseService} from "./request-base.service";
import {AuthenticationService} from "./authentication.service";

const API_URL = environment.BASE_URL + '/VENUE-SERVICE' + '/venues';

@Injectable({
  providedIn: 'root'
})
export class VenueService extends RequestBaseService {

  constructor(authenticationService: AuthenticationService, http: HttpClient) {
    super(authenticationService, http);
  }

  createVenue(venue: Venue): Observable<Venue> {
    return this.http.post<Venue>(API_URL, venue, {headers: this.getHeaders});
  }

    updateVenue(id: number | undefined, venue: Venue): Observable<Venue> {
    return this.http.put<Venue>(`${API_URL}/${id}`, venue, {headers: this.getHeaders});
  }

  deleteVenue(id: number): Observable<void> {
    return this.http.delete<void>(`${API_URL}/${id}`, {headers: this.getHeaders});
  }

  findAllVenues(): Observable<Venue[]> {
    return this.http.get<Venue[]>(API_URL, {headers: this.getHeaders});
  }

  findVenueById(id: number): Observable<Venue> {
    return this.http.get<Venue>(`${API_URL}/${id}`, {headers: this.getHeaders});
  }
}
