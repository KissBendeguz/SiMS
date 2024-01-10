import { Component, OnInit } from '@angular/core';
import { Observable, of } from 'rxjs';
import { Business } from 'src/app/models/business';
import { Inventory } from 'src/app/models/inventory';
import { Product } from 'src/app/models/product';
import { User } from 'src/app/models/user';
import { AuthService } from 'src/app/services/auth.service';
import { BusinessService } from 'src/app/services/business.service';
import { InventoryService } from 'src/app/services/inventory.service';
import { UserService } from 'src/app/services/user.service';

@Component({
  selector: 'app-dashboard',
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.scss']
})
export class DashboardComponent implements OnInit {
  authenticatedUser: User | null;
  selectedBusiness: Business | null;
  selectedBusinessIndex: number;
  selectedEmployee: User | null;

  inventories: Set<Inventory> = new Set<Inventory>();
  associatedBusinesses: Set<Business> = new Set<Business>();

  constructor(
    private authService: AuthService,
    private userService: UserService,
    private businessService: BusinessService,
    private inventoryService: InventoryService
  ) { }

  ngOnInit(): void {
    this.userService.authenticatedUser$.subscribe(user => {
      this.authenticatedUser = user;
    });

    this.fetchData();
  }

  private fetchData(){
    this.businessService.getAssociatedBusinesses().subscribe((businesses: Set<Business>) => {
      console.log(businesses)

      //sort
      this.associatedBusinesses = new Set([...businesses].sort((a, b) => a.name.localeCompare(b.name)));

      //set first as selected
      this.selectedBusiness = [...this.associatedBusinesses][0];

      //remove owner from list
      const updatedBusinesses = new Set(this.associatedBusinesses);
      this.associatedBusinesses.forEach(business => {
        business.associates = new Set([...business.associates].filter(associate => associate.id !== business.owner.id));

        if (business.associates.size > 0) {
          updatedBusinesses.add(business);
        }
      });
      this.associatedBusinesses = updatedBusinesses;

      if(this.associatedBusinesses.size>0){
        this.fetchInventories();
      }

    });
  }

  private fetchInventories(){
    this.businessService.getBusinessInventories(this.selectedBusiness!.id).subscribe((inventories: Set<Inventory>) => {
      this.inventories = new Set<Inventory>(inventories);
      this.inventories.forEach(inventory=>{
        inventory.products = new Set<Product>(inventory.products);
      })
    });
  }
  onBusinessClick(business: Business) {
    this.selectedBusiness = business;
    this.fetchInventories();
  }

  onEmployeeClick(employee: User) {
    this.selectedEmployee = this.selectedEmployee === employee ? null : employee;
  }


  removeEmployee(employee: User){
    this.businessService.removeEmployee(this.selectedBusiness!.id,employee.id).subscribe({next:() => {
      this.selectedEmployee = null;
      this.fetchData();
    }});
  }
  deleteSelectedBusiness(){
    this.businessService.deleteBusiness(this.selectedBusiness!.id).subscribe({next:() => {
      this.fetchData();
    }})
  }
  deleteInventory(inventory:Inventory){
    this.inventoryService.deleteInventory(inventory.id).subscribe({next:()=>{
      this.fetchInventories();
    }})
  }
}
