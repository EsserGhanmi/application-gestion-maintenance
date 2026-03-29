import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { AuthService } from '../../../../core/services/auth';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './login.html',
  styleUrl: './login.scss'
})
export class LoginComponent {
  errorMessage = '';
  loading = false;

  loginForm = this.fb.group({
    email: ['', [Validators.required, Validators.email]],
    password: ['', [Validators.required]]
  });

  constructor(
    private fb: FormBuilder,
    private authService: AuthService,
    private router: Router
  ) {}

  onSubmit(): void {
    if (this.loginForm.invalid) {
      this.loginForm.markAllAsTouched();
      return;
    }

    const { email, password } = this.loginForm.getRawValue();
    this.loading = true;
    this.errorMessage = '';

    this.authService.login(email!, password!).subscribe({
      next: (success) => {
        this.loading = false;
        if (success) {
          this.router.navigate(['/interventions']);
        } else {
          this.errorMessage = 'Email ou mot de passe incorrect.';
        }
      },
      error: () => {
        this.loading = false;
        this.errorMessage = 'Erreur lors de la connexion.';
      }
    });
  }
}