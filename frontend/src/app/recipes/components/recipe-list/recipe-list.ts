import { Component, inject, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Recipe } from '../../models/recipe.model';
import { RecipeService } from '../../services/recipe.service';
import { AuthService } from '../../../auth/services/auth.service'; // Import AuthService
import { Subject } from 'rxjs'; // Import Subject
import { startWith, switchMap } from 'rxjs/operators'; // Import operators

@Component({
  selector: 'app-recipe-list',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './recipe-list.html',
  styleUrls: ['./recipe-list.scss']
})
export class RecipeList implements OnInit {
  recipes: Recipe[] = [];
  private recipeService = inject(RecipeService);
  private authService = inject(AuthService); // Inject AuthService
  private refresh$ = new Subject<void>(); // Add refresh$ subject

  ngOnInit(): void {
    this.refresh$.pipe(
      startWith(null), // Trigger initial fetch
      switchMap(() => this.recipeService.getRecipes())
    ).subscribe({
      next: (data) => {
        this.recipes = data;
        console.log('Receitas carregadas:', this.recipes);
        // --- DIAGNOSTIC LOGGING (TEMPORARY) ---
        this.recipes.forEach(recipe => {
          console.log(`Recipe ID: ${recipe.id}, hasVoted: ${recipe.hasVoted}, totalVotos: ${recipe.totalVotos}`);
        });
        // --- END DIAGNOSTIC LOGGING ---
      },
      error: (err) => {
        console.error('Erro ao carregar receitas:', err);
      }
    });
  }

  onVote(recipeId: number): void {
    // Check if user is logged in
    if (!this.authService.getToken()) { // Using getToken as a simple check for login status
      alert('Você precisa estar logado para votar!');
      return;
    }

    this.recipeService.vote(recipeId).subscribe({
      next: () => {
        this.refresh$.next(); // Refresh recipes after successful vote
      },
      error: (err) => {
        console.error('Erro ao votar:', err);
        let errorMessage = 'Ocorreu um erro ao registrar seu voto.';
        if (err.status === 403) {
          errorMessage = 'Você não pode votar em sua própria receita.';
        } else if (err.status === 409) {
          errorMessage = 'Você já votou nesta receita.';
        }
        alert(errorMessage);
      }
    });
  }

  onRemoveVote(recipeId: number): void {
    // Check if user is logged in
    if (!this.authService.getToken()) {
      alert('Você precisa estar logado para remover seu voto!');
      return;
    }

    this.recipeService.removeVote(recipeId).subscribe({
      next: () => {
        this.refresh$.next(); // Refresh recipes after successful vote removal
      },
      error: (err) => {
        console.error('Erro ao remover voto:', err);
        let errorMessage = 'Ocorreu um erro ao remover seu voto.';
        if (err.status === 404) {
          errorMessage = 'Voto não encontrado para remoção.';
        }
        alert(errorMessage);
      }
    });
  }
}

