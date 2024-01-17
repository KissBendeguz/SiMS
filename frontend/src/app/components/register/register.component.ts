import { HttpErrorResponse } from '@angular/common/http';
import { Component } from '@angular/core';
import { AbstractControl, FormBuilder, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { TranslateService } from '@ngx-translate/core';
import { catchError, tap } from 'rxjs';
import { Gender } from 'src/app/models/gender';
import { User } from 'src/app/models/user';
import { AuthService, RegisterRequest } from 'src/app/services/auth.service';
import { HttpErrorService } from 'src/app/services/http-error.service';
import { UserService } from 'src/app/services/user.service';

@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.scss']
})
export class RegisterComponent {
  errorMessage:string;
  genderOptions = Object.values(Gender);
  selectedGender: Gender = Gender.UNKNOWN;



  constructor(
    private authService: AuthService,
    private userService:UserService,
    private router: Router,
    private formBuilder: FormBuilder,
    private httpErrorService: HttpErrorService,
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
      switch(error?.status){
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
  
  enumValidator = (enumType: any) => (control: AbstractControl) => {
    const isValid = Object.values(enumType).includes(control.value as string);
    return isValid ? null : { invalidEnumValue: true };
  };
  confirmPasswordValidator = (control: AbstractControl) => {
    return control.value === this.password!.value ? null : { mismatchedPasswords: true };
  };
  genderValidator = (control: AbstractControl) => {
    return control.value != Gender.UNKNOWN ? null : { unknownGender: true }
  };
  registerForm = this.formBuilder.nonNullable.group({
    email: ['', [Validators.required, Validators.email]],
    firstName: ['', [Validators.required, Validators.pattern(/^[^0-9_!¡?÷?¿/\\+=@#$%ˆ&*(){}|~<>;:[\]]{2,}$/)]],
    lastName: ['', [Validators.required, Validators.pattern(/^[^0-9_!¡?÷?¿/\\+=@#$%ˆ&*(){}|~<>;:[\]]{2,}$/)]],
    password: ['', [Validators.required, Validators.pattern("^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{8,}$")]],
    gender: [Gender.UNKNOWN, [Validators.required, this.enumValidator, this.genderValidator]],
    confirmPassword: ['', [Validators.required]],
    privacyPolicy: [false, Validators.requiredTrue]
  }, { updateOn: 'blur' });

  get email() { return this.registerForm.get('email') }
  get firstName() { return this.registerForm.get('firstName') }
  get lastName() { return this.registerForm.get('lastName') }
  get password() { return this.registerForm.get('password') }
  get gender() { return this.registerForm.get('gender') }
  get confirmPassword() { return this.registerForm.get('confirmPassword') }
  get privacyPolicy() { return this.registerForm.get('privacyPolicy') }
  
  updateConfirmPasswordValidators() {
    this.registerForm.controls.confirmPassword.setValidators([Validators.required, this.confirmPasswordValidator]);
    this.registerForm.controls.confirmPassword.updateValueAndValidity();
  }

  register() {
    if(!this.registerForm.valid){
      Object.keys(this.registerForm.controls).forEach(key => {
        this.registerForm.controls[key].markAsTouched();
      });
      this.registerForm.updateValueAndValidity();
      return;
    }
    const registerData: RegisterRequest = {
      email: this.email!.value,
      password: this.password!.value,
      firstname: this.firstName!.value,
      lastname: this.lastName!.value,
      gender: this.gender!.value
    }

    this.authService.register(registerData).subscribe({
      next: () => this.router.navigate(['/']),
    });
    //
  }

}
