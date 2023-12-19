import { Injectable } from '@angular/core';
import {
  HttpInterceptor,
  HttpRequest,
  HttpHandler,
  HttpEvent,
  HttpErrorResponse,
} from '@angular/common/http';
import { Observable, catchError, throwError } from 'rxjs';
import { AuthService } from './auth.service';
import { HttpErrorService } from './http-error.service';

@Injectable()
export class AuthInterceptor implements HttpInterceptor {
  constructor(private authService: AuthService,private httpErrorService:HttpErrorService) {}

  intercept(
    request: HttpRequest<any>,
    next: HttpHandler
  ): Observable<HttpEvent<any>> {
    const token = this.authService.getToken();

    let clonedRequest = request;
    if (token) {
      clonedRequest = request.clone({
        setHeaders: {
          Authorization: `Bearer ${token}`,
        },
      });
    }

    return next.handle(clonedRequest).pipe(
      catchError((error: HttpErrorResponse) => {
        this.httpErrorService.addError(error);
        if(token){
          this.authService.removeToken();
          if(error.status===401){ //UNAUTHORIZED
            console.warn("Invalid or expired token has been removed.");
          }else if(error.status >= 500 && error.status <= 504){
            console.warn("Token has been removed due to Server error.")
          }
        }
        return throwError(() => error);
      })
    );
  }
}