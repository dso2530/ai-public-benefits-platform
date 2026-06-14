import axios from "axios";

export const api = axios.create({
  //baseURL: process.env.NEXT_PUBLIC_API_URL,
  //baseURL: "/api",

  timeout: 30000
 // ,withCredentials: true,
});