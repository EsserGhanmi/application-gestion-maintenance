import { CommonModule } from '@angular/common';
import { ChangeDetectionStrategy, Component, computed, inject, signal } from '@angular/core';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { AuthService } from '../../../../core/services/auth';
import { ActivatedRoute, Router } from '@angular/router';

@Component({
  selector: 'app-login',
  
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './login.html',
  styleUrl: './login.scss',
  changeDetection: ChangeDetectionStrategy.OnPush,

})
export class LoginComponent {
  private readonly fb = inject(FormBuilder);
  private readonly authService = inject(AuthService);
  private readonly router = inject(Router);
  private readonly route = inject(ActivatedRoute);

  readonly loading = signal(false);
  readonly errorMessage = signal('');
  readonly isSubmitDisabled = computed(() => this.loading() || this.loginForm.invalid);
 

  readonly loginForm = this.fb.nonNullable.group({
    email: ['', [Validators.required, Validators.email]],
    password: ['', [Validators.required]],
  });

  

  onSubmit(): void {
    if (this.loginForm.invalid) {
      this.loginForm.markAllAsTouched();
      return;
    }

    const { email, password } = this.loginForm.getRawValue();
     this.loading.set(true);
    this.errorMessage.set('');

    this.authService.login(email, password).subscribe((isAuthenticated) => {
      this.loading.set(false);

      if (!isAuthenticated) {
        this.errorMessage.set('Identifiants invalides. Vérifiez votre email et mot de passe.');
        return;
      }
       const redirectTo = this.route.snapshot.queryParamMap.get('redirectTo') || '/interventions';
      void this.router.navigateByUrl(redirectTo);
    });
  }
}