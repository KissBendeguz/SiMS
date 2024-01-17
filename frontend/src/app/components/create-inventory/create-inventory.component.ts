import { HttpErrorResponse } from '@angular/common/http';
import { Component } from '@angular/core';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { Inventory } from 'src/app/models/inventory';
import { AuthService } from 'src/app/services/auth.service';
import { HttpErrorService } from 'src/app/services/http-error.service';
import { InventoryService } from 'src/app/services/inventory.service';

@Component({
  selector: 'app-create-inventory',
  templateUrl: './create-inventory.component.html',
  styleUrls: ['./create-inventory.component.scss']
})
export class CreateInventoryComponent {
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
    name: ['', [Validators.required, Validators.pattern(/^[^0-9_!¡?÷?¿/\\+=@#$%ˆ&*(){}|~<>;:[\]]{2,}$/)]],
    address: ['', [Validators.required]],
    managerName: ['', [Validators.required, Validators.pattern(/^[^0-9_!¡?÷?¿/\\+=@#$%ˆ&*(){}|~<>;:[\]]{2,}$/)]],
    managerEmail: ['', [Validators.required, Validators.email]],
    managerPhone: ['',[Validators.required, Validators.pattern(/^\+(?:[0-9] ?){6,14}[0-9]$/)]]
  }, { updateOn: 'blur' });

  get name() { return this.createForm.get('name') }
  get address() { return this.createForm.get('address') }
  get managerName() { return this.createForm.get('managerName') }
  get managerEmail() { return this.createForm.get('managerEmail') }
  get managerPhone() { return this.createForm.get('managerPhone') }


  create() {
    if (!this.createForm.valid) {
      Object.keys(this.createForm.controls).forEach(key => {
        this.createForm.controls[key].markAsTouched();
      });
      this.createForm.updateValueAndValidity();
      return;
    }

    let inventory = new Inventory();
    inventory.name = this.name!.value;
    inventory.address = this.address!.value;
    inventory.managerName = this.managerName!.value;
    inventory.managerEmail = this.managerEmail!.value;
    inventory.managerPhone = this.managerPhone!.value;
    
    this.inventoryService.createInventory(this.businessId,inventory).subscribe({
      next: () => this.router.navigate(['/']),
    });
  }
}
