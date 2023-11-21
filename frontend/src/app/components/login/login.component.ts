import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { AuthService, LoginRequest } from 'src/app/services/auth.service';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss']
})
export class LoginComponent {
  email: string = "";
  password: string = "";

  constructor(
    private authService:AuthService,
    private router:Router
  ){}

  login(){
    const loginData:LoginRequest = {
      email: this.email,
      password: this.password
    }
    const handler = {
      next: res => {
        this.router.navigate(['/'])
      },
      error: err => {
        console.log(err.status);
      }
    };
    this.authService.login(loginData).subscribe(handler);
  }
}
