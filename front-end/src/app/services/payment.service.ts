import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';
import { Payment } from '../models/payment.model';
import {AuthenticationService} from "./authentication.service";
import {RequestBaseService} from "./request-base.service";

const API_URL = environment.BASE_URL + '/PAYMENT-SERVICE' + '/payments';

@Injectable({
  providedIn: 'root'
})
export class PaymentService extends RequestBaseService {

  constructor(authenticationService: AuthenticationService, http: HttpClient) {
    super(authenticationService, http);
  }

  createPayment(payment: Payment): Observable<Payment> {
    return this.http.post<Payment>(API_URL, payment, {headers: this.getHeaders});
  }

  findPaymentById(id: number): Observable<Payment> {
    return this.http.get<Payment>(`${API_URL}/${id}`, {headers: this.getHeaders});
  }

  findAllPayments(): Observable<Payment[]> {
    return this.http.get<Payment[]>(API_URL, {headers: this.getHeaders});
  }

  findPaymentsByEventId(eventId: number): Observable<Payment[]> {
    return this.http.get<Payment[]>(`${API_URL}/by-event/${eventId}`, {headers: this.getHeaders});
  }

  findPaymentsByUserId(userId: number): Observable<Payment[]> {
    return this.http.get<Payment[]>(`${API_URL}/by-user/${userId}`);
  }
}
