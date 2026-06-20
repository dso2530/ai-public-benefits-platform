export interface HouseholdSummary {
  city: string;
  housingStatus: string;
  children: number;
  singleParent: boolean;
}

export interface BenefitSummary {
  eligibleCount: number;
  pendingCount: number;
  totalAmount: number;
}

export interface NotificationSummary {
  total: number;
  unread: number;
}

export interface DocumentSummary {
  total: number;
  validated: number;
  pending: number;
}

export interface DashboardData {
  household: HouseholdSummary;
  benefits: BenefitSummary;
  notifications: NotificationSummary;
  documents : DocumentSummary;
}