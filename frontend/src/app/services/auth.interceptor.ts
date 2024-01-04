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
import { UserService } from './user.service';

@Injectable()
export class AuthInterceptor implements HttpInterceptor {
  constructor(
    private authService: AuthService,
    private httpErrorService:HttpErrorService,
    private userService: UserService) {}

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
    console.log(clonedRequest)
    return next.handle(clonedRequest).pipe(
      catchError((error: HttpErrorResponse) => {
        this.httpErrorService.addError(error);
        //if(token){

          switch(error.status){
            case 401:
            case 500:
            case 504:
              console.warn("Interceptor forced a logout due to an error(UNAUTHORIZED or SERVER ERROR)");
              this.userService.logout();
          }

        //}
        return throwError(() => error);
      })
    );
  }
}