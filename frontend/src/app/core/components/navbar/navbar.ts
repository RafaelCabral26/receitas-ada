import { Component, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router'; // Importa o RouterModule
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { LoginModalComponent } from '../../../auth/components/login-modal/login-modal';
import { RegisterComponent } from '../../../auth/components/register/register';
import { AuthService } from '../../../auth/services/auth.service';

@Component({
  selector: 'app-navbar',
  templateUrl: './navbar.html',
  styleUrls: ['./navbar.scss'],
  standalone: true,
  imports: [CommonModule, RouterModule] // Adiciona o RouterModule aqui
})
export class NavbarComponent {
  // Services
  private modalService = inject(NgbModal);
  // Deixamos o authService como público para ser acessado no template
  authService = inject(AuthService);

  openLoginModal() {
    this.modalService.open(LoginModalComponent);
  }

  openRegisterModal() {
    this.modalService.open(RegisterComponent);
  }

  logout() {
    this.authService.logout();
  }
}
