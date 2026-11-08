import type { AuthResponse, PlanRequest, TravelPlanResponse } from './types';
const API_BASE = (import.meta as any).env?.VITE_API_URL ?? '';
async function handle<T>(res: Response): Promise<T> {
  if (!res.ok) { let message = `Request failed (${res.status})`; try { const body = await res.json(); message = body.message || body.error || message; } catch { /* keep default */ } throw new Error(message); }
  return res.json() as Promise<T>;
}
export async function login(email: string, password: string): Promise<AuthResponse> {
  const res = await fetch(`${API_BASE}/api/users/login`, { method: 'POST', headers: { 'Content-Type': 'application/json' }, body: JSON.stringify({ email, password }) });
  return handle<AuthResponse>(res);
}
export async function planTrip(request: PlanRequest, token?: string): Promise<TravelPlanResponse> {
  const res = await fetch(`${API_BASE}/ai/plan`, { method: 'POST', headers: { 'Content-Type': 'application/json', ...(token ? { Authorization: `Bearer ${token}` } : {}) }, body: JSON.stringify(request) });
  return handle<TravelPlanResponse>(res);
}
