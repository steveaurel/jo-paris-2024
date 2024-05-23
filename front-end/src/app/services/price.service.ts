import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';
import { Price } from '../models/price.model';
import {RequestBaseService} from "./request-base.service";
import {AuthenticationService} from "./authentication.service";

const API_URL = environment.BASE_URL + '/EVENT-SERVICE' + '/prices';

@Injectable({
  providedIn: 'root'
})
export class PriceService extends RequestBaseService {

  constructor(authenticationService: AuthenticationService, http: HttpClient) {
    super(authenticationService, http);
  }

  createPrice(price: Price): Observable<Price> {
    return this.http.post<Price>(API_URL, price, {headers: this.getHeaders});
  }

  findPriceById(id: number): Observable<Price> {
    return this.http.get<Price>(`${API_URL}/${id}`, {headers: this.getHeaders});
  }

  findAllPrices(): Observable<Price[]> {
    return this.http.get<Price[]>(API_URL, {headers: this.getHeaders});
  }

  updatePrice(id: number, price: Price): Observable<Price> {
    return this.http.put<Price>(`${API_URL}/${id}`, price, {headers: this.getHeaders});
  }

  deletePrice(id: number): Observable<void> {
    return this.http.delete<void>(`${API_URL}/${id}`, {headers: this.getHeaders});
  }

  findPricesByEventId(eventId: number): Observable<Price[]> {
    return this.http.get<Price[]>(`${API_URL}/by-event/${eventId}`, {headers: this.getHeaders});
  }
}
