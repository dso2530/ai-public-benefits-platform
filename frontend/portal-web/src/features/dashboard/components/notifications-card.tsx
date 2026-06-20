import { Card, CardContent, CardHeader, CardTitle } from '../../../shared/components/ui/card';

interface Props {
  readonly total: number;
  readonly unread: number;

}

export function NotificationsCard({
  total, 
  unread
}: Props) {
  return (
    <Card>
      <CardHeader>
        <CardTitle>Notifications</CardTitle>
      </CardHeader>

      <CardContent className="space-y-2">
        <p>{total} notifications</p>
        <p>{unread} unread notifications</p>
      </CardContent>

      
    </Card>
  );
}