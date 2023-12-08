import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { NavigationEnd, Router } from '@angular/router';
import { CookieService } from 'ngx-cookie-service';
import { User } from '../models/user';
import { BehaviorSubject, Observable, catchError, filter, of, tap } from 'rxjs';
import { AuthService, httpOptions } from './auth.service';

@Injectable({
  providedIn: 'root'
})
export class UserService{
  private apiUrl = '/api/user';
  private authenticatedUserSubject: BehaviorSubject<User | null> = new BehaviorSubject<User | null>(null);
  authenticatedUser$: Observable<User | null> = this.authenticatedUserSubject.asObservable();
  responseError:string|null;

  constructor(
    private http: HttpClient,
    private router: Router,
    private authService: AuthService
  ) {
    //this.loadAuthenticatedUser();
    this.router.events.pipe(
      filter(event => event instanceof NavigationEnd)
    ).subscribe(() => {
      this.loadAuthenticatedUser();
    });
  }

  private loadAuthenticatedUser() {
    if (this.authService.isAuthenticated() && !this.authenticatedUserSubject.value) {
      this.getAuthenticatedUser().subscribe(
        user => this.authenticatedUserSubject.next(user)
      );
    }
  }

  public logout(){
    this.authenticatedUserSubject.next(null);
    httpOptions.headers = httpOptions.headers.set('Authorization', ``);
    this.authService.removeToken();
  }

  getAuthenticatedUser(): Observable<User | null> {
    return this.http.get<User>(`${this.apiUrl}`, { headers: httpOptions.headers });
  }

}
