import { Bell } from "lucide-react";
import { Notification } from "../types/notification";

type Props = {
    readonly notification: Notification;
};

export function NotificationCard({ notification }: Props) {
    return (
        <div className="rounded-lg border bg-white p-4 shadow-sm">
            <div className="flex items-start gap-3">
                <Bell className="mt-1 h-5 w-5 text-blue-600" />

                <div className="flex-1">
                    <h3 className="font-semibold">{notification.title}</h3>

                    <p className="mt-1 text-sm text-muted-foreground">
                        {notification.message}
                    </p>

                    <p className="mt-3 text-xs text-muted-foreground">
                        📅{" "}
                        {new Date(notification.createdAt).toLocaleString("fr-FR", {
                            dateStyle: "short",
                            timeStyle: "short",
                        })}
                    </p>
                </div>
            </div>
        </div>
    );
}