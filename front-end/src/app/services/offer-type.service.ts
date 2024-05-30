import { Injectable } from '@angular/core';
import {environment} from "../../environments/environment";
import {RequestBaseService} from "./request-base.service";
import {AuthenticationService} from "./authentication.service";
import {HttpClient} from "@angular/common/http";
import {Observable} from "rxjs";
import {OfferType} from "../models/offer-type.model";

const API_URL = environment.BASE_URL + '/EVENT-SERVICE' + '/offertypes';
@Injectable({
  providedIn: 'root'
})
export class OfferTypeService extends RequestBaseService{

  constructor(authenticationService: AuthenticationService, http: HttpClient) {
    super(authenticationService, http);
  }
  getOfferTypeById(id: number): Observable<OfferType> {
    return this.http.get<OfferType>(`${API_URL}/${id}`, {headers: this.getHeaders});
  }
}
