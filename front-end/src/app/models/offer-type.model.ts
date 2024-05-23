import { Price } from './price.model';
import {Events} from "./events.model";

export class OfferType {
  id?: number;
  description?: string;
  seatQuantity?: number;
  event?:Events;
  price?: Price;

}
