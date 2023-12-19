import { Injectable } from '@angular/core';
import { Inventory } from '../models/inventory';
import { HttpClient } from '@angular/common/http';
import { httpOptions } from './auth.service';
import { Product } from '../models/product';

@Injectable({
  providedIn: 'root'
})
export class InventoryService {
  private apiUrl = '/api/inventory';

  constructor(private http:HttpClient) { }

  createInventory(businessId:number,inventory:Inventory){
    return this.http.post<Inventory>(`${this.apiUrl}/${businessId}`, inventory, { headers: httpOptions.headers });
  }
  getInventory(id:number){
    return this.http.get<Inventory>(`${this.apiUrl}/${id}`, { headers: httpOptions.headers });
  }
  modifyInventory(id:number, inventory:Inventory){
    return this.http.patch<Inventory>(`${this.apiUrl}/${id}`, inventory, { headers: httpOptions.headers });
  }
  getAllProducts(id:number){
    return this.http.get<Product>(`${this.apiUrl}/${id}/products`, { headers: httpOptions.headers });
  }
}
