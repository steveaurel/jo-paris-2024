import { Component, Inject, OnInit } from '@angular/core';
import { FormGroup, FormBuilder, Validators, FormArray } from '@angular/forms';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { VenueService } from "../../services/venue.service";
import {Events} from "../../models/events.model";
import {EventService} from "../../services/event.service";

@Component({
  selector: 'app-edit-event-dialog',
  templateUrl: './edit-event-dialog.component.html',
  styleUrls: ['./edit-event-dialog.component.css']
})
export class EditEventDialogComponent implements OnInit {
  event: Events | undefined;
  eventForm!: FormGroup;
  venues: any[] = [];


  constructor(
    private fb: FormBuilder,
    private venueService: VenueService,
    private eventService: EventService,
    public dialogRef: MatDialogRef<EditEventDialogComponent>,
    @Inject(MAT_DIALOG_DATA) public data: any
  ) {}

  ngOnInit(): void {
    this.eventForm = this.fb.group({
      id: [this.data.event.id],
      name: [this.data.event.name, Validators.required],
      description: [this.data.event.description, Validators.required],
      date: [this.data.event.date, Validators.required],
      startTime: [this.data.event.startTime, Validators.required],
      endTime: [this.data.event.endTime, Validators.required],
      eventStatus: [this.data.event.eventStatus],
      venueID: [{value: this.data.event.venueID, disabled: true}, Validators.required],
      offerTypes: this.fb.array([])
    });

    this.loadEvent();
    this.loadVenues();
    this.addOfferTypes(this.data.event.offerTypes || []);
  }

  loadEvent(): void {
    this.eventService.getEventById(this.data.event.id).subscribe({
      next: (event) => this.event = event,
      error: (err) => console.error('Error loading event', err)
    });
  }

  loadVenues(): void {
    this.venueService.findAllVenues().subscribe({
      next: (venues) => this.venues = venues,
      error: (err) => console.error('Error loading venues', err)
    });
  }

  addOfferTypes(offerTypes: any[]): void {
    offerTypes.forEach(offerType => this.addOfferType(offerType));
  }

  addOfferType(offerType?: any): void {
    this.offerTypes.push(this.createOfferTypeFormGroup(offerType));
  }

  createOfferTypeFormGroup(offerType?: any): FormGroup {
    return this.fb.group({
      description: [offerType ? offerType.description : '', Validators.required],
      seatQuantity: [offerType ? offerType.seatQuantity : 0, [Validators.required, Validators.min(1)]],
      price: [offerType ? offerType.price?.amount : 0, [Validators.required, Validators.min(1)]]
    });
  }

  get offerTypes(): FormArray {
    return this.eventForm.get('offerTypes') as FormArray;
  }

  removeOfferType(index: number): void {
    this.offerTypes.removeAt(index);
  }

  onSave(): void {
    if (this.eventForm.valid && this.event) {

      const formValue = this.eventForm.value;
      const updatedEvent: Events = {
        id:formValue.id,
        name: formValue.name,
        description: formValue.description,
        date: formValue.date,
        startTime: formValue.startTime,
        endTime: formValue.endTime,
        eventStatus: formValue.eventStatus,
        venueID: this.event.venueID,
        venue:this.event.venue,
        seatAvailable: this.event.seatAvailable,
        offerTypes: formValue.offerTypes.map((offer: { description: any; seatQuantity: any; price: any; }) => ({
          description: offer.description,
          seatQuantity: offer.seatQuantity,
          price: {
            amount: offer.price,
            currency: 'EUR'
          }
        }))
      };

      if (new Date(updatedEvent.date + 'T' + updatedEvent.startTime) >= new Date(updatedEvent.date + 'T' + updatedEvent.endTime)) {
        alert('Start time must be earlier than end time.');
        return;
      }

      this.eventService.updateEvent(updatedEvent.id, updatedEvent).subscribe({
        next: () => alert('Event updated added!'),
        error: (err) => console.error('Failed to update event:', err)
      });

      console.log(updatedEvent);
      this.dialogRef.close(this.eventForm.value);
    } else {
      console.error('Form is not valid');
      }
    }

  onCancel(): void {
    this.dialogRef.close();
  }
}
