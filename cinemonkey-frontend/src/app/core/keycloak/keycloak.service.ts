import { Injectable } from '@angular/core';
import Keycloak, { KeycloakInstance } from 'keycloak-js';
import { UserProfile } from './user-profile';
import { EnvService } from '../services/env.service';

@Injectable({ providedIn: 'root' })
export class KeycloakService {
  private keycloak!: KeycloakInstance;
  private _profile?: UserProfile;
  
  constructor(private envService: EnvService) {} // ✅ inject το envService

  get userProfile(): UserProfile | undefined {
    return this._profile;
  }

  async init(): Promise<boolean> {
    this.keycloak = new Keycloak({
      url: this.envService.keycloakUrl,
      realm: 'cine-monkey',
      clientId: 'cm'
    });

    try {
      const authenticated = await this.keycloak.init({
        onLoad: 'check-sso',
        silentCheckSsoRedirectUri: `${window.location.origin}/assets/silent-check-sso.html`,
        checkLoginIframe: false
      });

      console.log('[KEYCLOAK] authenticated:', authenticated);

      if (authenticated) {
        await this.keycloak.updateToken(0); // ensure fresh token
        this._profile = await this.keycloak.loadUserProfile() as UserProfile;

        localStorage.setItem('authToken', this.keycloak.token || '');
        localStorage.setItem('username', this.getUsername() || '');
        localStorage.setItem('userSub', this.getUserSub() || '');
        localStorage.setItem('roles', JSON.stringify(this.getUserRoles()));

        setInterval(() => {
          this.keycloak.updateToken(60).then(refreshed => {
            if (refreshed) {
              localStorage.setItem('authToken', this.keycloak.token || '');
              console.log('[KEYCLOAK] Token refreshed');
            }
          }).catch(err => {
            console.error('[KEYCLOAK] Failed to refresh token', err);
          });
        }, 6000);
      }

      return authenticated;
    } catch (err) {
      console.error('[KEYCLOAK] init error', err);
      return false;
    }
  }

  login(): Promise<void> {
    return this.keycloak.login();
  }

  logout(): void {
    this.keycloak.logout({ redirectUri: window.location.origin });
  }

  getToken(): string | undefined {
    return this.keycloak?.token ?? localStorage.getItem('authToken') ?? undefined;
  }

  isAuthenticated(): boolean {
    return this.keycloak?.authenticated ?? false;
  }

  isTokenExpired(): boolean {
    return this.keycloak?.isTokenExpired() ?? true;
  }

  accountManagement(): void {
    this.keycloak.accountManagement();
  }

  getUsername(): string | undefined {
    return this.keycloak?.tokenParsed?.['preferred_username'];
  }

  getKeycloakInstance(): KeycloakInstance {
    return this.keycloak;
  }
  getUserSub(): string | undefined {
    return this.keycloak?.tokenParsed?.sub;
  }
  getUserRoles(): string[] {
    return this.keycloak?.tokenParsed?.resource_access?.['cm']?.roles || [];
  }


}
