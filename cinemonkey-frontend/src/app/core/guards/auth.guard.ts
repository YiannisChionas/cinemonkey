import { Injectable } from '@angular/core';
import { CanActivate, Router } from '@angular/router';
import { KeycloakService } from '../keycloak/keycloak.service';

@Injectable({ providedIn: 'root' })
export class AuthGuard implements CanActivate {

  constructor(private keycloakService: KeycloakService, private router: Router) {}

  canActivate(): boolean {
    const token = this.keycloakService.getToken();
    const expired = this.keycloakService.isTokenExpired();

    if (!token || expired) {
      console.warn('[AuthGuard] Not authenticated or token expired. Redirecting to login.');
      this.keycloakService.login(); // ή this.router.navigate(['/login']);
      return false;
    }

    return true;
  }
}
