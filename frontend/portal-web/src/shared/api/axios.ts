import axios from "axios";

export const api = axios.create({
  //baseURL: process.env.NEXT_PUBLIC_API_URL,
  timeout: 30000
 // ,withCredentials: true,
 
});

api.interceptors.request.use((config) => {

  const token =
    localStorage.getItem("access_token");

  if (token) {

    config.headers.Authorization =
      `Bearer ${token}`;
  }

  return config;
});