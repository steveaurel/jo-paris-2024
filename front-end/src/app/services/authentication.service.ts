import { Injectable } from '@angular/core';
import {User} from "../models/user.model";
import {BehaviorSubject, map, Observable} from "rxjs";
import {HttpClient} from "@angular/common/http";
import {environment} from "../../environments/environment";
import {AuthRequest} from "../models/authrequest.model";
import {UserImplementation} from "../models/UserImplementation";

const API_URL = environment.BASE_URL + '/AUTH-SERVICE/auth';

@Injectable({
  providedIn: 'root'
})
export class AuthenticationService {
  public currentUser: Observable<User>;
  private currentUserSubject: BehaviorSubject<User>;

  constructor(private http: HttpClient) {
    let storageUser;
    const storageUserAsStr = localStorage.getItem('currentUser');
    if (storageUserAsStr) {
      storageUser = JSON.parse(storageUserAsStr);
    }

    this.currentUserSubject = new BehaviorSubject<User>(storageUser);
    this.currentUser = this.currentUserSubject.asObservable();
  }

  public get currentUserValue(): User {
    return this.currentUserSubject.value;
  }

    login(authRequest: AuthRequest | undefined): Observable<any> {
    return this.http.post<User>(API_URL + '/sign-in', authRequest).pipe(
      map(response => {
        if (response) {
          //set session-user
          this.setSessionUser(response);
        }
        return response;
      })
    );
  }

  setSessionUser(user: User) {
    localStorage.setItem('currentUser', JSON.stringify(user));
    this.currentUserSubject.next(user);
  }

  register(user: User | undefined): Observable<any> {
    return this.http.post<User>(API_URL + '/sign-up', user).pipe(
      map(response => {
        if (response) {
          //set session-user
          this.setSessionUser(response);
        }
        return response;
      })
    );
  }

  logout() {
    localStorage.removeItem('currentUser');
    this.currentUserSubject.next(new UserImplementation());
  }

  refreshToken(): Observable<any> {
    return this.http.post(API_URL + '/refresh-token?token=' + this.currentUserValue?.refreshToken, {});
  }
}
