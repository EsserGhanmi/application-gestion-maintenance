import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { BehaviorSubject, Observable, catchError, map, of, tap } from 'rxjs';
import { environment } from '../../../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private readonly storageKey = 'auth_token';

  private isAuthenticatedSubject = new BehaviorSubject<boolean>(this.hasToken());
  public isAuthenticated$ = this.isAuthenticatedSubject.asObservable();

  constructor(private http: HttpClient) {}

  login(email: string, password: string): Observable<boolean> {
    const token = 'Basic ' + btoa(`${email}:${password}`);

    return this.http.get(`${environment.apiBaseUrl}/interventions`, {
      headers: new HttpHeaders({
        Authorization: token
      })
    }).pipe(
      tap(() => {
        sessionStorage.setItem(this.storageKey, token);
        this.isAuthenticatedSubject.next(true);
      }),
      map(() => true),
      catchError(() => of(false))
    );
  }

  logout(): void {
    sessionStorage.removeItem(this.storageKey);
    this.isAuthenticatedSubject.next(false);
  }

  getToken(): string | null {
    return sessionStorage.getItem(this.storageKey);
  }

  hasToken(): boolean {
    return !!sessionStorage.getItem(this.storageKey);
  }
}