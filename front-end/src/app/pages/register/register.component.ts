import {Component, OnInit} from '@angular/core';
import {User} from "../../models/user.model";
import {faUserCircle} from "@fortawesome/free-solid-svg-icons";
import {AuthenticationService} from "../../services/authentication.service";
import {Router} from "@angular/router";
import {take, throwError} from "rxjs";
import {catchError} from "rxjs/operators";
import {Role} from "../../models/role.enum";

@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrl: './register.component.css'
})
export class RegisterComponent implements OnInit{

  user: User = { firstName: '', lastName: '', email: '', password: '', role: Role.USER };
  faUser = faUserCircle;
  errorMessage: string = "";

  constructor(private authenticationService: AuthenticationService, private router: Router) {
  }

  ngOnInit(): void {
    if (this.authenticationService.currentUserValue?.id) {
      this.router.navigate(['/profile']);
    }
  }

  register() {
    this.authenticationService.register(this.user).pipe(
      take(1),
      catchError(err => {
        if (err?.status === 409) {
          this.errorMessage = 'Email already exists.';
        } else {
          this.errorMessage = 'Unexpected error occurred.';
          console.log(err);
        }
        return throwError(() => err);
      })
    ).subscribe({
      next: () => {
        this.router.navigate(['/event']).then(success => {
          if (!success) {
            console.error('Navigation failed!');
          }
        });
      },
      error: () => {}
    });
  }

}
