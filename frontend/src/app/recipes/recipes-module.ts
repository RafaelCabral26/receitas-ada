import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RecipeList } from './components/recipe-list/recipe-list';

@NgModule({
  declarations: [],
  imports: [
    CommonModule,
    RecipeList
  ],
  exports: [
    RecipeList
  ]
})
export class RecipesModule { }

