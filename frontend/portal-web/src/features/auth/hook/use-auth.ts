"use client";

import { useRouter } from "next/navigation";
import { useQuery } from "@tanstack/react-query";
import { api } from "../../../shared/api/axios";

export interface User {
  subject: string;
  email: string;
  name: string;
}

export const useAuth = () => {
  const router = useRouter();

  const {
    data: user,
    isLoading: loading,
    refetch: refreshUser,
  } = useQuery<User | null>({
    queryKey: ["me"],
    queryFn: async () => {
      try {
        const response = await api.get("/api/me", {
          withCredentials: true,
        });

        return response.data;
      } catch {
        return null;
      }
    },
    retry: false,
  });

  const login = () => {
    globalThis.location.href =
      "http://localhost:8084/oauth2/authorization/keycloak";
  };

  const logout = async () => {
    try {
      await api.post("/api/auth/logout");
    } finally {
      router.push("/login");
      refreshUser();
    }
  };

  return {
    user,
    loading,
    isAuthenticated: !!user,
    login,
    logout,
    refreshUser,
  };
};