import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, BehaviorSubject, of } from 'rxjs';
import { tap, catchError, switchMap } from 'rxjs/operators';
import { User } from '../models/user.model';
import { HttpErrorResponse } from '@angular/common/http'; // ADDED

// --- Interfaces ---
export interface UserRegistration {
  email: string;
  username: string;
  password: string;
}
export interface UserLogin {
  email: string;
  password: string;
}

interface AuthResponse {
  token: string;
}

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private http = inject(HttpClient);
  private apiUrl = 'http://localhost:8080/usuarios';
  private token: string | null = null;

  private loggedIn = new BehaviorSubject<boolean>(false);
  private currentUser = new BehaviorSubject<User | null>(null);

  isLoggedIn$ = this.loggedIn.asObservable();
  currentUser$ = this.currentUser.asObservable();

  constructor() {
    this.loadToken();
    // Initialize loggedIn based on token presence (immediate, unconfirmed state)
    this.loggedIn.next(!!this.token && !this.isTokenExpired(this.token));
    // Removed checkAuthStatus() call from here
  }

  // New method to be called after service is fully constructed
  initAuth(): void {
    if (this.token && !this.isTokenExpired(this.token)) {
      this.checkAuthStatus().subscribe({
        next: () => {
          // checkAuthStatus's tap block already calls loggedIn.next(true) on success
        },
        error: () => {
          // checkAuthStatus's catchError already handles logout and loggedIn.next(false)
        }
      });
    } else {
      // If no token or expired token, ensure loggedIn is false
      this.loggedIn.next(false);
      this.currentUser.next(null);
    }
  }

  getToken(): string | null {
    if (!this.token) {
      return null;
    }
    // Check token expiration before returning
    if (this.isTokenExpired(this.token)) {
      console.log('Token expired on getToken call, logging out.');
      this.logout();
      return null;
    }
    return this.token;
  }

  private loadToken(): void {
    if (typeof localStorage !== 'undefined') {
      this.token = localStorage.getItem('jwt_token');
    }
  }

  private saveToken(token: string): void {
    this.token = token;
    if (typeof localStorage !== 'undefined') {
      localStorage.setItem('jwt_token', token);
    }
  }

  private clearToken(): void {
    this.token = null;
    if (typeof localStorage !== 'undefined') {
      localStorage.removeItem('jwt_token');
    }
  }

  private isTokenExpired(token: string): boolean {
    try {
      const payload = JSON.parse(atob(token.split('.')[1]));
      if (payload.exp) {
        const expirationTime = payload.exp * 1000; // Convert to milliseconds
        return expirationTime < Date.now();
      }
      return false; // No expiration claim, treat as not expired
    } catch (e) {
      console.error('Error decoding token:', e);
      return true; // Treat malformed token as expired
    }
  }

  checkAuthStatus(): Observable<User | null> {
    // The interceptor will add the token header.
    return this.http.get<User>(`${this.apiUrl}/validar_usuario`).pipe(
      tap(user => {
        console.log('checkAuthStatus: User validated successfully', user); // ADDED
        this.loggedIn.next(true);
        this.currentUser.next(user);
      }),
      catchError((err: HttpErrorResponse) => {
        console.error('checkAuthStatus: Error during validation', err); // ADDED
        if (err.status === 401) {
          this.logout();
        } else {
          this.loggedIn.next(false);
          this.currentUser.next(null);
        }
        return of(null);
      })
    );
  }

  login(credentials: UserLogin): Observable<User | null> {
    return this.http.post<AuthResponse>(`${this.apiUrl}/login`, credentials).pipe(
      tap(response => this.saveToken(response.token)),
      switchMap(() => this.checkAuthStatus()),
      tap(() => window.location.reload()) // <--- ADD THIS LINE
    );
  }

  logout(): void {
    // Call backend logout to allow for server-side invalidation if ever implemented
    // this.http.post(`${this.apiUrl}/logout`, {}).subscribe(); // Removed to break circular dependency
    this.clearToken();
    this.loggedIn.next(false);
    this.currentUser.next(null);
    window.location.reload(); // <--- ADD THIS LINE
  }

  register(userData: UserRegistration): Observable<any> {
    return this.http.post(this.apiUrl, userData);
  }
}
