import { Location } from './location.model';

export interface Venue {
  id?: number;
  name: string;
  capacity: number;
  location?: Location;
}
