import {Component, OnInit} from '@angular/core';
import {Venue} from "../../models/venue.model";
import {VenueService} from "../../services/venue.service";
import {Router} from "@angular/router";

@Component({
  selector: 'app-venue-management',
  templateUrl: './venue-management.component.html',
  styleUrl: './venue-management.component.css'
})
export class VenueManagementComponent implements OnInit {
  venues: Venue[] = [];
  filteredVenues: Venue[] = [];
  newVenue: Venue = { id: 0, name: '', capacity: 0, location: { id:0, address: '', city: '', country: '' } };
  displayedColumns: string[] = ['name', 'address', 'city', 'country', 'capacity', 'actions'];

  constructor(private venueService: VenueService, private router: Router) {}

  ngOnInit() {
    this.loadVenues();
  }
  loadVenues(){
    this.venueService.findAllVenues().subscribe({
      next: (venues) => {
        this.venues = venues;
        this.filteredVenues = venues;
      },
      error: (err) => {
        alert("Error loading venues");
        console.error('Error loading venues', err)
      }
    })
  }

  updateVenue(venue: Venue) {
    this.venueService.updateVenue(venue?.id, venue).subscribe({
      next: () => {
        alert('Venue has been successfully updated.');
        this.router.navigate(['/dashboard/venues']);
      },
      error: (error) => {
        console.error('Failed to update the venue:', error);
        alert('Failed to update the venue.');
      }
    });

  }

  filterVenues(event: Event) {
    const inputElement = event.target as HTMLInputElement; // Safe type assertion in TypeScript
    if (inputElement && inputElement.value) {
      const value = inputElement.value;
      this.filteredVenues = this.venues.filter(venue =>
        venue.name?.toLowerCase().includes(value.toLowerCase()) ||
        venue.location?.address?.toLowerCase().includes(value.toLowerCase()) ||
        venue.location?.city?.toLowerCase().includes(value.toLowerCase()) ||
        venue.location?.country?.toLowerCase().includes(value.toLowerCase())
      );
    } else {
      this.filteredVenues = this.venues; // Reset if no value
    }
  }
  addVenue() {
    if (this.newVenue && this.newVenue.name && this.newVenue.location?.address) {
      this.venueService.createVenue(this.newVenue).subscribe({
        next: (venue) => {
          this.ngOnInit();
          alert('Venue has been successfully added.');
          this.newVenue = { name: '', location: { address: '', city: '', country: '' }, capacity: 0 };
        },
        error: (error) => {
          console.error('Error adding venue:', error);
        }
      });
    } else {
      alert('Please fill in all fields.');
    }
  }

}
