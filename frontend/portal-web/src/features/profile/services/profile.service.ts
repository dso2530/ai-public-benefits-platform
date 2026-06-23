import { api } from '../../../shared/api/axios';
import { Profile } from "../types/profile";

export const getProfile = async (): Promise<Profile> => {
  const response = await api.get<Profile>("/api/profile/me");
  return response.data;
};