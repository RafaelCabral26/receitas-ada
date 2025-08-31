import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Recipe } from '../models/recipe.model';

@Injectable({
  providedIn: 'root'
})
export class RecipeService {
  private http = inject(HttpClient);
  private apiUrl = 'http://localhost:8080/receitas';

  /**
   * Busca a lista completa de receitas do backend.
   */
  getRecipes(): Observable<Recipe[]> {
    return this.http.get<Recipe[]>(this.apiUrl);
  }

  /**
   * Busca apenas as receitas do usuário autenticado.
   */
  getMyRecipes(): Observable<Recipe[]> {
    return this.http.get<Recipe[]>(`${this.apiUrl}/my-recipes`);
  }

  /**
   * Cria uma nova receita no backend.
   * @param recipeData Os dados da receita a ser criada.
   */
  createRecipe(recipeData: Partial<Recipe>): Observable<Recipe> {
    return this.http.post<Recipe>(this.apiUrl, recipeData);
  }

  /**
   * Atualiza uma receita existente no backend.
   * @param id O ID da receita a ser atualizada.
   * @param recipeData Os novos dados da receita.
   */
  updateRecipe(id: number, recipeData: Partial<Recipe>): Observable<Recipe> {
    return this.http.put<Recipe>(`${this.apiUrl}/${id}`, recipeData);
  }

  /**
   * Exclui uma receita do backend.
   * @param id O ID da receita a ser excluída.
   */
  deleteRecipe(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }

  /**
   * Registra um voto para uma receita.
   * @param recipeId O ID da receita a ser votada.
   * @param valor O valor do voto (ex: 1 para positivo).
   */
  vote(recipeId: number, valor: number = 1): Observable<any> {
    return this.http.post(`${this.apiUrl.replace('receitas', 'votos')}/${recipeId}/${valor}`, {});
  }

  /**
   * Remove o voto de uma receita.
   * @param recipeId O ID da receita cujo voto será removido.
   */
  removeVote(recipeId: number): Observable<any> {
    return this.http.delete(`${this.apiUrl.replace('receitas', 'votos')}/${recipeId}`);
  }
}
