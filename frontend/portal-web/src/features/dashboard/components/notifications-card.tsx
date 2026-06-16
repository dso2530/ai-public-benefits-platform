import { Card, CardContent, CardHeader, CardTitle } from '../../../shared/components/ui/card';

interface Props {
  readonly unreadCount: number;
}

export function NotificationsCard({
  unreadCount,
}: Props) {
  return (
    <Card>
      <CardHeader>
        <CardTitle>Notifications</CardTitle>
      </CardHeader>

      <CardContent>
        <p>{unreadCount} unread notifications</p>
      </CardContent>
    </Card>
  );
}