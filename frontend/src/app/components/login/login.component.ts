import { HttpErrorResponse } from '@angular/common/http';
import { Component } from '@angular/core';
import { FormBuilder, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { AuthService, LoginRequest } from 'src/app/services/auth.service';
import { HttpErrorService } from 'src/app/services/http-error.service';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss']
})
export class LoginComponent {
  errorMessage:string;

  

  constructor(
    private authService:AuthService,
    private router:Router,
    private httpErrorService:HttpErrorService,
    private formBuilder: FormBuilder,
  ){
    if(this.authService.isAuthenticated()){
      this.router.navigate(['/']);
    }
    this.httpErrorService.error$.subscribe((error: HttpErrorResponse | null) => {
        switch(error?.status){
          case 403:
            this.errorMessage = "Bad credentials";
            break;
          case 500:
          case 501:
          case 502:
          case 503:
          case 504:
            this.errorMessage = "Service unavailable";
            break;
        }
      }
    );
  }
  loginForm = this.formBuilder.nonNullable.group({
    email: ['', [Validators.required]],
    password: ['', [Validators.required]],
  }, { updateOn: 'blur' });

  get email() { return this.loginForm.get('email') }
  get password() { return this.loginForm.get('password') }

  login(){
    console.log(this.loginForm.valid)
    if(!this.loginForm.valid){
      Object.keys(this.loginForm.controls).forEach(key => {
        this.loginForm.controls[key].markAsTouched();
      });
      this.loginForm.updateValueAndValidity();
      return;
    }
    const loginData:LoginRequest = {
      email: this.email!.value,
      password: this.password!.value
    }

    this.authService.login(loginData).subscribe({
      next: () => this.router.navigate(['/']),
    });
  }
}
