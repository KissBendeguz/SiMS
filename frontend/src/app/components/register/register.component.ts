import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { catchError, tap } from 'rxjs';
import { Gender } from 'src/app/models/gender';
import { AuthService, RegisterRequest } from 'src/app/services/auth.service';

@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.scss']
})
export class RegisterComponent {
  email: string = "";
  password: string = "";
  confirmPassword: string = "";
  lastName: string = "";
  firstName: string = "";

  genderOptions = Object.values(Gender);
  selectedGender: Gender = Gender.UNKNOWN;



  constructor(
    private authService: AuthService,
    private router: Router
  ){}

  register() {
    const registerData: RegisterRequest = {
      email: this.email,
      password: this.password,
      firstname: this.firstName,
      lastname: this.lastName,
      gender: this.selectedGender
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
