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