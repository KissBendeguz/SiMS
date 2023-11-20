import { Component } from '@angular/core';
import { AuthService,RegisterRequest } from 'src/app/services/auth.service';

@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.scss']
})
export class RegisterComponent {
  email:string="";
  password:string="";

  constructor(private authService:AuthService){}

  register(){
    const registerData:RegisterRequest = {
      email:this.email,
      password:this.password
    }

    this.authService.register(registerData).subscribe();


    console.log("register")
  }
}
