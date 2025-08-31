import { Component, OnInit, inject, ViewChild, ElementRef, OnDestroy, AfterViewInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Observable, Subject } from 'rxjs';
import { startWith, switchMap } from 'rxjs/operators';
import { Recipe } from '../../models/recipe.model';
import { RecipeService } from '../../services/recipe.service';
import { FormBuilder, FormGroup, Validators, ReactiveFormsModule } from '@angular/forms';
import { Modal } from 'bootstrap';

@Component({
  selector: 'app-my-recipes',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './my-recipes.component.html',
  styleUrls: ['./my-recipes.component.scss']
})
export class MyRecipesComponent implements OnInit, AfterViewInit, OnDestroy {
  private recipeService = inject(RecipeService);
  private fb = inject(FormBuilder);

  myRecipes$!: Observable<Recipe[]>;
  recipeForm: FormGroup;
  editForm: FormGroup;

  @ViewChild('createRecipeModal') createModalElement!: ElementRef;
  @ViewChild('editRecipeModal') editModalElement!: ElementRef;
  private createModal: Modal | undefined;
  private editModal: Modal | undefined;
  private refresh$ = new Subject<void>();
  private editingRecipeId: number | null = null;

  constructor() {
    this.recipeForm = this.fb.group({
      titulo: ['', Validators.required],
      descricao: ['', Validators.required],
      ingredientes: ['', Validators.required],
      pathImg: ['']
    });

    this.editForm = this.fb.group({
      titulo: ['', Validators.required],
      descricao: ['', Validators.required],
      ingredientes: ['', Validators.required],
      pathImg: ['']
    });
  }

  ngOnInit(): void {
    this.myRecipes$ = this.refresh$.pipe(
      startWith(null),
      switchMap(() => this.recipeService.getMyRecipes())
    );
  }

  ngAfterViewInit(): void {
    if (this.createModalElement) {
      this.createModal = new Modal(this.createModalElement.nativeElement);
    }
    if (this.editModalElement) {
      this.editModal = new Modal(this.editModalElement.nativeElement);
    }
  }

  ngOnDestroy(): void {
    this.createModal?.dispose();
    this.editModal?.dispose();
  }

  // --- Create Methods ---
  openCreateModal(): void {
    this.recipeForm.reset();
    this.createModal?.show();
  }

  closeCreateModal(): void {
    this.createModal?.hide();
  }

  onSubmit(): void {
    if (this.recipeForm.invalid) return;

    this.recipeService.createRecipe(this.recipeForm.value).subscribe({
      next: () => {
        this.refresh$.next();
        this.closeCreateModal();
      },
      error: (err) => console.error('Failed to create recipe', err)
    });
  }

  // --- Edit Methods ---
  openEditModal(recipe: Recipe): void {
    this.editingRecipeId = recipe.id;
    this.editForm.patchValue(recipe);
    this.editModal?.show();
  }

  closeEditModal(): void {
    this.editModal?.hide();
    this.editingRecipeId = null;
  }

  onEditSubmit(): void {
    if (this.editForm.invalid || !this.editingRecipeId) return;

    this.recipeService.updateRecipe(this.editingRecipeId, this.editForm.value).subscribe({
      next: () => {
        this.refresh$.next();
        this.closeEditModal();
      },
      error: (err) => console.error('Failed to update recipe', err)
    });
  }

  // --- Delete Method ---
  onDelete(recipeId: number): void {
    if (confirm('Tem certeza que deseja excluir esta receita?')) {
      this.recipeService.deleteRecipe(recipeId).subscribe({
        next: () => {
          this.refresh$.next();
        },
        error: (err) => console.error('Failed to delete recipe', err)
      });
    }
  }
}
