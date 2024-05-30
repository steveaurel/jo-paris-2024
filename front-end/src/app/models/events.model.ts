import { EventStatus } from './event-status.enum';
import {Venue} from "./venue.model";
import {OfferType} from "./offer-type.model";

export interface Events {
  id?: number;
  name: string;
  description: string;
  date: string;
  startTime: string;
  endTime: string;
  offerTypes: OfferType[];
  venueID: number;
  venue?: Venue;
  eventStatus?: EventStatus;
  seatAvailable?: number;

}
