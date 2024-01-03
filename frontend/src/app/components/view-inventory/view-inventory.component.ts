import { Component } from '@angular/core';
import { Form, FormArray, FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { Business } from 'src/app/models/business';
import { Inventory } from 'src/app/models/inventory';
import { User } from 'src/app/models/user';
import { AuthService } from 'src/app/services/auth.service';
import { BusinessService } from 'src/app/services/business.service';
import { InventoryService } from 'src/app/services/inventory.service';
import { UserService } from 'src/app/services/user.service';

@Component({
  selector: 'app-view-inventory',
  templateUrl: './view-inventory.component.html',
  styleUrls: ['./view-inventory.component.scss']
})
export class ViewInventoryComponent {
  authenticatedUser: User | null;
  business: Business | null ;
  inventory: Inventory | null;
  
  businessId: number;
  inventoryId: number

  dynamicForm: FormGroup;

  ngOnInit(): void {
    const businessIdParam = this.route.snapshot.params['businessId'];
    const inventoryIdParam = this.route.snapshot.params['inventoryId'];
    
    if (!isNaN(Number(businessIdParam)) || !isNaN(Number(inventoryIdParam))) {
      this.businessId = Number(businessIdParam);
      this.inventoryId = Number(inventoryIdParam);
    } else {
      this.router.navigate(['/']);
      return;
    }
    this.userService.authenticatedUser$.subscribe(user => {
      this.authenticatedUser = user;
      console.log('business get')
      this.businessService.getBusiness(this.businessId).subscribe((business:Business) => {
        this.business = business;
    
        console.log('inventory get')
        this.inventoryService.getInventory(this.inventoryId).subscribe((inventory:Inventory) => {
          this.inventory = inventory;
        });
    });
    
      
    });

  }
  
  constructor(
    private authService: AuthService,
    private userService: UserService,
    private inventoryService: InventoryService,
    private businessService: BusinessService,
    private router: Router,
    private route: ActivatedRoute,
    private fb: FormBuilder
  ) {
    this.dynamicForm = this.fb.nonNullable.group({
      productName: ['', Validators.required],
      itemNumber: ['',Validators.required],
      category: ['', Validators.required],
      quantity: ['', Validators.required],
      unit: ['', [Validators.required]],

      dynamicProperties: this.fb.array([]),
    },{ updateOn: 'blur' });
    this.addProperty();
  }



  get dynamicProperties() {
    return this.dynamicForm.get('dynamicProperties') as FormArray;
  }

  addProperty() {
    const propertyGroup = this.fb.group({
      key: ['', Validators.required],
      value: ['', Validators.required],
    });

    this.dynamicProperties.push(propertyGroup);
  }

  removeProperty(index: number) {
    this.dynamicProperties.removeAt(index);
  }

  onSubmit() {
    const formData = this.dynamicForm.value;
    console.log(formData);
  }
}
