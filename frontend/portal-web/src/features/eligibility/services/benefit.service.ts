import { api } from "@/src/shared/api/axios";

export interface Eligibility {
  aidCode: string;
  aidName: string;
  description: string;
  category: string;
  status: "ELIGIBLE" | "NOT_ELIGIBLE" | "PENDING";
  estimatedAmount: number | null;
  estimatedAmountLabel: string;
  reason: string;
  canApply: boolean;
  actionLabel: string;
  icon: string;
  applicationId?: string | null;
}

export const getBenefits = async (): Promise<Eligibility[]> => {
  const response = await api.get<Eligibility[]>("/api/eligibility");
  return response.data;
};