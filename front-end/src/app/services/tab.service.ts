import { Injectable } from '@angular/core';
import { BehaviorSubject } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class TabService {
  private tabIndexSource = new BehaviorSubject<number>(0);
  currentTab = this.tabIndexSource.asObservable();

  constructor() { }

  changeTab(index: number) {
    this.tabIndexSource.next(index);
  }
}
