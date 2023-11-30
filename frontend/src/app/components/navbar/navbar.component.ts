import { Component, HostListener, OnInit } from '@angular/core';
import { NavigationEnd, Router } from '@angular/router';
import { filter } from 'rxjs';
import { User } from 'src/app/models/user';
import { AuthService } from 'src/app/services/auth.service';
import { UserService } from 'src/app/services/user.service';


@Component({
  selector: 'app-navbar',
  templateUrl: './navbar.component.html',
  styleUrls: ['./navbar.component.scss']
})
export class NavbarComponent implements OnInit {
  authenticatedUser: User | null;
  constructor(
    private authService: AuthService,
    private userService: UserService,
    private router:Router
    ) { }

  loadAuthenticatedUser(){
    console.log("loadUser")
    this.userService.authenticatedUser$.subscribe(user => {
      this.authenticatedUser = user;
    });
  }
    
  ngOnInit(): void {
    //this.loadAuthenticatedUser();
    this.router.events.pipe(
      filter(event => event instanceof NavigationEnd)
    ).subscribe(() => {
      this.loadAuthenticatedUser();
    });
  }

  @HostListener('document:click', ['$event'])
  handleDocumentClick(event: MouseEvent): void {
    const target = event.target as HTMLElement;

    // Check if the clicked element is a dropdown button
    const isDropdownButton = target.matches('[data-dropdown-button]');

    if (!isDropdownButton && target.closest('[data-dropdown]') !== null) {
      return;
    }

    let currentDropdown: HTMLElement | null;

    if (isDropdownButton) {
      currentDropdown = target.closest('[data-dropdown]') as HTMLElement;
      currentDropdown.classList.toggle('active');
    }

    document.querySelectorAll('[data-dropdown].active').forEach((dropdown) => {
      if (dropdown === currentDropdown) {
        return;
      }
      dropdown.classList.remove('active');
    });
  }

  logout() {
    this.authenticatedUser = null;
    this.userService.logout();
  }

}
