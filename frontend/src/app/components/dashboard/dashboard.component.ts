import { Component, OnInit } from '@angular/core';
import { Business } from 'src/app/models/business';
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
  authenticatedUser: User | null = new User();
  selectedBusiness: Business | null = new Business();
  
  associatedBusinesses:Set<Business> | null = new Set<Business>();
  
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
        let sortedArray = Array.from(businesses).sort((a, b) => a.name.localeCompare(b.name));
        this.associatedBusinesses = new Set(sortedArray);
        this.selectedBusiness = sortedArray[0];
        console.log(this.selectedBusiness);
      });
    }
    
    onListItemClick(business:Business) {
      this.selectedBusiness = business;
    }
}
