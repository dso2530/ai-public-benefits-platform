import { Bell, MailOpen, Eye } from "lucide-react";

import { Card, CardContent, CardHeader, CardTitle } from "../../../shared/components/ui/card";

interface Props {
  readonly total: number;
  readonly unread: number;
}

export function NotificationsCard({
  total,
  unread,
}: Props) {
  return (
    <Card className="h-full">
      <CardHeader className="flex flex-row items-center justify-between pb-3">
        <CardTitle className="flex items-center gap-2 text-lg font-bold">
          <Bell className="h-5 w-5 text-amber-500" />
          Notifications
        </CardTitle>

        <a
          href="/notifications"
          className="flex items-center gap-1 text-sm text-primary hover:underline"
        >
          <Eye className="h-5 w-5 text-primary" />
        </a>
      </CardHeader>

      <CardContent className="space-y-3 text-sm">
        <div className="flex justify-between">
          <span className="font-medium">Total</span>
          <span>{total}</span>
        </div>

        <div className="flex justify-between">
          <span className="flex items-center gap-2">
            <MailOpen className="h-4 w-4 text-orange-500" />
            Non lues
          </span>
          <span className="font-semibold">{unread}</span>
        </div>
      </CardContent>
    </Card>
  );
}