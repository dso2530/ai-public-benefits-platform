import { api } from "../../../shared/api/axios";
import { Notification } from "../types/notification";

export async function getNotifications(): Promise<Notification[]> {
    const { data } = await api.get<Notification[]>("/api/notifications");
    return data;
}

export async function markNotificationAsRead(id: string): Promise<void> {
    await api.patch(`/api/notifications/${id}/read`);
}