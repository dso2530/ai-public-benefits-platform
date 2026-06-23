import { api } from "@/src/shared/api/axios";

export interface Eligibility {
  code: string;
  label: string;
  amount: number;
}

export const getBenefits = async (): Promise<Eligibility[]> => {
  const response = await api.get<Eligibility[]>("/api/eligibility");
  return response.data;
};