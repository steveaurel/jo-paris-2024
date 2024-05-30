export interface Ticket {
  id?: number;
  firstName?: string;
  lastName?: string;
  userID?: number;
  eventID?: number;
  eventName: string;
  eventDate: string;
  eventStartTime: string;
  paymentID?: number;
  offerTypeID?: number;
  priceAmount: number;

}
