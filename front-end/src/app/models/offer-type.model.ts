import { Price } from './price.model';
import {Events} from "./events.model";

export interface OfferType {
  id: number;
  description: string;
  seatQuantity: number;
  event?:Events;
  price?: Price;

}
