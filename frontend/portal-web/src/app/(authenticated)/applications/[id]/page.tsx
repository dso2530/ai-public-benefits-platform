"use client";

import { useEffect, useState } from "react";
import { useParams } from "next/navigation";

import { getApplication } from "@/src/features/application/services/application.service";
import { Application } from "@/src/features/application/types/application";
import { ApplicationCard } from "@/src/features/application/components/application-card";


export default function ApplicationPage() {
    const { id } = useParams<{ id: string }>();

    const [application, setApplication] = useState<Application | null>(null);

    useEffect(() => {
        if (!id) return;

        getApplication(id)
            .then(setApplication)
            .catch(console.error);
    }, [id]);

    if (!application) {
        return <div>Chargement...</div>;
    }

    return <ApplicationCard
        application={application}
        detailed
    />;
}