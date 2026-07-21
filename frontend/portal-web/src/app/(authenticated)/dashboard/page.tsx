'use client';

import { HouseholdCard } from '../../../features/dashboard/components/household-card';
import { BenefitsCard } from '../../../features/dashboard/components/benefits-card';
import { NotificationsCard } from '../../../features/dashboard/components/notifications-card';
import { QuickActions } from '../../../features/dashboard/components/quick-actions';
import { useDashboard } from '../../../features/dashboard/hooks/use-dashboard';
import { AuthGuard } from "../../../features/auth/components/auth-guard";
import { DocumentsCard } from '@/src/features/dashboard/components/documents-card';
import { ApplicationsCard } from "@/src/features/dashboard/components/applications-card";


export default function DashboardPage() {
  const { data, isLoading } = useDashboard();


  if (isLoading) {
    return <div>Loading...</div>;
  }

  if (!data) {
    return <div>No data available</div>;
  }

  return (
    <AuthGuard>

      <div className="container mx-auto space-y-6 p-6">
        <h1 className="text-3xl font-bold">
          Dashboard
        </h1>

        <QuickActions />

        <div className="grid gap-6 md:grid-cols-3">
          <HouseholdCard
            {...data.household}
          />

          <BenefitsCard
            {...data.benefits}
          />

          <NotificationsCard
            {...data.notifications}
          />

          <DocumentsCard
            {...data.documents}
          />

          <ApplicationsCard
            {...data.applications}
          />





        </div>
      </div>
    </AuthGuard>
  );
}