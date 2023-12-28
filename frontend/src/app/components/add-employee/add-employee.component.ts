import { HttpErrorResponse } from '@angular/common/http';
import { Component } from '@angular/core';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { Inventory } from 'src/app/models/inventory';
import { AuthService } from 'src/app/services/auth.service';
import { AddEmployeeRequest, BusinessService } from 'src/app/services/business.service';
import { HttpErrorService } from 'src/app/services/http-error.service';
import { InventoryService } from 'src/app/services/inventory.service';

@Component({
  selector: 'app-add-employee',
  templateUrl: './add-employee.component.html',
  styleUrls: ['./add-employee.component.scss']
})

export class AddEmployeeComponent {
  businessId: number;
  errorMessage: string;

  ngOnInit(): void {
    const businessIdParam = this.route.snapshot.params['businessId'];

    if (!isNaN(Number(businessIdParam))) {
      this.businessId = Number(businessIdParam);
    } else {
      this.router.navigate(['/']);
    }
    
  }


  constructor(
    private businessService: BusinessService,
    private router: Router,
    private route: ActivatedRoute,
    private formBuilder: FormBuilder,
    private httpErrorService: HttpErrorService
  ) {
    this.httpErrorService.error$.subscribe((error: HttpErrorResponse | null) => {
      switch(error?.status){
        case 403:
          this.errorMessage = "Bad credentials";
          break;
        case 404:
          this.errorMessage = "User is not found with this email address.";
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



  addForm = this.formBuilder.nonNullable.group({
    email: ['', [Validators.required, Validators.email]],
    phone: ['',Validators.required],
    placeofbirth: ['', [Validators.required]],
    dateofbirth: ['', [Validators.required]],
    homeaddress: ['', [Validators.required]],
    citizenship: ['', [Validators.required]],
    identitycardnumber: ['', [Validators.required]],
    socialsecuritynumber: ['', [Validators.required]],

  }, { updateOn: 'blur' });

  get email() { return this.addForm.get('email') }
  get phone() { return this.addForm.get('phone') }
  get placeofbirth() { return this.addForm.get('placeofbirth') }
  get dateofbirth() { return this.addForm.get('dateofbirth') }
  get homeaddress() { return this.addForm.get('homeaddress') }
  get citizenship() { return this.addForm.get('citizenship') }
  get identitycardnumber() { return this.addForm.get('identitycardnumber') }
  get socialsecuritynumber() { return this.addForm.get('socialsecuritynumber') }

  add() {
    console.log(this.addForm.valid)
    if (!this.addForm.valid) {
      Object.keys(this.addForm.controls).forEach(key => {
        this.addForm.controls[key].markAsTouched();
      });
      this.addForm.updateValueAndValidity();
      return;
    }
    const request:AddEmployeeRequest = {
      email: this.email!.value,
      phoneNumber: this.phone!.value,
      placeOfBirth: this.placeofbirth!.value,
      dateOfBirth: new Date(this.dateofbirth!.value),
      homeAddress: this.homeaddress!.value,
      citizenship: this.citizenship!.value,
      identityCardNumber: this.identitycardnumber!.value,
      socialSecurityNumber: this.socialsecuritynumber!.value
    }
    console.log("http kérés")
    this.businessService.addEmployee(this.businessId,request).subscribe({
      next: () => this.router.navigate(['/']),
    });
  }
}