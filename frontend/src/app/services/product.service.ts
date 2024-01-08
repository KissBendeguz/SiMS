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
  
  deleteProduct(inventoryId: number,productId: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${inventoryId}/${productId}`, { headers: httpOptions.headers });
  }

  modifyProduct(productId: number, product: Product):Observable<Product>{
    return this.http.patch<Product>(`${this.apiUrl}/${productId}`, product, { headers: httpOptions.headers });
  }
}
