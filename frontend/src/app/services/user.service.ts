import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Router } from '@angular/router';
import { CookieService } from 'ngx-cookie-service';
import { User } from '../models/user';
import { Observable, catchError, tap } from 'rxjs';
import { AuthService, httpOptions } from './auth.service';

@Injectable({
  providedIn: 'root'
})
export class UserService{
  private apiUrl = '/api/user';

  constructor(
    private http: HttpClient,
    private router: Router,
    private authService: AuthService
  ) {}

  getAuthenticatedUser():Observable<User>{
    return this.http.get<User>(`${this.apiUrl}`,{headers:httpOptions.headers});
  }
}
