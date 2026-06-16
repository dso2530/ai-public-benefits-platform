// features/auth/services/auth.service.ts

import { api } from "../../../shared/api/axios";

import { Me } from "../types/me";

export const authService = {

  getCurrentUser: async () => {

    const response =
      await api.get<Me>(
        "/api/me"
      );

    return response.data;
  }
};