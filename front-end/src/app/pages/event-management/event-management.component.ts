import {ChangeDetectorRef, Component, OnInit} from '@angular/core';
import {EventService} from "../../services/event.service";
import {Events} from "../../models/events.model";
import {Venue} from "../../models/venue.model";
import {VenueService} from "../../services/venue.service";
import {FormBuilder, FormArray, FormGroup, Validators} from "@angular/forms";
import {MatDialog} from "@angular/material/dialog";
import {EditEventDialogComponent} from "../edit-event-dialog/edit-event-dialog.component";
import {Router} from "@angular/router";
import {ShowEventDialogComponent} from "../show-event-dialog/show-event-dialog.component";

@Component({
  selector: 'app-event-management',
  templateUrl: './event-management.component.html',
  styleUrl: './event-management.component.css'
})
export class EventManagementComponent implements OnInit {

  events: Events[] = [];
  filteredEvents: Events[] = [];
  venues: Venue[] =[];
  newEventForm!: FormGroup;

  displayedColumns: string[] = ['name', 'description', 'date', 'startTime', 'endTime', 'seat', 'status', 'venue', 'actions' ];

  constructor(private eventService: EventService,
              private venueService: VenueService,
              private fb: FormBuilder,
              private dialog: MatDialog,
              private cdr: ChangeDetectorRef,
              private router: Router) {
  }

  ngOnInit() {
    this.loadEvents();
    this.loadVenues();

    this.newEventForm = this.fb.group({
      name: ['', Validators.required],
      description: ['', Validators.required],
      date: ['', Validators.required],
      startTime: ['', Validators.required],
      endTime: ['', Validators.required],
      venueID: [0, Validators.required],
      offerTypes: this.fb.array([])
    });
  }

  loadEvents(): void {
    this.eventService.getAllEvents().subscribe({
      next: (events) => {
        this.events = events;
        this.filteredEvents = events
      },
      error: (error) => {
        console.error('Error loading events:', error);
        alert('Failed to load events');
      }
    });
  }
  loadVenues(){
    this.venueService.findAllVenues().subscribe({
      next: (venues) => {
        this.venues = venues;
      },
      error: (err) => {
        alert("Error loading venues");
        console.error('Error loading venues', err)
      }
    })
  }

  get offerTypes(): FormArray {
    return this.newEventForm.get('offerTypes') as FormArray;
  }

  addOfferType(): void {
    this.offerTypes.push(this.fb.group({
      description: ['', Validators.required],
      seatQuantity: [0, [Validators.required, Validators.min(1)]],
      price: [0, [Validators.required, Validators.min(1)]]
    }));
  }

  removeOfferType(index: number): void {
    this.offerTypes.removeAt(index);
  }

  filterEvent(event: Event) {
    const inputElement = event.target as HTMLInputElement;
    if (inputElement && inputElement.value) {
      const value = inputElement.value;
      this.filteredEvents = this.events.filter(event =>
        event.name?.toLowerCase().includes(value.toLowerCase()) ||
        event.description?.toLowerCase().includes(value.toLowerCase()) ||
        event.venue?.name?.toLowerCase().includes(value.toLowerCase())
      );
    } else {
      this.filteredEvents = this.events; // Reset if no value
    }
  }

  edit(event: Events) {
    const dialogRef = this.dialog.open(EditEventDialogComponent, {
      width: '500px',
      data: { event: event }
    });

    dialogRef.afterClosed().subscribe(result => {
      if (result) {
        this.loadEvents();
        this.router.navigate(['/dashboard/events']).then(() => {
        console.log('Navigation to /dashboard/events successful!');
      }).catch(error => {
        console.error('Navigation to /dashboard/events failed:', error);
      });
      }
        console.log('The dialog was closed with data:', result);

    });
  }
  show(event:Events){
    const dialogRef = this.dialog.open(ShowEventDialogComponent, {
      width: '500px',
      data: { id: event.id }
    });

    dialogRef.afterClosed().subscribe(result => {
      if (result) {
        this.loadEvents();
        this.router.navigate(['/dashboard/events']).then(() => {
          console.log('Navigation to /dashboard/events successful!');
        }).catch(error => {
          console.error('Navigation to /dashboard/events failed:', error);
        });
      }
      console.log('The dialog was closed with data:', result);

    });
  }
  addEvent() {
    if (this.newEventForm.valid) {
      const formValue = this.newEventForm.value;
      const newEvent: Events = {
        name: formValue.name,
        description: formValue.description,
        date: formValue.date,
        startTime: formValue.startTime,
        endTime: formValue.endTime,
        venueID: formValue.venueID,
        offerTypes: formValue.offerTypes.map((offer: { description: any; seatQuantity: any; price: any; }) => ({
          description: offer.description,
          seatQuantity: offer.seatQuantity,
          price: {
            amount: offer.price,
            currency: 'EUR'
          }
        }))
      };

      if (new Date(newEvent.date + 'T' + newEvent.startTime) >= new Date(newEvent.date + 'T' + newEvent.endTime)) {
        alert('Start time must be earlier than end time.');
      }

      this.eventService.checkEventAvailability(newEvent.venueID, newEvent.startTime, newEvent.endTime, newEvent.date).subscribe(isAvailable => {
        if (isAvailable) {
          this.eventService.createEvent(newEvent).subscribe({
            next: () => {
              alert('Event successfully added!');
            },
            error: (err) => console.error('Failed to create event:', err)
          });
        } else {
          alert('An event is already scheduled at this venue within 2 hours of your selected time.');
        }
      });
      console.log(newEvent);
    } else {
      console.error('Form is not valid');
    }
  }

}
