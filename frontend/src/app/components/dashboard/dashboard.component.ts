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
  authenticatedUser: User | null;
  selectedBusiness: Business | null;

  associatedBusinesses:Set<Business> | null;

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
      this.associatedBusinesses = businesses;
    });
  }

}
