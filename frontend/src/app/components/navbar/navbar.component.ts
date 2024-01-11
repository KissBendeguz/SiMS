import { Component, HostListener, OnInit } from '@angular/core';
import { ActivatedRoute, NavigationEnd, Router } from '@angular/router';
import { filter, take } from 'rxjs';
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
    private router:Router,
    private activatedRoute:ActivatedRoute
    ) { }

  loadAuthenticatedUser(){
    this.userService.authenticatedUser$.subscribe(user => {
      this.authenticatedUser = user;
    });
  }
    
  ngOnInit(): void {

    this.router.events.pipe(
      filter(event => event instanceof NavigationEnd)
      ).subscribe(() => {
        let currentRoute = this.router.routerState.snapshot.url
        if(!(currentRoute==='/login' || currentRoute==='/register')){
          this.loadAuthenticatedUser();
        }
      });
  }

  @HostListener('document:click', ['$event'])
  handleDocumentClick(event: MouseEvent | TouchEvent): void {
    const target = event.target as HTMLElement;

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
