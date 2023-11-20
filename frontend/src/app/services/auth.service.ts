import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, of } from 'rxjs';
import { catchError, tap } from 'rxjs/operators';
import { CookieService } from 'ngx-cookie-service';

export interface RegisterRequest {
  email: string;
  password: string;
}

export interface LoginRequest {
  email: string;
  password: string;
}

export interface AuthenticationResponse {
  token: string;
}

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private apiUrl = '/user';
  private tokenKey = '';
  public loggedIn = false;

  constructor(
    private http: HttpClient,
    private cookieService: CookieService
  ) {}

  register(registerData: RegisterRequest): Observable<AuthenticationResponse> {
    console.log("authservice:register")
    return this.http.post<AuthenticationResponse>(`${this.apiUrl}/register`, registerData).pipe(
      tap(response => {
        this.setToken(response.token);
        console.log(response)
      }),
      catchError(error => {
        console.error('Registration error:', error);
        throw error;
      })
    );
  }

  login(loginData: LoginRequest): Observable<AuthenticationResponse> {
    return this.http.post<AuthenticationResponse>(`${this.apiUrl}/login`, loginData).pipe(
      tap(response => this.setToken(response.token)),
      catchError(error => {
        console.error('Login error:', error);
        throw error;
      })
    );
  }

  private setToken(token: string): void {
    const expirationTimestamp = this.parseTokenPayload(token).exp;
    //this.tokenKey = token;
    
    this.cookieService.set(this.tokenKey, token, { expires: new Date(expirationTimestamp * 1000) });
  }

  getToken(): string | null {
    return this.cookieService.get(this.tokenKey);
  }

  removeToken(): void {
    this.cookieService.delete(this.tokenKey);
  }

  isAuthenticated(): boolean {
    const token = this.getToken();
    return !!token && !this.isTokenExpired(token);
  }

  private isTokenExpired(token: string): boolean {
    const expirationTimestamp = this.parseTokenPayload(token).exp;

    return expirationTimestamp < Date.now() / 1000;
  }

  private parseTokenPayload(token: string): any {
    const tokenBody = token.split('.')[1];
    const decodedTokenBody = atob(tokenBody);
    return JSON.parse(decodedTokenBody);
  }
}