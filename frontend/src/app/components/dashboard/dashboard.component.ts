import { Component, OnInit } from '@angular/core';
import { User } from 'src/app/models/user';
import { UserService } from 'src/app/services/user.service';

@Component({
  selector: 'app-dashboard',
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.scss']
})
export class DashboardComponent implements OnInit {
  authenticatedUser:User;
  constructor(private userService:UserService){}
  ngOnInit(): void {
    this.userService.getAuthenticatedUser().subscribe(
      (user: User) => {
        this.authenticatedUser = user;
        console.log(this.authenticatedUser)
      },
      (error) => {
        console.error('Error fetching authenticated user:', error);
      }
    );
  }

}
