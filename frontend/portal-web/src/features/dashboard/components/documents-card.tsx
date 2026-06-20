import { Card, CardContent, CardHeader, CardTitle } from '../../../shared/components/ui/card';

interface Props {
  readonly total: number;
  readonly validated: number;
  readonly pending: number;

}

export function DocumentsCard({
  total,
  validated,
  pending
}: Props) {

return  ( <Card>
  <CardHeader>
    <CardTitle>Documents</CardTitle>
  </CardHeader>

  <CardContent className="space-y-2">
    <p>Total : {total}</p>
    <p>Validated: {validated}</p>
    <p>Pending : {pending}</p>
  </CardContent>
</Card>);

}