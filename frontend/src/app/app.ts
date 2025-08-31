import { Component, signal, OnInit, inject } from '@angular/core'; // Add OnInit and inject
import { NavbarComponent } from './core/components/navbar/navbar';
import { RouterOutlet } from '@angular/router';
import { AuthService } from './auth/services/auth.service'; // Import AuthService

@Component({
  selector: 'app-root',
  templateUrl: './app.html',
  standalone: true,
  styleUrl: './app.scss',
  imports: [RouterOutlet, NavbarComponent],
})
export class App implements OnInit { // Implement OnInit
  protected readonly title = signal('frontend');
  private authService = inject(AuthService); // Inject AuthService

  ngOnInit(): void {
    this.authService.initAuth(); // Call initAuth here
  }
}
