import { Component, OnInit, ViewChild, ElementRef, OnDestroy } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { AuthService } from '../../services/auth.service'; // Import the correct AuthService
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule } from '@angular/forms';
import { Modal } from 'bootstrap';
import { Subscription } from 'rxjs';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './login.html',
  styleUrls: ['./login.scss']
})
export class Login implements OnInit, OnDestroy {
  @ViewChild('loginModal') modalElementRef!: ElementRef;
  loginForm: FormGroup;
  errorMessage: string | null = null;
  private modal: Modal | undefined;
  private loginSubscription: Subscription | undefined;

  constructor(
    private fb: FormBuilder,
    private authService: AuthService, // Use the correct AuthService
    private router: Router
  ) {
    this.loginForm = this.fb.group({
      email: ['', [Validators.required, Validators.email]],
      password: ['', Validators.required]
    });
  }

  ngOnInit(): void {
    // AuthService already checks for auth status in its constructor.
    // We can subscribe to the login status to react to changes.
    this.loginSubscription = this.authService.isLoggedIn$.subscribe(isLoggedIn => {
      if (isLoggedIn) {
        this.close();
        this.router.navigate(['/recipes']);
      }
    });
  }

  ngOnDestroy(): void {
    this.loginSubscription?.unsubscribe();
  }

  ngAfterViewInit(): void {
    if (this.modalElementRef) {
      this.modal = new Modal(this.modalElementRef.nativeElement);
    }
  }

  open(): void {
    this.modal?.show();
  }

  close(): void {
    this.modal?.hide();
  }

  onSubmit(): void {
    this.errorMessage = null;
    if (this.loginForm.valid) {
      this.authService.login(this.loginForm.value).subscribe({
        error: (err) => {
          console.error('Login error:', err);
          this.errorMessage = 'Credenciais inválidas. Por favor, tente novamente.';
        }
        // On success, the isLoggedIn$ observable will fire,
        // and the subscription in ngOnInit will handle navigation.
      });
    } else {
      this.errorMessage = 'Por favor, preencha todos os campos obrigatórios.';
    }
  }
}