import { HttpErrorResponse } from '@angular/common/http';
import { Component } from '@angular/core';
import { FormBuilder, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { TranslateService } from '@ngx-translate/core';
import { User } from 'src/app/models/user';
import { AuthService, LoginRequest } from 'src/app/services/auth.service';
import { HttpErrorService } from 'src/app/services/http-error.service';
import { UserService } from 'src/app/services/user.service';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss']
})
export class LoginComponent {
  errorMessage: string;



  constructor(
    private authService: AuthService,
    private userService: UserService,
    private router: Router,
    private httpErrorService: HttpErrorService,
    private formBuilder: FormBuilder,
    private translateService: TranslateService,

  ) {
    this.userService.authenticatedUser$.subscribe({
      next: (user: User | null) => {
        if (user !== null) {
          this.router.navigate(['/']);
        }
      },
    })
    this.httpErrorService.error$.subscribe((error: HttpErrorResponse | null) => {
      switch (error?.status) {
        case 403:
          this.translateService.get("errors.403").subscribe(
            (translation:string) => this.errorMessage = translation,
         );
          break;
        case 500:
        case 501:
        case 502:
        case 503:
        case 504:
          this.translateService.get("errors.500").subscribe(
            (translation:string) => this.errorMessage = translation,
         );
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

  login() {
    if (!this.loginForm.valid) {
      Object.keys(this.loginForm.controls).forEach(key => {
        this.loginForm.controls[key].markAsTouched();
      });
      this.loginForm.updateValueAndValidity();
      return;
    }
    const loginData: LoginRequest = {
      email: this.email!.value,
      password: this.password!.value
    }

    this.authService.login(loginData).subscribe({
      next: () => this.router.navigate(['/']),
    });
  }
}
