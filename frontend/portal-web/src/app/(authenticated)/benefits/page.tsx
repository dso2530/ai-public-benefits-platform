"use client";

import { BenefitCard } from "../../../features/eligibility/components/benefit-card";
import { useBenefits } from "../../../features/eligibility/hooks/useBenefits";

export default function BenefitsPage() {
  const { benefits, loading } = useBenefits();

  if (loading) {
    return <div>Chargement...</div>;
  }

  return (
    <div className="container mx-auto p-6">
      <h1 className="text-2xl font-bold mb-6">
        Benefits
      </h1>

      <div className="grid gap-4 md:grid-cols-2">
        {benefits.map((benefit) => (
          <BenefitCard
            key={benefit.code}
            benefit={benefit}
          />
        ))}
      </div>
    </div>
  );
}