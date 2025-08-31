import { inject } from '@angular/core';
import { CanActivateFn, Router, UrlTree } from '@angular/router';
import { Observable } from 'rxjs';
import { map, take } from 'rxjs/operators';
import { AuthService } from '../services/auth.service';

export const authGuard: CanActivateFn = (): Observable<boolean | UrlTree> => {
  const authService = inject(AuthService);
  const router = inject(Router);

  return authService.isLoggedIn$.pipe(
    take(1), // Pega o valor mais recente do estado de login e completa.
    map(isLoggedIn => {
      if (isLoggedIn) {
        // Se estiver logado, permite o acesso.
        return true;
      }
      // Se não estiver logado, redireciona para a página inicial.
      return router.createUrlTree(['/recipes']);
    })
  );
};
