export interface Payment {
  id?: number;
  amount: number;
  paymentDateTime: Date;
  userID: number;
  offerTypeID: number;
}
