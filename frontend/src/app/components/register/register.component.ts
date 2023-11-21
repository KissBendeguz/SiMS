import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { catchError, tap } from 'rxjs';
import { AuthService, RegisterRequest } from 'src/app/services/auth.service';

@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.scss']
})
export class RegisterComponent {
  email: string = "";
  password: string = "";
  lastName: string = "";
  firstName: string = "";

  confirmEmail: string = "";
  confirmPassword: string = "";


  constructor(
    private authService: AuthService,
    private router: Router
  ){}

  register() {
    const registerData: RegisterRequest = {
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
    this.authService.register(registerData).subscribe(handler);
    //
  }

}
