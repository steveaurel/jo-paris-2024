import {Component, OnInit} from '@angular/core';
import {User} from "../../models/user.model";
import {AuthenticationService} from "../../services/authentication.service";
import {Router} from "@angular/router";
import {Role} from "../../models/role.enum";

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrl: './header.component.css'
})
export class HeaderComponent implements OnInit {
  currentUser?: User ;

  constructor(private authenticationService: AuthenticationService, private router: Router) {

  }

  ngOnInit(): void {
    this.authenticationService.currentUser.subscribe(data => {
      this.currentUser = data;
    });
  }

  isAdmin() {
    return this.currentUser?.role === Role.ADMIN;
  }

  logout() {
    this.authenticationService.logout();
    this.router.navigate(['/event']).then(success => {
      if (!success) {
        console.log('Navigation failed!');
      }
    });
  }
}
