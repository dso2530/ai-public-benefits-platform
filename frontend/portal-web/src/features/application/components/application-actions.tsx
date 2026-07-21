"use client";

import { useState } from "react";

import { Application } from "../types/application";
import { SubmitApplicationButton } from "./submit-application-button";
import { UploadDocumentsDialog } from "./upload-documents-dialog";
import { downloadPackage } from "../services/application.service";

type Props = {
    readonly application: Application;
};

export function ApplicationActions({ application }: Props) {
    const [isUploadOpen, setIsUploadOpen] = useState(false);

    const handleDownload = async () => {
        const { blob, filename } = await downloadPackage(application.id);

        const url = window.URL.createObjectURL(blob);

        const link = document.createElement("a");
        link.href = url;
        link.download = filename;
        link.click();

        window.URL.revokeObjectURL(url);
    };

    return (
        <div className="mt-4 flex gap-2">
            {application.status === "READY_TO_SUBMIT" && (
                <>
                    <button
                        type="button"
                        onClick={handleDownload}
                        className="rounded border px-3 py-2 hover:bg-gray-100"
                    >
                        Télécharger mon dossier
                    </button>

                    <SubmitApplicationButton applicationId={application.id} />
                </>
            )}

            {application.status === "READY_TO_COMPLETE" && (
                <>
                    <button
                        type="button"
                        onClick={() => setIsUploadOpen(true)}
                        className="rounded bg-amber-600 px-3 py-2 text-white hover:bg-amber-700"
                    >
                        Ajouter les documents
                    </button>

                    <UploadDocumentsDialog
                        applicationId={application.id}
                        missingDocuments={application.missingDocuments}
                        open={isUploadOpen}
                        onOpenChange={setIsUploadOpen}
                        onUploaded={() => {
                            // rafraîchir la liste après upload réussi
                            window.location.reload();
                        }}
                    />
                </>
            )}

            {application.status === "SUBMITTED" && (
                <button
                    type="button"
                    onClick={handleDownload}
                    className="rounded border px-3 py-2 hover:bg-gray-100"
                >
                    Télécharger mon dossier
                </button>
            )}
        </div>
    );
}