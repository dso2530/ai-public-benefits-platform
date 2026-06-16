import { api } from '../../../shared/api/axios';
import { DashboardData } from '../types/dashboard.types';

export async function getDashboard(): Promise<DashboardData> {
  const { data } = await api.get('/api/dashboard');

  return data;
}