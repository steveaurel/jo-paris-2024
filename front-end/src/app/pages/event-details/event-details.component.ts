import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { Events } from '../../models/events.model';
import { EventService } from "../../services/event.service";
import { OfferType } from "../../models/offer-type.model";
import { PaymentService } from "../../services/payment.service";
import { Payment } from "../../models/payment.model";
import { AuthenticationService } from "../../services/authentication.service";
import {MatDialog} from "@angular/material/dialog";
import {LoginDialogComponent} from "../login-dialog/login-dialog.component";
import {formatDate} from "@angular/common";
import {Ticket} from "../../models/ticket.model";
import {TicketService} from "../../services/ticket.service";
import {TabService} from "../../services/tab.service";

@Component({
  selector: 'app-event-details',
  templateUrl: './event-details.component.html',
  styleUrls: ['./event-details.component.css']
})
export class EventDetailsComponent implements OnInit {
  event?: Events  ;
  selectedOfferType?: OfferType;
  selectedPrice?: number ;
  payment: Payment | any ;
  ticket?: Ticket ;

  constructor(
    private eventService: EventService,
    private paymentService: PaymentService,
    private authenticationService: AuthenticationService,
    private ticketService: TicketService,
    private route: ActivatedRoute,
    private router: Router,
    private tabService: TabService,
    private dialog: MatDialog
  ) {}

  ngOnInit(): void {
    const eventId = this.route.snapshot.params['id'];
    this.eventService.getEventById(eventId).subscribe({
      next: (event) => {
        this.event = event;
      },
      error: (err) => {
        console.error('Failed to load event details:', err);
        alert('Failed to load event details. Please try again.');
      }
    });
  }

  onOfferTypeChange(offerType: OfferType): void {
    this.selectedOfferType = offerType;
    this.selectedPrice = offerType.price?.amount;
  }

  pay(): void {
    if (!this.selectedOfferType) {
      alert('Please select an offer before proceeding.');
      return;
    }
    if (!this.authenticationService.currentUserValue?.id) {
      const dialogRef = this.dialog.open(LoginDialogComponent, {
        width: '500px',
        data: { id: this.event?.id }
      });

      dialogRef.afterClosed().subscribe(result => {
        if (result === true) {
          this.performPayment();
        }
      });
    } else {
      this.performPayment();
    }
  }

  performPayment(): void {
    if(this.event) {
      if (this.eventService.validateEventStatus(this.event.id)) {
        this.payment.eventID = this.event.id;
        this.payment.offerTypeID = this.selectedOfferType?.id;
        this.payment.userID = this.authenticationService.currentUserValue.id;
        this.payment.amount = this.selectedPrice;
        this.payment.paymentDateTime = formatDate(new Date(), 'yyyy-MM-dd HH:mm:ss', 'en-US');

        this.paymentService.createPayment(this.payment).subscribe({
          next: (payment) => {
            this.payment = payment;
            this.generateTicket(payment.id)
            alert('Payment successful.');
            this.tabService.changeTab(1)
            this.router.navigate(['/profile']);
          },
          error: (err) => {
            console.error('Payment failed:', err);
            alert('Payment failed. Please check your details and try again.');
          }
        });
      } else {
        alert('Event is not available for payments.');
      }
    }
  }

  generateTicket(paymentID: number | undefined): void {
    if (this.event && this.selectedPrice) {
      const ticket: Ticket = {
        userID: this.authenticationService.currentUserValue.id,
        eventID: this.event.id,
        eventName: this.event.name,
        eventDate: this.event.date,
        eventStartTime: this.event.startTime,
        offerTypeID: this.selectedOfferType?.id,
        paymentID: paymentID,
        priceAmount: this.selectedPrice
      }
      this.ticketService.createTicket(ticket).subscribe({
        next: (ticket) => {
          this.ticket = ticket;
        },
        error: (err) => {
          console.error('Failed to create ticket:', err);
        }
      })
    }
  }

  goBackToEvents(): void {
    this.router.navigate(['/event']).then(success => {
      if (!success) {
        console.log('Navigation failed!');
        alert('Failed to navigate back to events.');
      }
    });
  }
}
