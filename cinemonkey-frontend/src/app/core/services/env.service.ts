import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root' // global διαθέσιμο
})
export class EnvService {
  public apiUrl: string;
  public keycloakUrl: string;

  constructor() {
    const env = (window as any)['env'] || {};
    this.apiUrl = env['apiUrl'] || '';
    this.keycloakUrl = env['keycloakUrl'] || '';
  }
}
