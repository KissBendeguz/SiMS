import { Component } from '@angular/core';
import { FormBuilder } from '@angular/forms';
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

  ngOnInit(): void {
    const businessIdParam = this.route.snapshot.params['businessId'];
    const inventoryIdParam = this.route.snapshot.params['inventoryId'];

    if (!isNaN(Number(businessIdParam)) || !isNaN(Number(inventoryIdParam))) {
      this.businessId = Number(businessIdParam);
      this.businessId = Number(inventoryIdParam);
    } else {
      this.router.navigate(['/']);
    }
    this.userService.authenticatedUser$.subscribe(user => {
      this.authenticatedUser = user;
    });
    
    this.businessService.getBusiness(this.businessId).subscribe((business:Business) => {
      this.business = business;
  
      this.inventoryService.getInventory(this.inventoryId).subscribe((inventory:Inventory) => {
        this.inventory = inventory;
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
    private formBuilder: FormBuilder
  ) {}
}
