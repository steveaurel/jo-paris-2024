import { Injectable } from '@angular/core';
import {
  HttpRequest,
  HttpHandler,
  HttpEvent,
  HttpInterceptor, HTTP_INTERCEPTORS
} from '@angular/common/http';
import {Observable, throwError} from 'rxjs';
import {AuthenticationService} from "../services/authentication.service";
import {Router} from "@angular/router";
import {catchError, switchMap} from "rxjs/operators";
import {User} from "../models/user.model";
import {jwtDecode, JwtPayload} from "jwt-decode";

const HEADER_AUTHORIZATION = "authorization";

@Injectable()
export class AuthInterceptor implements HttpInterceptor {

  private jwt: JwtPayload = {};

  constructor(private authenticationService: AuthenticationService, private router: Router) {}

  intercept(request: HttpRequest<unknown>, next: HttpHandler): Observable<HttpEvent<unknown>> {

    if (request.headers.has(HEADER_AUTHORIZATION)) {
      return this.handleToken(request, next);
    } else {
      return next.handle(request);
    }
  }

  private handleToken(request: HttpRequest<unknown>, next: HttpHandler) {

    this.jwt = jwtDecode(this.authenticationService.currentUserValue.accessToken ?? '');

    const nowInSecs = Date.now() / 1000;
    const exp = this.jwt.exp || 0;

    if (exp > nowInSecs) {
      return next.handle(request);
    } else {
      return this.authenticationService.refreshToken().pipe(
        switchMap((response: User) => {
          this.authenticationService.setSessionUser(response);

          const cloned = request.clone({
            headers: request.headers.set(HEADER_AUTHORIZATION, 'Bearer ' + response.accessToken)
          });

          return next.handle(cloned);
        }),
        catchError(err => {
          this.authenticationService.logout();

          this.router.navigate(['/event']).then(() => {
            console.log('Navigation to home page.');
          }).catch(navErr => {
            console.error('Navigation error:', navErr);
          });

          // Retourner un nouvel Observable en cas d'erreur
          return throwError(() => new Error(err));
        })
      );
    }
  }
}

export const authInterceptorProviders = [
  { provide: HTTP_INTERCEPTORS, useClass: AuthInterceptor, multi: true }
];
