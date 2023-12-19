import { Component, OnInit } from '@angular/core';
import { Observable, of } from 'rxjs';
import { Business } from 'src/app/models/business';
import { Inventory } from 'src/app/models/inventory';
import { User } from 'src/app/models/user';
import { AuthService } from 'src/app/services/auth.service';
import { BusinessService } from 'src/app/services/business.service';
import { UserService } from 'src/app/services/user.service';

@Component({
  selector: 'app-dashboard',
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.scss']
})
export class DashboardComponent implements OnInit {
  authenticatedUser: User | null;
  selectedBusiness: Business | null ;
  selectedEmployee: User | null;

  inventories: Set<Inventory> = new Set<Inventory>();
  associatedBusinesses:Set<Business> = new Set<Business>();
  
  constructor(
    private authService: AuthService,
    private userService: UserService,
    private businessService: BusinessService
    ) { }
    
    ngOnInit(): void {
      this.userService.authenticatedUser$.subscribe(user => {
        this.authenticatedUser = user;
      });
      
      this.businessService.getAssociatedBusinesses().subscribe((businesses: Set<Business>) =>{
        let sortedBusinessArray = Array.from(businesses).sort((a, b) => a.name.localeCompare(b.name));
        this.associatedBusinesses = new Set(sortedBusinessArray);
        this.selectedBusiness = sortedBusinessArray[0];

        if(this.selectedBusiness){
          this.businessService.getBusinessInventories(this.selectedBusiness.id).subscribe((inventories: Set<Inventory>) => {
            this.inventories = inventories;
            console.log(this.inventories);
          });
        }
        
      });
    }
    
    onBusinessClick(business:Business) {
      this.selectedBusiness = business;
    }
    onEmployeeClick(employee:User){
      this.selectedEmployee = this.selectedEmployee === employee ? null : employee;
    }
}
