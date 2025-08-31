import { ApplicationConfig, importProvidersFrom, provideBrowserGlobalErrorListeners } from '@angular/core';
import { provideRouter } from '@angular/router';
import { provideHttpClient, withFetch, withInterceptorsFromDi, HTTP_INTERCEPTORS } from '@angular/common/http';
import { provideAnimationsAsync } from '@angular/platform-browser/animations/async';

import { routes } from './app.routes';
import { CoreModule } from './core/core-module';
import { RecipesModule } from './recipes/recipes-module';
import { AuthInterceptor } from './auth/auth.interceptor';

export const appConfig: ApplicationConfig = {
  providers: [
    // Configura as rotas da aplicação
    provideRouter(routes),

    // Mantém o provider que você já tinha no AppModule
    provideBrowserGlobalErrorListeners(),

    // `importProvidersFrom` é a forma moderna de usar providers
    // de NgModules existentes em uma aplicação standalone.
    importProvidersFrom(CoreModule, RecipesModule),

    // Adiciona providers modernos para HttpClient e Animações
    provideHttpClient(withFetch(), withInterceptorsFromDi()), // Enable DI for interceptors
    provideAnimationsAsync(),

    // Register the AuthInterceptor
    { provide: HTTP_INTERCEPTORS, useClass: AuthInterceptor, multi: true },
  ],
};