import { Card, CardContent, CardHeader, CardTitle } from '../../../shared/components/ui/card';

interface Props {
  city: string;
  housingStatus: string;
  children: number;
  singleParent: boolean;
}

export function HouseholdCard({
  city,
  housingStatus,
  children,
  singleParent,
}: Props) {
  return (
    <Card>
      <CardHeader>
        <CardTitle>Household</CardTitle>
      </CardHeader>

      <CardContent className="space-y-2">
        <p>City: {city}</p>
        <p>Housing: {housingStatus}</p>
        <p>Children: {children}</p>
        <p>
          Status:{' '}
          {singleParent ? 'Single Parent' : 'Two Parents'}
        </p>
      </CardContent>
    </Card>
  );
}