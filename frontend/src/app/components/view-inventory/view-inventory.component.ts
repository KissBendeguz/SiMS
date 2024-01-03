import { Component } from '@angular/core';
import { Form, FormArray, FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { Business } from 'src/app/models/business';
import { Inventory } from 'src/app/models/inventory';
import { Product } from 'src/app/models/product';
import { User } from 'src/app/models/user';
import { AuthService } from 'src/app/services/auth.service';
import { BusinessService } from 'src/app/services/business.service';
import { InventoryService } from 'src/app/services/inventory.service';
import { ProductService } from 'src/app/services/product.service';
import { UserService } from 'src/app/services/user.service';

@Component({
  selector: 'app-view-inventory',
  templateUrl: './view-inventory.component.html',
  styleUrls: ['./view-inventory.component.scss']
})
export class ViewInventoryComponent {
  authenticatedUser: User | null;
  business: Business | null;
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
      this.businessService.getBusiness(this.businessId).subscribe((business: Business) => {
        this.business = business;

        console.log('inventory get')
        this.inventoryService.getInventory(this.inventoryId).subscribe((inventory: Inventory) => {
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
    private productService: ProductService,
    private router: Router,
    private route: ActivatedRoute,
    private fb: FormBuilder
  ) {
    this.dynamicForm = this.fb.nonNullable.group({
      productName: ['', Validators.required],
      itemNumber: ['', Validators.required],
      category: ['', Validators.required],
      quantity: ['', Validators.required],
      unit: ['', [Validators.required]],

      dynamicProperties: this.fb.array([]),
    }, { updateOn: 'blur' });
    this.addProperty();
  }

  get name() {
    return this.dynamicForm.get('productName')
  }
  get itemNumber() {
    return this.dynamicForm.get('itemNumber')
  }
  get category() {
    return this.dynamicForm.get('category')
  }
  get quantity() {
    return this.dynamicForm.get('quantity')
  }
  get unit() {
    return this.dynamicForm.get('unit')
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
    const dynProperties = formData.dynamicProperties.reduce((acc, property) => {
      acc[property.key] = property.value;
      return acc;
    }, {});
    const newProduct = new Product();
    newProduct.name = this.name?.value;
    newProduct.itemNumber = this.itemNumber?.value;
    newProduct.category = this.category?.value;
    newProduct.quantity = this.quantity?.value;
    newProduct.unit = this.unit?.value;
    newProduct.dynProperties = dynProperties;
    console.log(JSON.stringify(newProduct));
    this.productService.createProduct(this.inventoryId, newProduct).subscribe();
  }
}
