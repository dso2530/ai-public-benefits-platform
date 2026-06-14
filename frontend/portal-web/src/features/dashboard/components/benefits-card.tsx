import { Card, CardContent, CardHeader, CardTitle } from '../../../shared/components/ui/card';

interface Props {
  eligibleCount: number;
  pendingCount: number;
  totalAmount: number;
}

export function BenefitsCard({
  eligibleCount,
  pendingCount,
  totalAmount,
}: Props) {
  return (
    <Card>
      <CardHeader>
        <CardTitle>Benefits</CardTitle>
      </CardHeader>

      <CardContent className="space-y-2">
        <p>Eligible: {eligibleCount}</p>
        <p>Pending: {pendingCount}</p>
        <p>
          Potential Amount:{' '}
          {totalAmount.toLocaleString('fr-FR')} €
        </p>
      </CardContent>
    </Card>
  );
}