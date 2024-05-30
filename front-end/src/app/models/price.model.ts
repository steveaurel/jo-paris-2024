import { OfferType } from './offer-type.model';

export interface Price {
  id: number;
  amount: number;
  currency: string;
  offerType?: OfferType;

}
