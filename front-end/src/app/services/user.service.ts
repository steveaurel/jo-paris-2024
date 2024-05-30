import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';
import { User } from '../models/user.model';
import {RequestBaseService} from "./request-base.service";
import {AuthenticationService} from "./authentication.service";

const API_URL = environment.BASE_URL+ '/USER-SERVICE' + '/users';

@Injectable({
  providedIn: 'root'
})
export class UserService extends RequestBaseService {

  constructor(authenticationService: AuthenticationService, http: HttpClient) {
    super(authenticationService, http);
  }

    getUserById(userId: number | undefined): Observable<User> {
    return this.http.get<User>(`${API_URL}/${userId}`, {headers: this.getHeaders});
  }

  getAllUsers(): Observable<User[]> {
    return this.http.get<User[]>(API_URL, {headers: this.getHeaders});
  }

  createUser(user: User): Observable<User> {
    return this.http.post<User>(API_URL, user, {headers: this.getHeaders});
  }

    updateUser(userId: number | undefined, user: User): Observable<User> {
    return this.http.put<User>(`${API_URL}/${userId}`, user, {headers: this.getHeaders});
  }

  deleteUser(userId: number): Observable<void> {
    return this.http.delete<void>(`${API_URL}/${userId}`, {headers: this.getHeaders});
  }

  getUserByEmail(email: string): Observable<User> {
    return this.http.get<User>(`${API_URL}/by-email`, { params: { email }, headers: this.getHeaders});
  }
}

