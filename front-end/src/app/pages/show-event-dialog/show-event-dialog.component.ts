import {Component, Inject, OnInit} from '@angular/core';
import {MAT_DIALOG_DATA} from "@angular/material/dialog";
import {PaymentService} from "../../services/payment.service";
import {Router} from "@angular/router";
import {Payment} from "../../models/payment.model";
import {User} from "../../models/user.model";
import {OfferType} from "../../models/offer-type.model";
import {UserService} from "../../services/user.service";
import {OfferTypeService} from "../../services/offer-type.service";

@Component({
  selector: 'app-show-event-dialog',
  templateUrl: './show-event-dialog.component.html',
  styleUrl: './show-event-dialog.component.css'
})
export class ShowEventDialogComponent implements OnInit{
  payments: Payment[] = []
  users: { [key: number]: User } = {};
  offerTypes: { [key: number]: OfferType } = [];

  displayedColumns: string[] = ['name', 'date', 'amount'];

  constructor(private paymentService: PaymentService,
              private userService: UserService,
              private offerTypeService: OfferTypeService,
              @Inject(MAT_DIALOG_DATA) public data: any) {
  }
  ngOnInit(): void {
    this.paymentService.findPaymentsByEventId(this.data.id).subscribe({
      next: (payments) => {
        this.payments = payments;
        this.loadAdditionalData();
      },
      error: (error) => {
        console.error('Error loading payments:', error);
        alert('Failed to load payments');
      }
    })
  }

  loadAdditionalData(): void {
    this.payments.forEach((payment) => {
      this.userService.getUserById(payment.userID).subscribe({
        next: (user) => {
          this.users[payment.userID] = user;
        },
        error: (error) =>{
          console.error('Error loading user:', error);
          alert('Failed to load user');
        }
      })
    });
  }

  getTotalAmount(): number {
    return this.payments.reduce((total, payment) => total + payment.amount, 0);
  }

}
