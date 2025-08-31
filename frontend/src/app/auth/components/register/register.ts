import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { AuthService } from '../../services/auth.service';
import { HttpErrorResponse } from '@angular/common/http';

@Component({
  selector: 'app-register',
  templateUrl: './register.html',
  styleUrls: ['./register.scss'],
  standalone: true,
  imports: [FormsModule, CommonModule],
})
export class RegisterComponent {
  // Dependencies
  activeModal = inject(NgbActiveModal);
  authService = inject(AuthService);

  // Form data
  credentials = {
    email: '',
    username: '', // Adicionado
    password: '',
    confirmPassword: ''
  };

  // State management
  isLoading = false;
  errorMessage: string | null = null;

  onSubmit() {
    // Reset state and perform basic validation
    this.errorMessage = null;
    if (this.credentials.password !== this.credentials.confirmPassword) {
      this.errorMessage = 'As senhas não coincidem.';
      return;
    }

    this.isLoading = true;
    // Inclui o username na desestruturação
    const { email, username, password } = this.credentials;

    this.authService.register({ email, username, password }).subscribe({
      next: () => {
        this.isLoading = false;
        // On success, close the modal and pass a success result
        this.activeModal.close({ success: true });
      },
      error: (err: HttpErrorResponse) => {
        this.isLoading = false;
        // Use the error message from the backend if available, otherwise a generic one
        this.errorMessage = err.error?.message || err.error || 'Ocorreu um erro ao tentar registrar.';
        console.error(err);
      }
    });
  }
}
