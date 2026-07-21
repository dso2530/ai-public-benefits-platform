"use client";

import { useApplications } from "../hooks/useApplications";
import { ApplicationCard } from "./application-card";

export function ApplicationList() {
    const { data, isLoading, error } = useApplications();

    if (isLoading) {
        return (
            <div className="space-y-3">
                <p>Chargement des demandes...</p>
            </div>
        );
    }

    if (error) {
        return (
            <p className="text-red-600">
                Impossible de charger les demandes.
            </p>
        );
    }

    if (!data?.length) {
        return (
            <div className="rounded-lg border p-8 text-center">
                <p className="text-gray-500">
                    Aucune demande générée.
                </p>
            </div>
        );
    }

    return (
        <div className="space-y-4">
            {data.map((application) => (
                <ApplicationCard
                    key={application.id}
                    application={application}
                />
            ))}
        </div>
    );
}