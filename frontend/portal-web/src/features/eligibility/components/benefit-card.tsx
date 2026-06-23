import {
  Card,
  CardContent,
  CardHeader,
  CardTitle,
} from "../../../shared/components/ui/card";

import { Eligibility } from "../types/eligibility";

interface BenefitCardProps {
  readonly benefit: Eligibility;
}

export function BenefitCard({ benefit }: BenefitCardProps) {
  return (
    <Card>
      <CardHeader>
        <CardTitle>{benefit.label}</CardTitle>
      </CardHeader>

      <CardContent>
        <p>Code : {benefit.code}</p>
        <p>Montant : {benefit.amount} €</p>
      </CardContent>
    </Card>
  );
}