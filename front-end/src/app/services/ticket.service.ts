import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';
import { Ticket } from '../models/ticket.model';
import {RequestBaseService} from "./request-base.service";
import {AuthenticationService} from "./authentication.service";

const API_URL = environment.BASE_URL + '/TICKET-SERVICE' + '/tickets';

@Injectable({
  providedIn: 'root'
})
export class TicketService extends RequestBaseService {

  constructor(authenticationService: AuthenticationService, http: HttpClient) {
    super(authenticationService, http);
  }

  createTicket(ticket: Ticket): Observable<Ticket> {
    return this.http.post<Ticket>(API_URL, ticket, {headers: this.getHeaders});
  }

  findTicketById(id: number): Observable<Ticket> {
    return this.http.get<Ticket>(`${API_URL}/${id}`, {headers: this.getHeaders});
  }

  findAllTickets(): Observable<Ticket[]> {
    return this.http.get<Ticket[]>(API_URL, {headers: this.getHeaders});
  }

  findTicketsByEventId(eventId: number): Observable<Ticket[]> {
    return this.http.get<Ticket[]>(`${API_URL}/by-event/${eventId}`, {headers: this.getHeaders});
  }

  findTicketsByUserId(userId: number): Observable<Ticket[]> {
    return this.http.get<Ticket[]>(`${API_URL}/by-user/${userId}`, {headers: this.getHeaders});
  }

  decodeQRCode(qrCodeImage: Blob): Observable<boolean> {
    const formData = new FormData();
    formData.append('qrCodeImage', qrCodeImage);
    return this.http.post<boolean>(`${API_URL}/decode-qr`, formData, {headers: this.getHeaders});
  }
}
