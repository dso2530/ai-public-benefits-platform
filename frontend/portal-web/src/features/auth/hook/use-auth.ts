"use client";

import { useRouter } from "next/navigation";
import { useEffect, useState } from "react";
import { api } from "../../../shared/api/axios";

export interface User {
  subject: string;
  email: string;
  name: string;
}

export const useAuth = () => {

  const router = useRouter();

  const [user, setUser] = useState<User | null>(null);
  const [loading, setLoading] = useState(true);

  const login = () => {

    window.location.href =
      "http://localhost:8084/oauth2/authorization/keycloak";
  };

  const logout = async () => {

    try {
      await api.post("/api/auth/logout");
    } finally {
      setUser(null);
      router.push("/login");
    }
  };

  const loadUser = async () => {

    try {

      const response = await api.get("/api/me", {
        withCredentials: true,
      });

      setUser(response.data);

    } catch {

      setUser(null);

    } finally {

      setLoading(false);
    }
  };

  useEffect(() => {
    loadUser();
  }, []);
  return {
    user,
    loading,
    isAuthenticated: !!user,
    login,
    logout,
    refreshUser: loadUser,
  };
};