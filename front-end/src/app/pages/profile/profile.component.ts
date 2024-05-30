import { Component, OnInit } from '@angular/core';
import { User } from "../../models/user.model";
import { Router } from "@angular/router";
import { TicketService } from "../../services/ticket.service";
import { Ticket } from "../../models/ticket.model";
import { AuthenticationService } from "../../services/authentication.service";
import { UserService } from "../../services/user.service";

@Component({
  selector: 'app-profile',
  templateUrl: './profile.component.html',
  styleUrls: ['./profile.component.css']
})
export class ProfileComponent implements OnInit {
  user: User | any;
  ticketList: Ticket[] = [];
  selectedIndex = 0;
  displayedColumns: string[] = ['eventName', 'eventDate', 'eventStartTime', 'actions'];

  constructor(
    private router: Router,
    private userService: UserService,
    private ticketService: TicketService,
    private authenticationService: AuthenticationService
  ) {}

  ngOnInit(): void {
    if (!this.authenticationService.currentUserValue?.id) {
      this.router.navigate(['/401']).then(success => {
        if (!success) {
          console.log('Navigation failed!');
        }
      });
    }

    this.user = this.authenticationService.currentUserValue;
    this.ticketService.findTicketsByUserId(this.user?.id).subscribe({
      next: (tickets) => {
        this.ticketList = tickets;
        console.log(this.ticketList);
      },
      error: (error) => console.error('Error fetching tickets', error)
    });
  }

  updateProfile() {
    this.userService.updateUser(this.user?.id, this.user).subscribe(user => {
      this.user = user;
    });
  }

  downloadTicket(ticket: Ticket) {
    // Implement the logic to download the ticket
  }
}
