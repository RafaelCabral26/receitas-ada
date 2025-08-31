import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { AuthService } from '../../services/auth.service';
import { HttpErrorResponse } from '@angular/common/http';

@Component({
  selector: 'app-login-modal',
  templateUrl: './login-modal.html',
  styleUrls: ['./login-modal.scss'],
  standalone: true,
  imports: [FormsModule, CommonModule],
})
export class LoginModalComponent {
  // Dependencies
  activeModal = inject(NgbActiveModal);
  authService = inject(AuthService);

  // Form data
  credentials = {
    email: '',
    password: ''
  };

  // State management
  isLoading = false;
  errorMessage: string | null = null;

  onSubmit() {
    this.isLoading = true;
    this.errorMessage = null;

    this.authService.login(this.credentials).subscribe({
      next: (response) => {
        this.isLoading = false;
        console.log('Login successful:', response);
        this.activeModal.close({ success: true });
      },
      error: (err: HttpErrorResponse) => {
        this.isLoading = false;
        this.errorMessage = err.error?.message || err.error || 'Credenciais inválidas.';
        console.error(err);
      }
    });
  }
}
