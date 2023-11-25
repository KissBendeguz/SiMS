import { Component, HostListener, OnInit } from '@angular/core';
import { User } from 'src/app/models/user';
import { AuthService } from 'src/app/services/auth.service';
import { UserService } from 'src/app/services/user.service';


@Component({
  selector: 'app-navbar',
  templateUrl: './navbar.component.html',
  styleUrls: ['./navbar.component.scss']
})
export class NavbarComponent implements OnInit {
  authenticatedUser:User = new User();
  constructor(
    public authService: AuthService,
    private userService: UserService
  ){}
  ngOnInit(): void {
    this.userService.getAuthenticatedUser().subscribe(
      (user: User) => {
        this.authenticatedUser = user;
      },
      (error) => {
        console.error('Error fetching authenticated user:', error);
      }
    );
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

  test(){
    alert("teszt");
  }

}
