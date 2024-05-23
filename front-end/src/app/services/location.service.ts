import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';
import { Location } from '../models/location.model';
import {RequestBaseService} from "./request-base.service";
import {AuthenticationService} from "./authentication.service";

const API_URL = environment.BASE_URL + '/VENUE-SERVICE' + '/locations';

@Injectable({
  providedIn: 'root'
})
export class LocationService extends RequestBaseService{

  constructor(authenticationService: AuthenticationService, http: HttpClient) {
    super(authenticationService, http);
  }

  createLocation(location: Location): Observable<Location> {
    return this.http.post<Location>(API_URL, location, {headers: this.getHeaders});
  }

  updateLocation(id: number, location: Location): Observable<Location> {
    return this.http.put<Location>(`${API_URL}/${id}`, location, {headers: this.getHeaders});
  }

  deleteLocation(id: number): Observable<void> {
    return this.http.delete<void>(`${API_URL}/${id}`, {headers: this.getHeaders});
  }

  getLocationById(id: number): Observable<Location> {
    return this.http.get<Location>(`${API_URL}/${id}`, {headers: this.getHeaders});
  }

  getAllLocations(): Observable<Location[]> {
    return this.http.get<Location[]>(API_URL, {headers: this.getHeaders});
  }
}
