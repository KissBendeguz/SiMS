import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Product } from '../models/product';
import { httpOptions } from './auth.service';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class ProductService {

  constructor(private http:HttpClient) { }
  private apiUrl = '/api/product';

  createProduct(inventoryId: number, product: Product):Observable<Product>{
    return this.http.post<Product>(`${this.apiUrl}/${inventoryId}`, product, { headers: httpOptions.headers });
  }
  
  deleteBusiness(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`, { headers: httpOptions.headers });
  }

  modifyProduct(inventoryId: number, product: Product):Observable<Product>{
    return this.http.patch<Product>(`${this.apiUrl}/${inventoryId}`, product, { headers: httpOptions.headers });
  }
}
