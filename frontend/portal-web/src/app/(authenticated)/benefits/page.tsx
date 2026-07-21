"use client";

import { BenefitCard } from "../../../features/eligibility/components/benefit-card";
import { useBenefits } from "../../../features/eligibility/hooks/useBenefits";

export default function BenefitsPage() {
  const { benefits, loading } = useBenefits();

  if (loading) {
    return <div>Chargement...</div>;
  }

  return (<div className="mx-auto max-w-5xl p-8">

    <h1 className="text-4xl font-bold">
        Prestations
    </h1>

    <p className="text-muted-foreground mt-2">
        Consultez les aides auxquelles vous êtes éligible.
    </p>

    <div className="grid gap-6 mt-8 md:grid-cols-2">
        {benefits.map(benefit => (
            <BenefitCard
                key={benefit.aidCode}
                benefit={benefit}
            />
        ))}
    </div>

</div>
    
  );
}