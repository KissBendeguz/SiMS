import { Component } from '@angular/core';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { Inventory } from 'src/app/models/inventory';
import { AuthService } from 'src/app/services/auth.service';
import { InventoryService } from 'src/app/services/inventory.service';

@Component({
  selector: 'app-add-employee',
  templateUrl: './add-employee.component.html',
  styleUrls: ['./add-employee.component.scss']
})

export class AddEmployeeComponent {
  businessId: number;

  ngOnInit(): void {
    const businessIdParam = this.route.snapshot.params['businessId'];

    if (!isNaN(Number(businessIdParam))) {
      this.businessId = Number(businessIdParam);
    } else {
      this.router.navigate(['/']);
    }
  }


  constructor(
    private authService: AuthService,
    private inventoryService: InventoryService,
    private router: Router,
    private route: ActivatedRoute,
    private formBuilder: FormBuilder
  ) {}



  createForm = this.formBuilder.nonNullable.group({
 //   name: ['', [Validators.required, Validators.pattern(/^[^0-9_!¡?÷?¿/\\+=@#$%ˆ&*(){}|~<>;:[\]]{2,}$/)]],
    address: ['', [Validators.required]],
    name: ['', [Validators.required, Validators.pattern(/^[^0-9_!¡?÷?¿/\\+=@#$%ˆ&*(){}|~<>;:[\]]{2,}$/)]],
    email: ['', [Validators.required, Validators.email]],
    phone: ['',Validators.required],
    placeofbirth: ['', [Validators.required]],
    dateofbirth: ['', [Validators.required]],
    homeaddress: ['', [Validators.required]],
    citizenship: ['', [Validators.required]],
    identitycardnumber: ['', [Validators.required]],
    socialsecuritynumber: ['', [Validators.required]],

  }, { updateOn: 'blur' });

  //get name() { return this.createForm.get('name') }
  get address() { return this.createForm.get('address') }
  get name() { return this.createForm.get('name') }
  get email() { return this.createForm.get('email') }
  get phone() { return this.createForm.get('phone') }
  get placeofbirth() { return this.createForm.get('placeofbirth') }
  get dateofbirth() { return this.createForm.get('dateofbirth') }
  get homeaddress() { return this.createForm.get('homeaddress') }
  get citizenship() { return this.createForm.get('citizenship') }
  get identitycardnumber() { return this.createForm.get('identitycardnumber') }
  get socialsecuritynumber() { return this.createForm.get('socialsecuritynumber') }

  create() {
    if (!this.createForm.valid) {
      Object.keys(this.createForm.controls).forEach(key => {
        this.createForm.controls[key].markAsTouched();
      });
      this.createForm.updateValueAndValidity();
      return;
    }
  }
}