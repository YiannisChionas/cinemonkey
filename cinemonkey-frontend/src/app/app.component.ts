import { Component } from '@angular/core';
import { RouterModule, Router } from '@angular/router'; // Import RouterModule
import { MatIconModule } from '@angular/material/icon'
import { CommonModule } from '@angular/common';
import { PLATFORM_ID } from '@angular/core';
import { Injectable,Inject } from '@angular/core';
import { isPlatformBrowser } from '@angular/common';
import { KeycloakService } from './core/keycloak/keycloak.service';
import { EnvService } from './core/services/env.service';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [RouterModule, MatIconModule, CommonModule], // Add RouterModule here
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent {
  isLoggedIn = false;
  userEmail: string | null = null;
  apiUrl : string;


  constructor(
    private keycloakService: KeycloakService,
    private router: Router,
    private envService : EnvService,
    @Inject(PLATFORM_ID) private platformId: Object
  ) {
    this.apiUrl=this.envService.apiUrl;
  }

  async ngOnInit(): Promise<void> {
    if (this.isBrowser()) {
      await this.keycloakService.init();
      this.isLoggedIn = this.keycloakService.isAuthenticated();
      this.userEmail = this.keycloakService.getUsername() || null;
      console.log('User email:', this.userEmail);
    }
  }

  get loggedIn(): boolean {
    return this.keycloakService.isAuthenticated();
  }

  async logout(): Promise<void> {
    this.keycloakService.logout();
  }

  async personal(): Promise<void> {
    this.keycloakService.accountManagement();
  }

  private isBrowser(): boolean {
    return isPlatformBrowser(this.platformId);
  }

  get isAdmin(): boolean {
  const roles = JSON.parse(localStorage.getItem('roles') || '[]');
  return roles.includes('ADMIN')||roles.includes('EMPLOYER');
}
}
