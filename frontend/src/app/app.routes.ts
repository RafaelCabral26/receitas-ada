import { Routes } from '@angular/router';
import { RecipeList } from './recipes/components/recipe-list/recipe-list';
import { MyRecipesComponent } from './recipes/components/my-recipes/my-recipes.component';
import { authGuard } from './auth/guards/auth.guard';

export const routes: Routes = [
  // Rotas públicas
  { path: 'recipes', component: RecipeList },

  // Rotas protegidas
  {
    path: 'my-recipes',
    component: MyRecipesComponent,
    canActivate: [authGuard] // Aplica o guarda de rota aqui
  },

  // Redirecionamento padrão
  { path: '', redirectTo: '/recipes', pathMatch: 'full' },
];