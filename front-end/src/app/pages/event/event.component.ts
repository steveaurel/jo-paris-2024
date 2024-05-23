import {Component, HostListener, OnInit} from '@angular/core';
import {EventService} from "../../services/event.service";
import {Events} from '../../models/events.model';
import {Router} from "@angular/router"

@Component({
  selector: 'app-event',
  templateUrl: './event.component.html',
  styleUrl: './event.component.css'
})
export class EventComponent implements OnInit{
  activeEvents: Events[] = [];
  cols?: number;

  constructor(private eventService: EventService, private router: Router) {
    this.onResize(window.innerWidth);
  }

  ngOnInit(): void {
    this.eventService.findAllActiveEvents().subscribe(events => {
      this.activeEvents = events;
    });
  }
  @HostListener('window:resize', ['$event.target.innerWidth'])
  onResize(width: number) {
    if (width < 600) {
      this.cols = 1; // Less than 600px wide, show 1 tile per row
    } else if (width < 960) {
      this.cols = 2; // Less than 960px wide, show 2 tiles per row
    } else {
      this.cols = 3; // 960px or wider, show 3 tiles per row
    }
  }

  navigateToEventDetails(eventId: number | undefined): void {
    this.router.navigate(['/event-details', eventId]).then(success => {
      if (success) {
        console.log('Navigation was successful!');
      } else {
        console.log('Navigation failed!');
      }
    }).catch(error => {
      console.error('Error navigating:', error);
    });
  }
}
