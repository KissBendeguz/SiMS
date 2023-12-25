import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Business } from '../models/business';
import { User } from '../models/user';
import { Inventory } from '../models/inventory';
import { httpOptions } from './auth.service';

@Injectable({
  providedIn: 'root'
})
export class BusinessService {

  private apiUrl = '/api/businesses';

  constructor(private http: HttpClient) {}

  getAssociatedBusinesses(): Observable<Set<Business>> {
    return this.http.get<Set<Business>>(`${this.apiUrl}`, { headers: httpOptions.headers });
  }

  createBusiness(business: Business): Observable<Business> {
    return this.http.post<Business>(`${this.apiUrl}`, business, { headers: httpOptions.headers });
  }
  getBusiness(id: number): Observable<Business>{
    return this.http.get<Business>(`${this.apiUrl}/${id}`,{ headers: httpOptions.headers })
  }

  deleteBusiness(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`, { headers: httpOptions.headers });
  }

  updateBusiness(id: number, business: Business): Observable<Business> {
    return this.http.patch<Business>(`${this.apiUrl}/${id}`, business, { headers: httpOptions.headers });
  }

  inviteUser(businessId: number, userId: number): Observable<User> {
    return this.http.patch<User>(`${this.apiUrl}/${businessId}/invite/${userId}`, {}, { headers: httpOptions.headers });
  }

  joinBusiness(id: number): Observable<Business> {
    return this.http.patch<Business>(`${this.apiUrl}/${id}/join`, {}, { headers: httpOptions.headers });
  }

  getBusinessInventories(id: number): Observable<Set<Inventory>> {
    return this.http.get<Set<Inventory>>(`${this.apiUrl}/${id}/inventories`, { headers: httpOptions.headers });
  }
}
