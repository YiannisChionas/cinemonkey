import { AppComponent } from './app/app.component';
import { importProvidersFrom } from '@angular/core';
import { provideHttpClient, withInterceptors } from '@angular/common/http';
import { provideRouter } from '@angular/router';
import { routes } from './app/app.routes';
import { bootstrapApplication } from '@angular/platform-browser';
import { authInterceptor } from './app/core/interceptors/auth.interceptor';
import { KeycloakService } from './app/core/keycloak/keycloak.service';
import { EnvService } from './app/core/services/env.service';
import { createApplication } from '@angular/platform-browser';

async function main() {
  const app = await createApplication({
    providers: [
      provideHttpClient(withInterceptors([authInterceptor])),
      provideRouter(routes),
      EnvService,
      KeycloakService
    ]
  });

  const keycloak = app.injector.get(KeycloakService);
  
  const authenticated = await keycloak.init();
  if (!authenticated) {
    console.warn('[MAIN] Not authenticated. Redirecting to login...');
    await keycloak.login();
    return;
  }

  console.log('[MAIN] Authenticated. Bootstrapping app...');
  await bootstrapApplication(AppComponent, {
    providers: [
      provideHttpClient(withInterceptors([authInterceptor])),
      provideRouter(routes),
      { provide: KeycloakService, useValue: keycloak }
    ]
  });
}

main().catch(err => {
  console.error('[MAIN] Unexpected error during app bootstrap', err);
});
