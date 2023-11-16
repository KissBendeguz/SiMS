import { Component, HostListener, OnInit } from '@angular/core';


@Component({
  selector: 'app-navbar',
  templateUrl: './navbar.component.html',
  styleUrls: ['./navbar.component.scss']
})
export class NavbarComponent {
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

}
