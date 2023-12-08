import { Injectable } from '@angular/core';
import { Observable, BehaviorSubject } from 'rxjs';
import { HttpErrorResponse } from '@angular/common/http';

@Injectable({
  providedIn: 'root'
})
export class HttpErrorService {
  private errorSubject: BehaviorSubject<HttpErrorResponse | null> = new BehaviorSubject<HttpErrorResponse | null>(null);
  error$: Observable<HttpErrorResponse | null> = this.errorSubject.asObservable();

  addError(error: HttpErrorResponse) {
    this.errorSubject.next(error);
  }

}