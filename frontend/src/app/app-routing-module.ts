import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { RegisterComponent } from './auth/components/register/register';
import { RecipeList } from './recipes/components/recipe-list/recipe-list';

const routes :Routes = [
  { path: 'register', component: RegisterComponent },
  { path: 'recipes', component: RecipeList },
  { path: '', redirectTo: '/recipes', pathMatch: 'full' }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
