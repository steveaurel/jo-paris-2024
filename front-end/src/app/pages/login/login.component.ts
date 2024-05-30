import { Component, OnInit } from '@angular/core';
import {User} from "../../models/user.model";
import {faUserCircle} from "@fortawesome/free-solid-svg-icons";
import {AuthenticationService} from "../../services/authentication.service";
import {Router} from "@angular/router";
import {AuthRequest} from "../../models/authrequest.model";

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit {

  user?: User;
  authRequest: AuthRequest = { email: '', password: '' };
  faUser = faUserCircle;
  errorMessage: string = "";

  constructor(private authenticationService: AuthenticationService, private router: Router) { }

  ngOnInit(): void {
    if (this.authenticationService.currentUserValue?.id) {
      this.router.navigate(['/profile']).then(success => {
        if (!success) {
          console.log('Navigation failed!');
        }
      });
    }
  }

  login() {
    this.authenticationService.login(this.authRequest).subscribe({
      next: () => {
        this.router.navigate(['/profile']);
      },
      error: (error) => {
        this.errorMessage = 'Email or password is incorrect.';
        console.log(error);
      }
    });
  }


}
