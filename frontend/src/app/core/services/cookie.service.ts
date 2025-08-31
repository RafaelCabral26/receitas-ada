import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class CookieService {

  /**
   * Obtém o valor de um cookie pelo nome.
   * @param name O nome do cookie.
   * @returns O valor do cookie ou null se não for encontrado.
   */
  get(name: string): string | null {
    const nameEQ = name + '=';
    const ca = document.cookie.split(';');
    for (let i = 0; i < ca.length; i++) {
      let c = ca[i];
      while (c.charAt(0) === ' ') {
        c = c.substring(1, c.length);
      }
      if (c.indexOf(nameEQ) === 0) {
        return c.substring(nameEQ.length, c.length);
      }
    }
    return null;
  }

  /**
   * Apaga um cookie pelo nome.
   * @param name O nome do cookie a ser apagado.
   */
  delete(name: string): void {
    // Para apagar um cookie, definimos uma data de expiração no passado.
    document.cookie = name + '=; Path=/; Expires=Thu, 01 Jan 1970 00:00:01 GMT;';
  }
}
