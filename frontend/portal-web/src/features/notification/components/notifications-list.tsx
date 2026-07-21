"use client";

import { useEffect } from "react";

import { NotificationCard } from "./notification-card";
import { useNotifications } from "../hooks/useNotifications";
import { markNotificationAsRead } from "../services/notification.service";

export function NotificationsList() {
    const { data: notifications = [], isLoading } = useNotifications();

    useEffect(() => {
        notifications.forEach((notification) => {
            if (!notification.read) {
                markNotificationAsRead(notification.id);
            }
        });
    }, [notifications]);

    if (isLoading) {
        return <p>Chargement...</p>;
    }

    return (
        <div className="space-y-4">
            {notifications.map((notification) => (
                <NotificationCard
                    key={notification.id}
                    notification={notification}
                />
            ))}
        </div>
    );
}